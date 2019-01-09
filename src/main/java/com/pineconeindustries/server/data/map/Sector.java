package com.pineconeindustries.server.data.map;

import com.pineconeindustries.server.ServerZone;
import com.pineconeindustries.server.data.factions.Faction;

public class Sector {

	private int sectorID, galacticX, galacticY, factionID;
	private String sectorName;
	Faction faction;
	ServerZone zone;

	public Sector(int sectorID, String sectorName, int galacticX, int galacticY, int factionID) {

		this.sectorID = sectorID;
		this.sectorName = sectorName;
		this.galacticX = galacticX;
		this.galacticY = galacticY;
		this.factionID = factionID;

	}

	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}

	public void setZone(ServerZone zone) {
		this.zone = zone;
	}

	public ServerZone getZone() {
		return zone;
	}

	public int getSectorID() {
		return sectorID;
	}

	public void setSectorID(int sectorID) {
		this.sectorID = sectorID;
	}

	public int getGalacticX() {
		return galacticX;
	}

	public void setGalacticX(int galacticX) {
		this.galacticX = galacticX;
	}

	public int getGalacticY() {
		return galacticY;
	}

	public void setGalacticY(int galacticY) {
		this.galacticY = galacticY;
	}

	public int getFactionID() {
		return factionID;
	}

	public void setFactionID(int factionID) {
		this.factionID = factionID;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

}
