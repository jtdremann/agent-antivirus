package com.dremann.AgentAV.level;

import com.dremann.AgentAV.ProgressTracker;
import com.dremann.AgentAV.level.tile.Tile;

public class LevelLoader {
	
	//I have no clue how to read levels from a file inside JAR >.<
	
	public static Level loadLevel(int room) {
		if(room < 0 || room >= LevelHolder1.levels.length + LevelHolder2.levels.length)
			return loadErrorLevel();
		
		byte[] b;
		if(room < LevelHolder1.levels.length)
			b = LevelHolder1.levels[room];
		else
			b = LevelHolder2.levels[room - LevelHolder1.levels.length];
		
		int w = b[0] & 0xFF; //0xFF to remove sign of byte
		int h = b[1] & 0xFF;
		int[] tiles = new int[w * h];
		int i = 2;
		while(i < b.length && b[i] != (byte)0b11111111) {
			int id = b[i++] & 0xFF;
			int pos = b[i++] & 0xFF; //positions are 16 bits
			pos = pos << 8; //shift first 8
			pos += ((int) b[i++] & 0xFF); //add on the next 8
			if(id == 4 && ProgressTracker.ammoCollected[room])
				id = 0;
			else if(id == 3 && ProgressTracker.healthCollected[room])
				id = 0;
			else if(id == 5 && ProgressTracker.djCollected)
				id = 0;
			if(pos < tiles.length && Tile.exists(id))
				tiles[pos] = id;
		}
		i++; //i++ to skip 1111111 bit
		int entCount = 0;
		byte[] ents = new byte[b[i++] & 0xFF];
		while(entCount < ents.length && i < b.length && b[i] != (byte)0b11111111) {
			ents[entCount++] = b[i++];
		}
		i++;
		int doorCount = 0;
		byte[] doors = new byte[b[i++] & 0xFF];
		while(doorCount < doors.length && i < b.length && b[i] != (byte)0b11111111) {
			doors[doorCount++] = b[i++];
		}
		
		return new Level(w, h, tiles, ents, doors, room);
	}
	
	/*public static Level loadLevel(int room) {
		try {
			InputStream is = new FileInputStream(new File(LevelLoader.class.getResource("/levels/lvl" + room + ".aavl").toURI()));
			byte[] b = new byte[is.available()];
			is.read(b);
			int w = b[0] & 0xFF; //0xFF to remove sign of byte
			int h = b[1] & 0xFF;
			int[] tiles = new int[w * h];
			int i = 2;
			while(i < b.length && b[i] != (byte)0b11111111) {
				int id = b[i++] & 0xFF;
				int pos = b[i++] & 0xFF; //positions are 16 bits
				pos = pos << 8; //shift first 8
				pos += ((int) b[i++] & 0xFF); //add on the next 8
				if(id == 4 && ProgressTracker.ammoCollected[room])
					id = 0;
				else if(id == 3 && ProgressTracker.healthCollected[room])
					id = 0;
				else if(id == 5 && ProgressTracker.djCollected)
					id = 0;
				if(pos < tiles.length && Tile.exists(id))
					tiles[pos] = id;
			}
			i++; //i++ to skip 1111111 bit
			int entCount = 0;
			byte[] ents = new byte[b[i++] & 0xFF];
			while(entCount < ents.length && i < b.length && b[i] != (byte)0b11111111) {
				ents[entCount++] = b[i++];
			}
			i++;
			int doorCount = 0;
			byte[] doors = new byte[b[i++] & 0xFF];
			while(doorCount < doors.length && i < b.length && b[i] != (byte)0b11111111) {
				doors[doorCount++] = b[i++];
			}
			is.close();
			return new Level(w, h, tiles, ents, doors, room);
		} catch(Exception e) {
			e.printStackTrace();
			return loadErrorLevel();
		}
	}//*/
	
	public static Level loadErrorLevel() {
		//32w
		//25h
		int[] tiles = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
						1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
						1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
						1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		return new Level(32, 25, tiles, null, null, 0);
	}
	
	public static final byte WORM_MIN = 0;
	public static final byte MOTH_RED = 1;
	public static final byte MOTH_BLUE = 2;
	public static final byte HOME_VIR = 3;
	public static final byte CHAR_VIR = 4;
	public static final byte WORM_BOSS = 5;
	public static final byte VIR_BOSS = 6;
	public static final byte SPIDER = 7;
	
