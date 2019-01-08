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

		switch (p.getType()) {

		case Packet.MOVE_PACKET:

			PlayerData d = zone.getDataByID(p.getPlayerID());
			if (d == null) {
				return;
			}

			boolean canMove = true;

			Vector2 mov = MathUtils.getVectorFromString(p.getData());

			Vector2 currentPos = new Vector2(d.getX(), d.getY());

			Vector2 adjustedMov = new Vector2(mov.x * Game.PLAYER_MOVE_SPEED, mov.y * Game.PLAYER_MOVE_SPEED);

			float velocity = (Math.abs(adjustedMov.x) + Math.abs(adjustedMov.y)) / 2;

			Vector2 destination = currentPos
					.add(new Vector2(mov.x * Game.PLAYER_MOVE_SPEED, mov.y * Game.PLAYER_MOVE_SPEED));

			if (canMove) {
				d.setX(destination.x);
				d.setY(destination.y);
				p.setData(MathUtils.getStringFromVector(destination) + "=" + adjustedMov.x + "=" + adjustedMov.y + "="
						+ velocity);

			}

			// TRY MOVE: IF SUCCESSFUL SEND TO ALL

			zone.sendToAll(p);

			break;

		case Packet.SHIP_LAYOUT_PACKET:

			PlayerConnection sConn = zone.getPlayerByID(p.getPlayerID());
			if (sConn != null) {

				int shipID = Integer.parseInt(p.getData());

				Ship s = zone.getShipDataByID(shipID);

				if (s != null) {

					sConn.send(new Packet(0, Packet.SHIP_LAYOUT_PACKET, p.getData() + "=" + s.getData()));

				}

			}

			break;

		case Packet.PLAYER_ID_PACKET:

			break;
		case Packet.LOCAL_CHAT_PACKET:

			PlayerConnection pConn = zone.getPlayerByID(p.getPlayerID());
			if (pConn == null) {
				Log.print("ERROR IN LOCAL CHAT PACKET");
				return;
			}

			ArrayList<PlayerConnection> sendList = new ArrayList<PlayerConnection>();

			for (PlayerConnection player : zone.getPlayers()) {

				PlayerData playerData = pConn.getPlayerData();

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
