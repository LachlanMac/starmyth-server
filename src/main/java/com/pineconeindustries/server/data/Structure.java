package com.pineconeindustries.server.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Structure {

	private int quadrantX, quadrantY, tileWidth, tileHeight, checksum, sectorID, id;
	private float x, y;
	private String structureClass, filePath, name, data;
	StructureTileData[][] layout;

	public Structure(int id, int sectorID, String name, String structureClass, float x, float y, int quadrantX,
			int quadrantY, int tileWidth, int tileHeight, String filePath) {
		this.id = id;
		this.sectorID = sectorID;
		this.name = name;
		this.structureClass = structureClass;
		this.x = x;
		this.y = y;
		this.quadrantX = quadrantX;
		this.quadrantY = quadrantY;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.filePath = filePath;
		layout = new StructureTileData[tileWidth][tileHeight];

		loadShipLayout();

		parseTileData();

	}

	public StructureTileData getTileAt(float x, float y) {

		float intoLocalX = x % (128 * 64);
		float intoLocalY = y % (128 * 64);

		float intoXTile = intoLocalX / (128);
		float intoYTile = intoLocalY / (128);

		StructureTileData d = layout[(int) intoXTile][(int) intoYTile];

		return d;

	}

	public void loadShipLayout() {

		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(new File("ships/" + filePath)));

			data = br.readLine();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public int getQuadrantX() {
		return quadrantX;
	}

	public void setQuadrantX(int quadrantX) {
		this.quadrantX = quadrantX;
	}

	public int getQuadrantY() {
		return quadrantY;
	}

	public void setQuadrantY(int quadrantY) {
		this.quadrantY = quadrantY;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}

	public int getChecksum() {
		return checksum;
	}

	public void setChecksum(int checksum) {
		this.checksum = checksum;
	}

	public int getSectorID() {
		return sectorID;
	}

	public void setSectorID(int sectorID) {
		this.sectorID = sectorID;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public String getStructureClass() {
		return structureClass;
	}

	public void setStructureClass(String structureClass) {
		this.structureClass = structureClass;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public StructureTileData[][] getLayout() {
		return layout;
	}

	public void setLayout(StructureTileData[][] layout) {
		this.layout = layout;
	}

	public void parseTileData() {

		int startIndex = 0;
		int endIndex = 2;

		checksum = 0;

		for (int y = 0; y < tileHeight; y++) {

			for (int x = 0; x < tileWidth; x++) {

				String idata = data.substring(startIndex, endIndex);
				startIndex += 2;
				endIndex += 2;

				int val = Integer.parseInt(idata, 16);

				checksum += val;

				layout[x][y] = new StructureTileData(x, y, val);
			}
		}
	}

}
