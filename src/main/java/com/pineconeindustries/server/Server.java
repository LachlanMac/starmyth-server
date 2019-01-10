package com.pineconeindustries.server;

import com.pineconeindustries.server.config.Settings;
import com.pineconeindustries.server.data.global.Galaxy;
import com.pineconeindustries.server.data.map.Sector;
import com.pineconeindustries.server.database.Database;
import com.pineconeindustries.server.log.Log;

/**
 * Server.java - Server Launcher class that starts the Starmyth Application
 * Server
 * 
 * @author Lachlan R McCallum
 * @version 0.01.011
 */

public class Server {

	Database db;
	ServerZone zone;
	Galaxy galaxy;

	// Constructor : Server
	public Server() {

		init();
	}

	// Main Method
	public static void main(String[] args) {
		// Read Server Configuration Settings
		ConfigReader.readServerSettings();
		if (args.length > 0) {

			if (args[0].equals("local")) {
				Settings.LOCAL = true;
			}
		}
		new Server();
	}

	public void init() {
		// create Galaxy master object for server-wide variables
		galaxy = new Galaxy();
		// Create Database connection
		db = new Database(Settings.DATABASE_PATH);

		Log.database("Connecting to Database at path " + Settings.DATABASE_PATH);

		db.connect();
		// loads server wide faction state data from the database
		galaxy.setFactions(db.loadFactions());
		// loads all the sector dated needed to start the individual server zones
		galaxy.setSectors(db.loadSectors());

		// create and start the servers
		for (Sector s : galaxy.getSectors()) {

			ServerZone zone = new ServerZone(s, db, galaxy);
			zone.startServer();

		}

	}

}