package com.pineconeindustries.server.data;

public class StructureTileData {

	int shipTileID, x, y;

	public StructureTileData(int x, int y, int shipTileID) {

		this.x = x;
		this.y = y;
		this.shipTileID = shipTileID;
	}

	public int getShipTileID() {
		return shipTileID;
	}

}
