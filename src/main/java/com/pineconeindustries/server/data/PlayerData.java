package com.pineconeindustries.server.data;

import com.pineconeindustries.server.log.Log;
import com.pineconeindustries.server.utils.Vector2;

public class PlayerData {

	private String name, model;
	private int charID, sector;
	private float x, y;

	Structure currentStructure;

	int localX, localY;

	public PlayerData(String name, int charID, int sector, String model, float x, float y) {

		this.name = name;
		this.charID = charID;
		this.sector = sector;
		this.model = model;
		this.x = x;
		this.y = y;

		this.localX = (int) x / 8192;
		this.localY = (int) y / 8192;

	}

	public boolean structureChanged() {

		int tmpX = (int) x / 8192;
		int tmpY = (int) y / 8192;

		if (localX != tmpX || localY != tmpY) {
			localX = tmpX;
			localY = tmpY;
			return true;
		} else {
			return false;
		}

	}

	public Vector2 getPlayerCenter() {
		return new Vector2(x + 32, y - 32);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCharID() {
		return charID;
	}

	public void setCharID(int charID) {
		this.charID = charID;
	}

	public int getSector() {
		return sector;
	}

	public void setSector(int sector) {
		this.sector = sector;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		localX = (int) x / 8192;
		this.x = x;

	}

	public float getY() {

		return y;

	}

	public void setY(float y) {

		localX = (int) y / 8192;
		this.y = y;

	}

	public Vector2 getLocation() {

		return new Vector2(x, y);
	}

	public void setStructure(Structure s) {
		Log.print("Structure set to " + s.getName());
		this.currentStructure = s;
	}

	public Structure getStructure() {
		return currentStructure;
	}

}
