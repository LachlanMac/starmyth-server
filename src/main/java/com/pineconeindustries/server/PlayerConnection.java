package com.pineconeindustries.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import com.pineconeindustries.server.data.PlayerData;
import com.pineconeindustries.server.data.Structure;
import com.pineconeindustries.server.database.Database;
import com.pineconeindustries.server.debug.Debug;
import com.pineconeindustries.server.log.Log;
import com.pineconeindustries.server.networking.Packet;
import com.pineconeindustries.server.networking.PacketParser;

public class PlayerConnection implements Runnable {

	private final int MAX_OUT_ERRORS = 80;
	private int outErrors = 0;

	private boolean tickrender = false;

	Socket client;

	BufferedReader in;
	PrintWriter out;
	Thread thread;
	ServerZone zone;

	PacketParser parser;

	PlayerData playerData;

	ArrayBlockingQueue<Packet> sendQueue;

	Database db;
	private boolean isConnected = false;
	private boolean isReady = false;

	private int playerID = 0;

	public PlayerConnection(Socket client, ServerZone zone, PacketParser parser, Database db) {

		this.db = db;
		this.zone = zone;
		this.parser = parser;
		thread = new Thread(this);

		this.client = client;
		sendQueue = new ArrayBlockingQueue<Packet>(128);
		try {

			client.setSoTimeout(10000);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream());

		} catch (IOException e) {
			System.out.println("WTF CUAT THIS ERROR??");
			disconnect(e.getMessage());
			e.printStackTrace();
		}

		Log.print("Player Connection Created");

	}

	public int getID() {
		return playerID;
	}

	public void addToSendQueue(Packet p) {
		sendQueue.add(p);
	}

	public void sendFromQueue() {

		while (!sendQueue.isEmpty()) {

			try {
				Packet p = sendQueue.take();

				if (p != null) {

					send(p);

				}

			} catch (InterruptedException e) {

				e.printStackTrace();
			}

		}

	}

	public void send(Packet p) {

		if (out.checkError()) {
			outErrors++;
			if (outErrors > MAX_OUT_ERRORS)
				disconnect("Too many send errors");
		}

		out.println(p.encode());
		out.flush();
	}

	public void disconnect(String reason) {

		db.savePlayer(getPlayerData());

		try {
			Log.network("Player (id: " + getPlayerData().getCharID() + " name:" + getPlayerData().getName()
					+ ") disconnected from the server  REASON: " + reason);
		} catch (NullPointerException e) {
			Log.network("Uninitiliazed player disconnected");
		}
		isConnected = false;
		client = null;
		thread.interrupt();

	}

	public void startConnection() {

		isConnected = true;
		thread.start();

	}

	public void savePlayer() {

	}

	public boolean isConnected() {
		return isConnected;
	}

	public boolean isReady() {
		return isReady;
	}

	public void setID(int id) {
		this.playerID = id;

	}

	public void forceSync() {

	}

	@Override
	public void run() {

		while (isConnected) {

			String data;

			try {
				while ((data = in.readLine()) != null) {

					Packet p = new Packet(data);
					if (p.decode()) {

						Log.LOG_NETWORK_ACTIVITY = true;
						Log.netTraffic(p.getData(), "In from Client");

						if (p.getType() != Packet.HEARTBEAT_PACKET) {

							if (isReady != true) {

								if (p.getPlayerID() == Debug.TEST_CLIENT_1_ID) {
									Debug.TEST_CLIENT_1 = true;

								}

								setID(p.getPlayerID());

								playerData = db.loadPlayer(playerID);

								if (playerData != null) {

									Structure s = zone.getStructureAt(playerData.getX(), playerData.getY());
									if (s != null) {
										playerData.setStructure(
												zone.getStructureAt(playerData.getX(), playerData.getY()));
									}
									isReady = true;
								}
							} else {

								parser.parse(p);

							}

						} else {

						}

					} else {
						Log.netTraffic("Cannot Decode Packet", "Packet Parse Error");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				disconnect(e.getMessage());

			}

		}

	}

	public PlayerData getPlayerData() {
		return playerData;
	}

}
