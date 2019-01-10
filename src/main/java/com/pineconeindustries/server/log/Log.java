package com.pineconeindustries.server.log;

import com.pineconeindustries.server.config.Settings;

public class Log {

	public static boolean DEBUG_MODE = true;
	public static boolean LOG_NET_TRAFFIC = false;
	public static boolean LOG_NETWORK_ACTIVITY = true;
	public static boolean LOG_DATABASE_ACTIVITY = true;

	public static void print(String out) {
		if (!Settings.LOGGING) {
			return;
		}
		System.out.println("Console: [" + out + "]         (" + getStackTrace() + ")");
	}

	public static void debug(String out) {
		if (!Settings.LOGGING) {
			return;
		}

		if (DEBUG_MODE)
			System.out.println("Debug : [" + out + "] " + getStackTrace());
	}

	public static void netTraffic(String out, String type) {
		if (!Settings.LOGGING) {
			return;
		}
		if (LOG_NET_TRAFFIC)
			System.out.println("NET(" + type + ") ==== [" + out + "]");

	}

	public static void network(String out) {
		if (!Settings.LOGGING) {
			return;
		}
		if (LOG_NETWORK_ACTIVITY)
			System.out.println("SERVER: [" + out + "]");
	}

	public static void database(String out) {
		if (!Settings.LOGGING) {
			return;
		}
		if (LOG_DATABASE_ACTIVITY)
			System.out.println("DATABASE: [" + out + "]");
	}

	public static String getStackTrace() {
		return Thread.currentThread().getStackTrace()[3].toString();
	}

}
