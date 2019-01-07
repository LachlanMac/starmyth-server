package com.pineconeindustries.server.utils;

public class Vector2 {

	public float x, y;

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2 add(Vector2 v) {

		return new Vector2(x + v.x, y + v.y);

	}

	public float dist(Vector2 v) {

		float xDiff = v.x - x;
		float yDiff = v.y - y;

		return (float) Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));

	}

}
