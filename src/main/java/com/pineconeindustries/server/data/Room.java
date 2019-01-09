package com.pineconeindustries.server.data;

import java.util.ArrayList;

public class Room {

	private int roomID, startX, startY, roomWidth, roomHeight;

	ArrayList<Door> doors;

	enum roomType {
		none, engineering, bridge, cafe, shop, quarters;
	}

	private roomType type;

	private String roomName = "Default Room";

	public Room(int roomID, int startX, int startY, int roomWidth, int roomHeight) {
		this.roomID = roomID;
		this.startX = startX;
		this.startY = startY;
		this.roomWidth = roomWidth;
		this.roomHeight = roomHeight;
		type = roomType.none;
		doors = new ArrayList<Door>();

	}

	public void addDoor(Door d) {
		doors.add(d);

	}

	public ArrayList<Door> getDoors() {
		return doors;
	}

	public roomType getRoomType() {
		return type;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomType(String typeS) {

		if (typeS.equals("bridge")) {
			this.type = roomType.bridge;
		} else if (typeS.equals("engineering")) {
			this.type = roomType.engineering;
		} else if (typeS.equals("cafe")) {
			this.type = roomType.cafe;
		} else if (typeS.equals("shop")) {
			this.type = roomType.shop;
		} else if (typeS.equals("quarters")) {
			this.type = roomType.quarters;
		} else {
			this.type = roomType.none;
		}

	}

	public String getRoomTypeData() {

		String roomType = "";

		switch (this.type) {

		case bridge:
			roomType = "bridge";
			break;
		case engineering:
			roomType = "engineering";
			break;

		case cafe:
			roomType = "cafe";
			break;

		case shop:
			roomType = "shop";
			break;

		case quarters:
			roomType = "quarters";
			break;

		case none:
			roomType = "none";
			break;

		default:
			roomType = "none";
			break;

		}

		return roomType;

	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public int getRoomID() {
		return roomID;
	}

	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public int getRoomWidth() {
		return roomWidth;
	}

	public void setRoomWidth(int roomWidth) {
		this.roomWidth = roomWidth;
	}

	public int getRoomHeight() {
		return roomHeight;
	}

	public void setRoomHeight(int roomHeight) {
		this.roomHeight = roomHeight;
	}

	public roomType getType() {
		return type;
	}

	public void setType(roomType type) {
		this.type = type;
	}

	public void setDoors(ArrayList<Door> doors) {
		this.doors = doors;
	}

}
