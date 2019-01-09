package com.pineconeindustries.server.data.global;

import java.util.concurrent.ArrayBlockingQueue;

import com.pineconeindustries.server.data.factions.Faction;
import com.pineconeindustries.server.data.map.Sector;

public class Galaxy {

	ArrayBlockingQueue<Faction> factions;
	ArrayBlockingQueue<Sector> sectors;

	public Galaxy() {

	}

	public void setFactions(ArrayBlockingQueue<Faction> factions) {
		this.factions = factions;
	}

	public void setSectors(ArrayBlockingQueue<Sector> sectors) {
		this.sectors = sectors;

		for (Sector s : sectors) {

			s.setFaction(getFactionByID(s.getFactionID()));

		}

	}

	public Sector getSectorByID(int id) {
		Sector sector = null;

		for (Sector s : sectors) {

			if (s.getSectorID() == id) {
				sector = s;
				break;
			}

		}

		return sector;
	}

	public Faction getFactionByID(int id) {
		Faction faction = null;

		for (Faction f : factions) {

			if (f.getId() == id) {
				faction = f;
				break;
			}
		}

		return faction;

	}

	public ArrayBlockingQueue<Sector> getSectors() {
		return sectors;
	}

	public ArrayBlockingQueue<Faction> getFactions() {
		return factions;
	}

}
