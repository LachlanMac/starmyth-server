package com.pineconeindustries.server.data;

public class Door {

	private int x, y, status;
	Room r;

	public Door(Room r, int x, int y, int status) {
		this.r = r;
		this.x = x;
		this.y = y;
		this.status = status;
	}

}
