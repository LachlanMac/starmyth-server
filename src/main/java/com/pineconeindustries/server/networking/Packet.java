package com.pineconeindustries.server.networking;

public class Packet {

	private String data, msg;
	private int type, playerID;

	public static final int MOVE_PACKET = 100;
	public static final int ZONE_PLAYER_INFO_PACKET = 505;
	public static final int PLAYER_ID_PACKET = 5;
	public static final int HEARTBEAT_PACKET = 2;
	public static final int LOCAL_CHAT_PACKET = 1001;
	public static final int ADMIN_PACKET = 4000;
	public static final int SHIP_INFO_PACKET = 3000;
	public static final int SHIP_LAYOUT_PACKET = 3001;
	public static final int SHIP_DATA_PACKET = 3002;
	public static final int SHIP_ROOM_INFO_PACKET = 3005;

	public static final int STATION_INFO_PACKET = 3100;
	public static final int STATION_LAYOUT_PACKET = 3101;
	public static final int STATION_DATA_PACKET = 3102;
	public static final int STATION_ROOM_INFO_PACKET = 3105;

	public static final int NPC_INFO_PACKET = 5000;

	public Packet(int playerID, int type, String data) {
		this.data = data;
		this.type = type;
		this.playerID = playerID;
	}

	public Packet(String msg) {
		this.msg = msg;
	}

	public String encode() {

		return playerID + ":" + type + ":" + data;

	}

	public boolean decode() {

		boolean goodpacket = true;

		String[] dataSplit = msg.split(":");
		try {
			this.playerID = Integer.parseInt(dataSplit[0].trim());
			this.type = Integer.parseInt(dataSplit[1].trim());
		} catch (NumberFormatException e) {
			goodpacket = false;
		}

		data = dataSplit[2];

		return goodpacket;

	}

	public int getPlayerID() {
		return playerID;
	}

	public String getData() {
		return data;
	}

	public int getType() {
		return type;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {

		return "Packet -> " + playerID + ":" + data + ":" + type;

	}

}
