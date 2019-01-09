package com.pineconeindustries.server.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.pineconeindustries.server.data.Room.roomType;
import com.pineconeindustries.server.log.Log;
import com.pineconeindustries.server.utils.Units;
import com.pineconeindustries.server.utils.Vector2;
import com.pineconeindustries.server.utils.Vector2.direction;

public class Structure {

	ArrayList<Room> rooms;

	private int quadrantX, quadrantY, tileWidth, tileHeight, checksum, sectorID, id;
	private float x, y;
	private String structureClass, filePath, name, data, roomData;
	StructureTileData[][] layout;

	public Structure(int id, int sectorID, String name, String structureClass, float x, float y, int quadrantX,
			int quadrantY) {
		this.id = id;
		this.sectorID = sectorID;
		this.name = name;
		this.structureClass = structureClass;
		this.x = x;
		this.y = y;
		this.tileHeight = Units.REGION_GRID_SIZE;
		this.tileWidth = Units.REGION_GRID_SIZE;
		this.quadrantX = quadrantX;
		this.quadrantY = quadrantY;
		this.roomData = "";
		filePath = "ships/" + id + "-" + name;

		layout = new StructureTileData[tileWidth][tileHeight];
		rooms = new ArrayList<Room>();

		loadShipLayout();

		parseTileData();

	}

	public boolean canMoveToPoint(Vector2 playerCenter, Vector2 destination, Vector2 dir) {

		if (dir.getDirection() == direction.left) {
			if (dir.y <= 0) {
				if (!getTileAt(destination.x - 1 + 32, destination.y).isWalkable()) {
					return false;
				}
			} else {
				if (!getTileAt(destination.x - 1 + 32, destination.y + 32).isWalkable()) {
					return false;
				}

			}

		} else if (dir.getDirection() == direction.up) {

			if (!getTileAt(destination.x + 32, destination.y - 1 + 32).isWalkable()) {
				return false;
			}

		} else if (dir.getDirection() == direction.down) {

			if (!getTileAt(destination.x + 32, destination.y + 1).isWalkable()) {
				return false;
			}

		} else if (dir.getDirection() == direction.right) {
			if (dir.y <= 0) {
				if (!getTileAt(destination.x + 1 + 32, destination.y).isWalkable()) {
					return false;
				}

			} else {
				if (!getTileAt(destination.x + 1 + 32, destination.y + 32).isWalkable()) {
					return false;
				}
			}

		}

		return true;

	}

	public StructureTileData getTileAt(float x, float y) {

		float intoLocalX = x % (128 * 64);
		float intoLocalY = y % (128 * 64);

		float intoXTile = intoLocalX / (128);
		float intoYTile = intoLocalY / (128);

		StructureTileData d = layout[(int) intoXTile][(int) intoYTile];

		return d;

	}

	public void addRoom(Room r) {
		rooms.add(r);
	}

	public Room getRoomByID(int id) {
		Room room = null;
		for (Room r : rooms) {

			if (r.getRoomID() == id) {
				room = r;
				break;
			}

		}

		return room;

	}

	public ArrayList<Room> getRooms() {
		return rooms;
	}

	public void loadShipLayout() {

		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(new File(filePath + ".txt")));

			data = br.readLine();

			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		try {
			br = new BufferedReader(new FileReader(new File(filePath + "-rooms.txt")));

			String line;

			while ((line = br.readLine()) != null) {

				String[] split = line.split("-");

				int roomID = Integer.parseInt(split[0]);
				int startX = Integer.parseInt(split[1]);
				int startY = Integer.parseInt(split[2]);
				int width = Integer.parseInt(split[3]);
				int height = Integer.parseInt(split[4]);

				Room r = new Room(roomID, startX, startY, width, height);

				if (split[5].equals("N")) {

				} else {

					if (split.length == 6) {
						String[] doorOne = split[5].split("x");
						int xLocOne = Integer.parseInt(doorOne[0]);
						int yLocOne = Integer.parseInt(doorOne[1]);
						r.addDoor(new Door(r, xLocOne, yLocOne, 0));

					} else if (split.length == 7) {
						String[] doorOne = split[5].split("x");
						String[] doorTwo = split[6].split("x");
						int xLocOne = Integer.parseInt(doorOne[0]);
						int yLocOne = Integer.parseInt(doorOne[1]);
						int xLocTwo = Integer.parseInt(doorTwo[0]);
						int yLocTwo = Integer.parseInt(doorTwo[1]);
						r.addDoor(new Door(r, xLocOne, yLocOne, 0));
						r.addDoor(new Door(r, xLocTwo, yLocTwo, 0));

					} else if (split.length == 8) {
						String[] doorOne = split[5].split("x");
						String[] doorTwo = split[6].split("x");
						String[] doorThree = split[7].split("x");
						int xLocOne = Integer.parseInt(doorOne[0]);
						int yLocOne = Integer.parseInt(doorOne[1]);
						int xLocTwo = Integer.parseInt(doorTwo[0]);
						int yLocTwo = Integer.parseInt(doorTwo[1]);
						int xLocThree = Integer.parseInt(doorThree[0]);
						int yLocThree = Integer.parseInt(doorThree[1]);
						r.addDoor(new Door(r, xLocOne, yLocOne, 0));
						r.addDoor(new Door(r, xLocTwo, yLocTwo, 0));
						r.addDoor(new Door(r, xLocThree, yLocThree, 0));

					} else {
						Log.print("TOO MANY DOORS");
					}

				}
				rooms.add(r);
			}

			br.close();

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

	public void setRoomData() {

		StringBuilder sb = new StringBuilder();

		sb.append(rooms.size() + "=");

		for (Room r : rooms) {

			sb.append(r.getRoomID() + "-" + r.getStartX() + "-" + r.getStartY() + "-" + r.getRoomWidth() + "-"
					+ r.getRoomHeight() + "-" + r.getRoomName() + "-" + r.getRoomTypeData() + "=");

		}

		String dataString = sb.toString();

		roomData = dataString.substring(0, dataString.length() - 1);

	}

	public String getRoomData() {

		return roomData;
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
				boolean canWalk = true;
				if (val == 1 || val == 2) {
					canWalk = false;
				}
				layout[x][y] = new StructureTileData(x, y, val, canWalk, this);
			}
		}
	}

}
