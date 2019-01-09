package com.pineconeindustries.server.networking;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import com.pineconeindustries.server.PlayerConnection;
import com.pineconeindustries.server.ServerZone;
import com.pineconeindustries.server.config.Game;
import com.pineconeindustries.server.data.PlayerData;
import com.pineconeindustries.server.data.Ship;
import com.pineconeindustries.server.log.Log;
import com.pineconeindustries.server.utils.MathUtils;
import com.pineconeindustries.server.utils.Vector2;

public class PacketParser {

	ServerZone zone;

	public PacketParser(ServerZone zone) {
		this.zone = zone;
	}

	public void parse(Packet p) {

		PlayerConnection conn = null;
		PlayerData playerData = null;
		boolean canMove = true;
		Vector2 moveVector = null;
		Vector2 adjustedMov = null;
		Vector2 destination = null;
		Vector2 currentPosition = null;
		float velocity = 0;
		int shipID = 0;
		Ship ship = null;

		switch (p.getType()) {

		case Packet.MOVE_PACKET:

			playerData = zone.getDataByID(p.getPlayerID());
			if (playerData == null) {
				return;
			}

			moveVector = MathUtils.getVectorFromString(p.getData());

			currentPosition = new Vector2(playerData.getX(), playerData.getY());

			adjustedMov = new Vector2(moveVector.x * Game.PLAYER_MOVE_SPEED, moveVector.y * Game.PLAYER_MOVE_SPEED);

			velocity = (Math.abs(adjustedMov.x) + Math.abs(adjustedMov.y)) / 2;

			destination = currentPosition
					.add(new Vector2(moveVector.x * Game.PLAYER_MOVE_SPEED, moveVector.y * Game.PLAYER_MOVE_SPEED));

			canMove = playerData.getStructure().canMoveToPoint(playerData.getPlayerCenter(), destination, moveVector);

			if (canMove) {
				playerData.setX(destination.x);
				playerData.setY(destination.y);

				p.setData(MathUtils.getStringFromVector(destination) + "=" + adjustedMov.x + "=" + adjustedMov.y + "="
						+ velocity);

			} else {

				p.setData(MathUtils.getStringFromVector(currentPosition) + "=" + adjustedMov.x + "=" + adjustedMov.y
						+ "=" + velocity);

			}
			// TRY MOVE: IF SUCCESSFUL SEND TO ALL

			zone.sendToAll(p);

			break;

		case Packet.SHIP_LAYOUT_PACKET:

			conn = zone.getPlayerByID(p.getPlayerID());
			if (conn != null) {
				shipID = Integer.parseInt(p.getData());
				ship = zone.getShipDataByID(shipID);
				if (ship != null) {
					conn.send(new Packet(0, Packet.SHIP_LAYOUT_PACKET, p.getData() + "=" + ship.getData()));
				}
			}

			break;

		case Packet.SHIP_ROOM_INFO_PACKET:

			conn = zone.getPlayerByID(p.getPlayerID());
			shipID = Integer.parseInt(p.getData());
			ship = zone.getShipDataByID(shipID);

			if (ship != null) {
				conn.send(new Packet(0, Packet.SHIP_ROOM_INFO_PACKET, p.getData() + "=" + ship.getRoomData()));
			}

			break;

		case Packet.PLAYER_ID_PACKET:

			break;
		case Packet.LOCAL_CHAT_PACKET:

			conn = zone.getPlayerByID(p.getPlayerID());
			if (conn == null) {
				Log.print("ERROR IN LOCAL CHAT PACKET");
				return;
			}

			ArrayList<PlayerConnection> sendList = new ArrayList<PlayerConnection>();

			for (PlayerConnection player : zone.getPlayers()) {

				playerData = conn.getPlayerData();

				if (playerData != null) {

					if (playerData.getLocation().dist(player.getPlayerData().getLocation()) < 700) {
						sendList.add(player);
					}

				}

			}

			zone.sendToList(p, sendList);

			break;

		case Packet.ADMIN_PACKET:

			String[] adminSplit = p.getData().split("\\s+");

			try {

				if (adminSplit.length == 3) {

					float x = Float.parseFloat(adminSplit[1]);
					float y = Float.parseFloat(adminSplit[2]);

					if (adminSplit[0].toLowerCase().equals("!tp".toLowerCase())) {

						PlayerConnection pc = zone.getPlayerByID(p.getPlayerID());

						pc.getPlayerData().setX(x);
						pc.getPlayerData().setY(y);

					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		}

	}

}
