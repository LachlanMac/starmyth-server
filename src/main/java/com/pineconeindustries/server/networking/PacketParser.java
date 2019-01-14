package com.pineconeindustries.server.networking;

import java.util.ArrayList;
import com.pineconeindustries.server.PlayerConnection;
import com.pineconeindustries.server.ServerZone;
import com.pineconeindustries.server.data.PlayerData;
import com.pineconeindustries.server.data.Ship;
import com.pineconeindustries.server.data.Station;
import com.pineconeindustries.server.data.Structure;
import com.pineconeindustries.server.log.Log;
import com.pineconeindustries.server.utils.MathUtils;
import com.pineconeindustries.server.utils.Units;
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
		int stationID = 0;
		Ship ship = null;
		Station station = null;

		switch (p.getType()) {

		case Packet.MOVE_PACKET:

			playerData = zone.getDataByID(p.getPlayerID());
			if (playerData == null) {
				return;
			}

			moveVector = MathUtils.getVectorFromString(p.getData());

			currentPosition = new Vector2(playerData.getX(), playerData.getY());

			boolean structureChange = playerData.structureChanged();

			if (structureChange) {

				Structure tmpStruct = zone.getStructureAt(playerData.getLocation().x, playerData.getLocation().y);

				if (tmpStruct != null) {
					playerData.setStructure(tmpStruct);
				} else {
					playerData.setStructure(null);
				}

			}

			adjustedMov = new Vector2(moveVector.x * Units.PLAYER_MOVE_SPEED, moveVector.y * Units.PLAYER_MOVE_SPEED);

			velocity = (Math.abs(adjustedMov.x) + Math.abs(adjustedMov.y)) / 2;

			destination = currentPosition
					.add(new Vector2(moveVector.x * Units.PLAYER_MOVE_SPEED, moveVector.y * Units.PLAYER_MOVE_SPEED));

			int structureID = 0;

			if (playerData.getStructure() == null) {
				canMove = true;
				structureID = 0;

			} else {
				canMove = playerData.getStructure().canMoveToPoint(playerData.getPlayerCenter(), destination,
						moveVector);

				structureID = playerData.getStructure().getID();
			}

			if (canMove) {
				playerData.setX(destination.x);
				playerData.setY(destination.y);

				if (playerData.getStructure() != null) {
					if (playerData.getStructure().getTileAt(destination.x, destination.y).getShipTileID() == 0) {
						velocity = 999;
					}
				}
				p.setData(MathUtils.getStringFromVector(destination) + "=" + adjustedMov.x + "=" + adjustedMov.y + "="
						+ velocity + "=" + structureID);

			} else {

				p.setData(MathUtils.getStringFromVector(currentPosition) + "=" + adjustedMov.x + "=" + adjustedMov.y
						+ "=" + velocity + "=" + structureID);

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

		case Packet.STATION_LAYOUT_PACKET:

			conn = zone.getPlayerByID(p.getPlayerID());
			if (conn != null) {
				stationID = Integer.parseInt(p.getData());
				station = zone.getStationDataByID(stationID);
				if (station != null) {
					conn.send(new Packet(0, Packet.STATION_LAYOUT_PACKET, p.getData() + "=" + station.getData()));
				}
			}

			break;

		case Packet.STATION_ROOM_INFO_PACKET:

			conn = zone.getPlayerByID(p.getPlayerID());
			stationID = Integer.parseInt(p.getData());
			station = zone.getStationDataByID(stationID);

			if (station != null) {
				conn.send(new Packet(0, Packet.STATION_ROOM_INFO_PACKET, p.getData() + "=" + station.getRoomData()));
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
