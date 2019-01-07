package com.pineconeindustries.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import com.pineconeindustries.server.data.Packet;
import com.pineconeindustries.server.data.PacketParser;
import com.pineconeindustries.server.data.PlayerData;
import com.pineconeindustries.server.data.ShipData;
import com.pineconeindustries.server.data.Structure;
import com.pineconeindustries.server.database.Database;
import com.pineconeindustries.server.log.Log;

public class ServerZone implements Runnable {

	private int port;
	private String name;
	private boolean isConnected;
	private boolean stateChange = false;

	Thread runThread;
	SenderThread senderThread;
	ServerSocket zoneServer;
	BufferedReader in;
	PrintWriter out;

	ArrayBlockingQueue<PlayerConnection> players;
	ArrayBlockingQueue<ShipData> ships;
	ArrayBlockingQueue<Packet> sendToAllQueue;

	ArrayBlockingQueue<Packet> sendToPlayer;

	ArrayBlockingQueue<Packet> inPacketQueue;

	PacketParser parser;
	Database db;

	public ServerZone(int port, String name, Database db) {
		this.db = db;
		this.name = name;
		this.port = port;
		runThread = new Thread(this);
		senderThread = new SenderThread(this);
		players = new ArrayBlockingQueue<PlayerConnection>(1024);

		sendToAllQueue = new ArrayBlockingQueue<Packet>(1024);

		sendToPlayer = new ArrayBlockingQueue<Packet>(1024);

		parser = new PacketParser(this);

	}

	public void startServer() {

		try {

			Log.network("ServerZone:" + port + " started");

			zoneServer = new ServerSocket(port);
			isConnected = true;
			runThread.start();
			senderThread.start();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public ShipData getShipDataByID(int id) {

		ShipData data = null;

		for (ShipData ship : ships) {

			if (id == ship.getID()) {
				data = ship;
			}

		}

		return data;

	}

	public PlayerData getDataByID(int id) {

		PlayerData data = null;

		for (PlayerConnection pc : players) {

			if (pc.getPlayerData() != null) {
				if (id == pc.getPlayerData().getCharID()) {
					data = pc.getPlayerData();
				}
			}

		}

		return data;

	}

	public void loadShips() {

		ships = db.loadShips(port);

	}

	public boolean hasStateChanged() {
		return stateChange;
	}

	public void resetState() {
		stateChange = false;
	}

	public void sendToAll(Packet p) {

		sendToAllQueue.add(p);

	}

	public void sendToList(Packet p, ArrayList<PlayerConnection> players) {

		for (PlayerConnection player : players) {
			player.addToSendQueue(p);
		}

	}

	public ArrayBlockingQueue<PlayerConnection> getPlayers() {
		return players;
	}

	public PlayerConnection getPlayerByID(int id) {

		PlayerConnection player = null;
		for (PlayerConnection p : players) {

			if (p.getID() == id)
				player = p;

		}

		return player;

	}

	public Structure getStructureAt(float x, float y) {

		int quadrantX = ((int) x % (128 * 64)) / (128 * 64);
		int quadrantY = ((int) y % (128 * 64)) / (128 * 64);

		Structure struct = null;

		System.out.println("CHECKIGN STRUCTURE AT QUADRANT " + quadrantX + " , " + quadrantY);

		for (ShipData s : ships) {

			if (quadrantX == s.getQuadrantX() && quadrantY == s.getQuadrantY()) {
				struct = s;
			}

		}

		return struct;

	}

	public String getShipInfoUpdatePacket() {

		int shipCount = 0;
		StringBuilder sb = new StringBuilder();

		for (ShipData ship : ships) {
			shipCount++;
			sb.append(ship.getID() + "-" + ship.getName() + "-" + ship.getStructureClass() + "-" + ship.getQuadrantX()
					+ "-" + ship.getQuadrantY() + "-" + ship.getChecksum() + "=");

		}

		if (shipCount == 0) {
			return "EMPTY";
		}

		String data = sb.toString();

		return data.substring(0, data.length() - 1);

	}

	public String getPlayerUpdatePacket() {

		int playerCount = 0;

		StringBuilder sb = new StringBuilder();

		for (PlayerConnection player : players) {

			if (player.isReady() && player.isConnected()) {

				sb.append(player.getPlayerData().getCharID() + "," + player.getPlayerData().getName() + "=");

				playerCount++;
			}
		}

		if (playerCount == 0) {
			return "EMPTY";
		}

		String data = sb.toString();

		return data.substring(0, data.length() - 1);

	}

	public void sendAll() {

		while (!sendToAllQueue.isEmpty()) {

			Packet outPacket = null;
			try {
				outPacket = sendToAllQueue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (PlayerConnection p : players) {

				if (p.isConnected()) {
					p.send(outPacket);
					p.sendFromQueue();
				}
			}

		}

	}

	@Override
	public void run() {

		while (true) {

			try {
				Socket s = zoneServer.accept();
				Log.network("Connection received from " + s.getInetAddress());
				PlayerConnection p = new PlayerConnection(s, this, parser, db);
				p.startConnection();
				players.add(p);
				stateChange = true;

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}

class SenderThread extends Thread {

	ServerZone zone;

	public SenderThread(ServerZone zone) {
		this.zone = zone;
		zone.loadShips();
	}

	@Override
	public void run() {

		int counter = 0;

		while (true) {

			counter++;

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			String data = "";

			switch (counter) {

			case 200:

				data = zone.getShipInfoUpdatePacket();

				if (!data.equals("EMPTY")) {

					Packet updatePacket = new Packet(0, Packet.SHIP_INFO_PACKET, data);
					updatePacket.encode();
					zone.sendToAll(updatePacket);
					zone.resetState();
				}

			case 999:

				ShipData s = zone.getShipDataByID(100);
				PlayerData d = zone.getDataByID(998);

				if (d != null && s != null) {

					s.getTileAt(d.getX(), d.getY());

				}

				break;

			case 400:

				data = zone.getPlayerUpdatePacket();

				if (!data.equals("EMPTY")) {

					Packet updatePacket = new Packet(0, Packet.ZONE_PLAYER_INFO_PACKET, data);
					updatePacket.encode();
					zone.sendToAll(updatePacket);
					zone.resetState();
				}

			}

			if (counter >= 1000) {
				counter = 0;
			}

			zone.sendAll();

		}

	}
}