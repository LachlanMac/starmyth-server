package com.pineconeindustries.server.data;

import java.awt.Rectangle;

public class StructureTileData {

	private int shipTileID, x, y;

	private boolean walkable;

	Rectangle bounds;
	Structure struct;

	public StructureTileData(int x, int y, int shipTileID, boolean walkable, Structure struct) {

		this.x = x;
		this.y = y;
		this.shipTileID = shipTileID;
		this.walkable = walkable;
		this.struct = struct;

	}

	public Rectangle getBounds() {

		return new Rectangle();
	}

	public int getShipTileID() {
		return shipTileID;
	}

	public boolean isWalkable() {
		return walkable;
	}

}
