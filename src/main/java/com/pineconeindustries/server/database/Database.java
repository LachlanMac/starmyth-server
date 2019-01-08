package com.pineconeindustries.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import com.pineconeindustries.server.data.PlayerData;
import com.pineconeindustries.server.data.Ship;
import com.pineconeindustries.server.log.Log;
import com.pineconeindustries.server.npcs.NPC;
import com.pineconeindustries.server.utils.Vector2;

public class Database {

	String path;

	Connection conn;

	public Database(String path) {

		this.path = path;

	}

	public void connect() {
		conn = null;
		try {
			// db parameters
			String url = "jdbc:sqlite:" + path;
			// create a connection to the database
			conn = DriverManager.getConnection(url);

		} catch (SQLException e) {
			Log.database("Failed to connect to database: " + e.getMessage());
			System.exit(0);
		}
	}

	public boolean savePlayer(PlayerData d) {

		boolean success = true;

		String sql = "UPDATE characters SET x_pos = ?, y_pos = ?, sector_id = ? WHERE character_id = ?";

		PreparedStatement stmt;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setFloat(1, d.getX());
			stmt.setFloat(2, d.getY());
			stmt.setInt(3, d.getSector());
			stmt.setInt(4, d.getCharID());

			stmt.executeUpdate();

			Log.database("Player " + d.getName() + " saved successfully");

		} catch (SQLException e) {
			success = false;
			e.printStackTrace();
		}

		return success;

	}

	public PlayerData loadPlayer(int charID) {

		String sql = "SELECT character_name, character_model, x_pos, y_pos, sector_id FROM characters WHERE character_id = ?";
		PlayerData data = null;

		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, charID);
			ResultSet rs = stmt.executeQuery();

			// loop through the result set
			while (rs.next()) {

				String charName = rs.getString("character_name");
				int charModel = rs.getInt("character_model");
				float x = rs.getFloat("x_pos");
				float y = rs.getFloat("y_pos");
				int sID = rs.getInt("sector_id");

				data = new PlayerData(charName, charID, sID, charModel, x, y);

				Log.database("Loaded Player (id :" + charID + " name:" + charName);
				return data;

			}
		} catch (SQLException e) {
			Log.database("Error Loading Player From Database");
			e.printStackTrace();

		}

		if (data == null) {
			Log.database("Could Not Find Player With CHARID = " + charID);
		}

		return data;

	}

	public Connection getConnection() {
		return conn;

	}

	public ArrayBlockingQueue<NPC> loadNPCs(int sector) {
		Log.print("Loading npcs for sector : " + sector);
		ArrayBlockingQueue<NPC> npcList = new ArrayBlockingQueue<NPC>(1024);
		String sql = "SELECT npc_id, npc_name, x_pos, y_pos, faction_id FROM npcs WHERE sector_id = ?";
		PreparedStatement stmt;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, sector);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				int npcID = rs.getInt("npc_id");
				String npcName = rs.getString("npc_name");
				float x = rs.getFloat("x_pos");
				float y = rs.getFloat("y_pos");
				int faction_id = rs.getInt("faction_id");

				Log.database("Loaded NPC (id :" + npcName + " name:" + npcName + " in sector " + sector);

				NPC npc = new NPC(npcID, npcName, new Vector2(x, y), faction_id, sector);

				npcList.add(npc);

			}

		} catch (SQLException e) {
			Log.database("Error Loading NPCs From Database");
			e.printStackTrace();
		}

		return npcList;
	}

	public ArrayBlockingQueue<Ship> loadShips(int sector) {

		Log.print("Loading ships for sector : " + sector);
		String sql = "SELECT ship_id, ship_name, ship_class, x_pos, y_pos, local_x_pos, local_y_pos, file_path FROM ships WHERE sector_id = ?";
		ArrayBlockingQueue<Ship> ships = new ArrayBlockingQueue<Ship>(128);
		PreparedStatement stmt;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, sector);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				int shipID = rs.getInt("ship_id");
				String shipName = rs.getString("ship_name");
				String shipClass = rs.getString("ship_class");
				float x = rs.getFloat("x_pos");
				float y = rs.getFloat("y_pos");
				int localX = rs.getInt("local_x_pos");
				int localY = rs.getInt("local_y_pos");
				String path = rs.getString("file_path");

				Log.database("Loaded Ship (id :" + shipID + " name:" + shipName + " in sector " + sector);

				Ship ship = new Ship(shipID, sector, shipName, shipClass, x, y, localX, localY, 64, 64, path);

				ships.add(ship);

			}

		} catch (SQLException e) {
			Log.database("Error Loading Ship From Database");
			e.printStackTrace();
		}

		return ships;

	}

}
