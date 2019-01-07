package com.pineconeindustries.server.utils;

public class MathUtils {

	public static Vector2 getVectorFromString(String vec) {

		String[] xy = vec.trim().split("=");

		if (xy.length != 2) {
			System.out.println("MATH PARSE ERROR");
			return null;
		}

		float x = Float.parseFloat(xy[0]);
		float y = Float.parseFloat(xy[1]);

		return new Vector2(x, y);

	}

	public static String getStringFromVector(Vector2 vec) {

		return vec.x + "=" + vec.y;

	}
}
