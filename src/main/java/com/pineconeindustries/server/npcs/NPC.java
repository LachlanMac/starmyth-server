package com.pineconeindustries.server.npcs;

import com.pineconeindustries.server.data.Structure;
import com.pineconeindustries.server.utils.Vector2;

public class NPC {
	private int id, factionID, sectorID;
	private String name;
	private Vector2 loc;

	Structure currentStructure;

	public NPC(int id, String name, Vector2 loc, int factionID, int sectorID) {
		this.id = id;
		this.name = name;
		this.loc = loc;
		this.factionID = factionID;
		this.sectorID = sectorID;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFactionID() {
		return factionID;
	}

	public void setFactionID(int factionID) {
		this.factionID = factionID;
	}

	public int getSectorID() {
		return sectorID;
	}

	public void setSectorID(int sectorID) {
		this.sectorID = sectorID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector2 getLoc() {
		return loc;
	}

	public void setLoc(Vector2 loc) {
		this.loc = loc;
	}

	public void setStructure(Structure structure) {
		if (structure == null) {
			System.out.println("EMPTY STRUCTURE!!!");
		}

		this.currentStructure = structure;
	}

	public Structure getStructure() {
		return currentStructure;
	}

}
