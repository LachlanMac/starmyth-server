package com.pineconeindustries.server;

import com.pineconeindustries.server.database.Database;

public class Shutdown extends Thread {

	Server s;
	Database db;

	public Shutdown(Server s, Database db) {

		this.s = s;
		this.db = db;

	}

	@Override
	public void run() {

	}

}
