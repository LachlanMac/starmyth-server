package com.pineconeindustries.server.data;

import com.pineconeindustries.server.utils.Vector2;

public class PlayerData {

	private String name;
	private int charID, sector, model;
	private float x, y;

	Structure currentStructure;

	public PlayerData(String name, int charID, int sector, int model, float x, float y) {

		this.name = name;

		this.charID = charID;
		this.sector = sector;
		this.model = model;
		this.x = x;
		this.y = y;
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

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Vector2 getLocation() {
		return new Vector2(x, y);
	}

	public void setStructure(Structure s) {
		System.out.println("Structure set to " + s.getName());
		this.currentStructure = s;
	}
}