	/*public static int toTile(char c) {
		switch(c) {
			case '1':
				return 1;
			case 'F':
				return 2;
			case 'H':
				return 3;
			case 'A':
				return 4;
			case 'D':
				return 5;
			case 'M':
				return 6;
			case 'V':
				return 7;
			default:
				return 0;
		}
	}
	
	public static byte entToByte(char c) { 
		switch(c) {
		case 'W':
			return WORM_MIN;
		case 'R':
			return MOTH_RED;
		case 'B':
			return MOTH_BLUE;
		case 'H':
			return HOME_VIR;
		case 'C':
			return CHAR_VIR;
		case 'O':
			return WORM_BOSS;
		case 'V':
			return VIR_BOSS;
		case 'S':
			return SPIDER;
		default:
			return WORM_MIN;
		}
	}
	
	public static void converter(int room) {
		int[] tiles = loadConver(room);
		int wid = lConverWidth(room);
		int hei = tiles.length / wid;
		//Tiles
		try {
			File f = new File(LevelLoader.class.getResource("/levels/lvl" + room + ".aavl").toURI());
			if(!f.exists())
				f.mkdirs();
			FileOutputStream w = new FileOutputStream(f);
			int tracker = 0;
			for(int i = 0; i < tiles.length; i++) {
				if(tiles[i] != 0)
					tracker++;
			}
			byte[] b = new byte[tracker * 3 + 2];
			b[0] = (byte) wid;
			b[1] = (byte) hei;
			tracker = 2;
			for(int i = 0; i < tiles.length; i++) {
				int id = tiles[i];
				if(id <= 0)
					continue;
				b[tracker++] = (byte) id;
				b[tracker++] = (byte) (i >> 8);
				b[tracker++] = (byte) i;
			}
			//ENTS
			byte[] entDat = loadConverEnt(room);
			byte[] doorsDat = loadConverDoor(room);
			
			byte[] lvl = new byte[b.length + entDat.length + doorsDat.length + 4]; //+ 2 for next flags + 2 for length of ent and door arrs
			
			int lvlTrack = 0;
			for(int i = 0; i < b.length; i++) {
				lvl[lvlTrack++] = b[i];
			}
			lvl[lvlTrack++] = (byte)0b11111111;
			lvl[lvlTrack++] = (byte)entDat.length;
			for(int i = 0; i < entDat.length; i++) {
				lvl[lvlTrack++] = entDat[i];
			}
			lvl[lvlTrack++] = (byte)0b11111111;
			lvl[lvlTrack++] = (byte)doorsDat.length;
			for(int i = 0; i < doorsDat.length; i++) {
				lvl[lvlTrack++] = doorsDat[i];
			}
			
			w.write(lvl);
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int[] loadConver(int room) { // Load type 1
		try {
			Scanner s = new Scanner(new File(LevelLoader.class.getResource("/levels/lvl" + room + ".txt").toURI()));
			int w = s.nextLine().length() - 1;
			int h = 1;
			while(s.hasNextLine()) {
				s.nextLine();
				h++;
			}
			s = new Scanner(new File(LevelLoader.class.getResource("/levels/lvl" + room + ".txt").toURI()));
			s.useDelimiter("");
			int[] tiles = new int[w * h];
			for(int i = 0; i < tiles.length; i++) {
				char c = s.next().charAt(0);
				if(c == 'e') {
					s.next();
					s.next();
					c = s.next().charAt(0);
				}
				tiles[i] = toTile(c);
			}
			s.close();
			return tiles;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] loadConverEnt(int room) { // Load type 1
		try {
			Scanner s = new Scanner(new File(LevelLoader.class.getResource("/levels/lvl" + room + "Ents.txt").toURI()));
			int numEnts = 0;
			while(s.hasNextLine()) {
				s.nextLine();
				numEnts++;
			}
			byte[] ents = new byte[numEnts * 3];
			s = new Scanner(new File(LevelLoader.class.getResource("/levels/lvl" + room + "Ents.txt").toURI()));
			
			for(int i = 0 ; i < numEnts; i++) {
				char ent = s.next().charAt(0);
				byte x = s.nextByte();
				byte y = s.nextByte();
				
				ents[i * 3] = entToByte(ent);
				ents[i * 3 + 1] = x;
				ents[i * 3 + 2] = y;
			}
			
			s.close();
			return ents;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] loadConverDoor(int room) { // Load type 1
		try {
			Scanner s = new Scanner(new File(LevelLoader.class.getResource("/levels/lvl" + room + "Doors.txt").toURI()));
			int numDoor = 0;
			while(s.hasNextLine()) {
				s.nextLine();
				numDoor++;
			}
			byte[] doors = new byte[numDoor * 4];
			s = new Scanner(new File(LevelLoader.class.getResource("/levels/lvl" + room + "Doors.txt").toURI()));
			
			for(int i = 0 ; i < numDoor; i++) {
				doors[i * 4] = s.nextByte(); //type
				doors[i * 4 + 1] = s.nextByte(); //id
				doors[i * 4 + 2] = s.nextByte(); //x
				doors[i * 4 + 3] = s.nextByte(); //y
			}
			
			s.close();
			return doors;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int lConverWidth(int room) {
		try {
			Scanner s = new Scanner(new File(LevelLoader.class.getResource("/levels/lvl" + room + ".txt").toURI()));
			int r = s.nextLine().length() - 1;
			s.close();
			return r;
		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}//*/
}
