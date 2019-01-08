package com.pineconeindustries.server;

import javax.swing.JFrame;

import com.pineconeindustries.server.database.Database;
import com.pineconeindustries.server.log.Log;

public class Server extends JFrame {

	public static String TEST_IP = "127.0.0.1";
	public static String DB_PATH = "data/data.db";

	Database db;

	ServerZone zone;

	public Server() {

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(300, 100);
		this.setTitle("Server");
		this.setVisible(true);

		init();
	}

	public static void main(String[] args) {

		if (args.length > 0) {

			TEST_IP = args[0];
			DB_PATH = args[1];

			Log.print("Parameters : { " + TEST_IP + " , " + DB_PATH + "}");
		}
		new Server();
	}

	public void init() {

		// 27400
		db = new Database(DB_PATH);
		Log.database("Connecting to Database at path " + DB_PATH);
		db.connect();

		for (int i = 27400; i < 27447; i++) {

			ServerZone z = new ServerZone(i, "Meh", db);
			z.startServer();

		}

	}

}