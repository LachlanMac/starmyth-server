package com.pineconeindustries.server.log;

public class Log {

	public static boolean DEBUG_MODE = false;
	public static boolean LOG_NET_TRAFFIC = false;
	public static boolean LOG_NETWORK_ACTIVITY = true;
	public static boolean LOG_DATABASE_ACTIVITY = true;

	public static void print(String out) {
		System.out.println("Console: [" + out + "]");
	}

	public static void debug(String out) {
		if (DEBUG_MODE)
			System.out.println("Debug : [" + out + "]");
	}

	public static void netTraffic(String out, String type) {
		if (LOG_NET_TRAFFIC)
			System.out.println("NET(" + type + ") ==== [" + out + "]");

	}

	public static void network(String out) {
		if (LOG_NETWORK_ACTIVITY)
			System.out.println("SERVER: [" + out + "]");
	}

	public static void database(String out) {
		if (LOG_DATABASE_ACTIVITY)
			System.out.println("DATABASE: [" + out + "]");
	}
	
	public static int getLineNumber() {
	    return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}

}
