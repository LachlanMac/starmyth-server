package com.pineconeindustries.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import com.pineconeindustries.server.data.PlayerData;
import com.pineconeindustries.server.data.Ship;
import com.pineconeindustries.server.data.Station;
import com.pineconeindustries.server.data.Structure;
import com.pineconeindustries.server.data.global.Galaxy;
import com.pineconeindustries.server.data.map.Sector;
import com.pineconeindustries.server.database.Database;
import com.pineconeindustries.server.log.Log;
import com.pineconeindustries.server.networking.Packet;
import com.pineconeindustries.server.networking.PacketParser;
import com.pineconeindustries.server.npcs.NPC;
import com.pineconeindustries.server.utils.MathUtils;

/**
 * ServerZone.java - Class that contains the logic for network transmission of
 * data to and from clients. Implements Runnable thread for listening and
 * sending packets via the TCP Protocol.
 */

public class ServerZone implements Runnable {

	// the port this subserver is runnign on
	private int port;
	// name of the sector
	private String name;
	// Logic Threads
	Thread runThread;
	SenderThread senderThread;
	// Socket
	ServerSocket zoneServer;
	// IO
	BufferedReader in;
	PrintWriter out;
	// Threadsafe Queues
	ArrayBlockingQueue<PlayerConnection> players;
	ArrayBlockingQueue<Ship> ships;
	ArrayBlockingQueue<Station> stations;
	ArrayBlockingQueue<NPC> npcs;
	ArrayBlockingQueue<Packet> sendToAllQueue;
	ArrayBlockingQueue<Packet> sendToPlayer;
	ArrayBlockingQueue<Packet> inPacketQueue;
	// Handles parsing packets coming from clients
	PacketParser parser;
	// REFS
	Database db;
	Galaxy galaxy;
	Sector sector;

	public ServerZone(Sector sector, Database db, Galaxy galaxy) {
		this.db = db;
		this.galaxy = galaxy;
		this.sector = sector;
		this.name = sector.getSectorName();
		this.port = sector.getSectorID();

		// declare thread logic
		runThread = new Thread(this);
		senderThread = new SenderThread(this);

		// Initialize queues
		players = new ArrayBlockingQueue<PlayerConnection>(1024);
		sendToAllQueue = new ArrayBlockingQueue<Packet>(2048);
		sendToPlayer = new ArrayBlockingQueue<Packet>(1024);

		// Initialize parser
		parser = new PacketParser(this);

	}

	public void startServer() {

		try {
			Log.network("ServerZone:" + port + " started");

			zoneServer = new ServerSocket(port);
			runThread.start();
			senderThread.start();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Ship getShipDataByID(int id) {

		Ship data = null;

		for (Ship ship : ships) {

			if (id == ship.getID()) {
				data = ship;
			}

		}

		return data;

	}

	public Station getStationDataByID(int id) {

		Station data = null;

		for (Station station : stations) {

			if (id == station.getID()) {
				data = station;
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

	public void loadNPCs() {

		npcs = db.loadNPCs(port);

		for (NPC n : npcs) {

			n.setStructure(getStructureAt(n.getLoc().x, n.getLoc().y));

		}

	}

	public void loadShips() {

		ships = db.loadShips(port);

	}

	public void loadStations() {
		stations = db.loadStations(port);
	}

	public void loadRooms() {

		for (Ship s : ships) {

			db.loadRoomDataForShips(s);

		}

		for (Station s : stations) {
			db.loadRoomDataForStations(s);

		}

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

		System.out.println(quadrantX + " , " + quadrantY + "  SOMEHOW FROM : " + x + " , " + y);

		for (Ship s : ships) {

			if (quadrantX == s.getQuadrantX() && quadrantY == s.getQuadrantY()) {
				System.out.println("FOUND SHIP");
				struct = s;
			}

		}

		for (Station s : stations) {

			if (quadrantX == s.getQuadrantX() && quadrantY == s.getQuadrantY()) {
				System.out.println("FOUND STATION");
				struct = s;
			}
		}

		return struct;

	}

	public String getShipInfoUpdatePacket() {

		int shipCount = 0;
		StringBuilder sb = new StringBuilder();

		for (Ship ship : ships) {
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

	public String getStationInfoUpdatePacket() {

		int stationCount = 0;
		StringBuilder sb = new StringBuilder();

		for (Station station : stations) {
			stationCount++;
			sb.append(station.getID() + "-" + station.getName() + "-" + station.getStructureClass() + "-"
					+ station.getQuadrantX() + "-" + station.getQuadrantY() + "-" + station.getChecksum() + "=");

		}

		if (stationCount == 0) {
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

	public void queueNPCInfo() {

		for (NPC npc : npcs) {

			String data = npc.getId() + "=" + npc.getName() + "=" + npc.getFactionID() + "="
					+ MathUtils.getStringFromVector(npc.getLoc()) + "=" + npc.getStructure().getID();
			Packet p = new Packet(0, Packet.NPC_INFO_PACKET, data);
			p.encode();

			sendToAllQueue.add(p);

		}

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
		zone.loadStations();
		zone.loadNPCs();
		zone.loadRooms();
	}

	@Override
	public void run() {

		int counter = 0;

		while (true) {

			try {
				Thread.sleep(5);
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

				}
				break;

			case 300:

				data = zone.getStationInfoUpdatePacket();

				if (!data.equals("EMPTY")) {

					Packet updatePacket = new Packet(0, Packet.STATION_INFO_PACKET, data);
					updatePacket.encode();
					zone.sendToAll(updatePacket);

				}

				break;

			case 400:

				data = zone.getPlayerUpdatePacket();

				if (!data.equals("EMPTY")) {

					Packet updatePacket = new Packet(0, Packet.ZONE_PLAYER_INFO_PACKET, data);
					updatePacket.encode();
					zone.sendToAll(updatePacket);

				}

				break;

			default:
				break;
			}

			if (counter >= 1000) {
				counter = 0;
			}

			zone.queueNPCInfo();

			// zone.queueRoomInfo();

			zone.sendAll();

			counter++;
		}

	}
}
