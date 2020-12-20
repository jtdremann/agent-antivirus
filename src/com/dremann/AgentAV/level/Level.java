package com.dremann.AgentAV.level;

import java.util.ArrayList;
import java.util.Random;

import com.dremann.AgentAV.Game;
import com.dremann.AgentAV.ProgressTracker;
import com.dremann.AgentAV.graphics.Message;
import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.level.tile.Tile;
import com.dremann.AgentAV.math.Vector;
import com.dremann.AgentAV.objects.Asteroid;
import com.dremann.AgentAV.objects.Attack;
import com.dremann.AgentAV.objects.ChargerVirus;
import com.dremann.AgentAV.objects.Door;
import com.dremann.AgentAV.objects.DoorMissile;
import com.dremann.AgentAV.objects.DoorNormal;
import com.dremann.AgentAV.objects.Entity;
import com.dremann.AgentAV.objects.HomingVirus;
import com.dremann.AgentAV.objects.MothBlue;
import com.dremann.AgentAV.objects.MothBrown;
import com.dremann.AgentAV.objects.MothRed;
import com.dremann.AgentAV.objects.Packet;
import com.dremann.AgentAV.objects.Player;
import com.dremann.AgentAV.objects.PlayerShip;
import com.dremann.AgentAV.objects.Spider;
import com.dremann.AgentAV.objects.VirusBoss;
import com.dremann.AgentAV.objects.WormBossHead;
import com.dremann.AgentAV.objects.WormMinion;

public class Level {
	
	private static Player player = new Player(null);
	private static PlayerShip ship = new PlayerShip(null);
	private static Random rand = new Random(System.currentTimeMillis());
	private static final int tickMax = 1260;
	private static final int messageDelay = 60;
	
	private static int messTimer;
	private static int inRoom;
	private static int countdown;
	private static ArrayList<Message> messageQueue = new ArrayList<Message>();
	
	private boolean canEndGame;
	private int width, height; //in tiles
	private int endGameTimer;
	private int tick;
	private int maxX, maxY, minX, minY; //rendering vars
	private ArrayList<Entity> ents;
	private ArrayList<Attack> bullets;
	private ArrayList<Packet> packets;
	private ArrayList<Door> doors;
	private Camera cam;
	private int[] tiles;
	
	private int lvlID;
	
	private Vector gravity;
	
	public static int randNextInt(int i) {
		return rand.nextInt(i);
	}
	
	public Level(int w, int h, int[] t, byte[] b, byte[] d, int lid) {
		width = w;
		height = h;
		maxX = width * Tile.SIZE;
		maxY = height * Tile.SIZE;
		minX = 0;
		minY = 0;
		lvlID = lid;
		tiles = t;
		ents = new ArrayList<Entity>();
		bullets = new ArrayList<Attack>();
		packets = new ArrayList<Packet>();
		doors = new ArrayList<Door>();
		gravity = new Vector(0.0f, 1f);
		
		if(lid >= ProgressTracker.HUB)
			countdown = 0;
		
		if(lid == ProgressTracker.CS) {
			countdown = 3600;
			ship = new PlayerShip(this);
		}
		
		if(b != null && !(lid == 4 && ProgressTracker.clearedUSB)) {
			for(int i = 0; i < b.length; i += 3) {
				if(ProgressTracker.killedWormBoss && b[i] == LevelLoader.WORM_BOSS)
					continue;
				if(ProgressTracker.killedVirusBoss && b[i] == LevelLoader.VIR_BOSS)
					continue;
				addEntity(toEnt(b[i], b[i + 1], b[i + 2]));
			}
		}
		
		if(d != null) {
			for(int i = 0; i < d.length; i += 4) {
				doors.add(toDoor(d[i], d[i + 1], d[i + 2], d[i + 3]));
			}
		}
		
		if((lid == ProgressTracker.HD_BOSS && !ProgressTracker.killedVirusBoss) || (lid == ProgressTracker.RAM_BOSS && !ProgressTracker.killedWormBoss))
			doors = new ArrayList<Door>();
		
		getMessages(lid);
		
		player.setLevel(this);
		cam = new Camera();
	}
	
	public void setOffsets(Screen scr) {
		int w = scr.getWidth();
		int h = scr.getHeight() - 32;
		
		if(lvlID != ProgressTracker.CS) {
			int x = player.getCenterX() - w / 2;
			int y = player.getCenterY() - h / 2;
			
			if(x > maxX - w)
				x = maxX - w;
			if(x < minX)
				x = minX;
			if(y > maxY - h)
				y = maxY - h;
			if(y < minY)
				y = minY;
			
			cam.update(x, y);
			
			scr.setOffsets(cam.getX(), cam.getY());
		}
		else {
			int y = ship.getY() - 393;
			if(y > maxY - h)
				y = maxY - h;
			if(y < minY)
				y = minY;
			
			cam.update(0, y);
			
			scr.setOffsets(cam.getX(), cam.getY());
		}
	}
	
	public void update() {
		if(messTimer <= 0) {
			if(!messageQueue.isEmpty())
				messageQueue.get(0).update();
		}
		else
			messTimer--;
		
		if(lvlID < ProgressTracker.HUB && ProgressTracker.clearedUSB) {
			if(countdown > 0)
				countdown--;
			else {
				ProgressTracker.clearedUSB = false;
				player.respawn();
			}
		}
		
		//CYBER SPACE LEVEL
		if(lvlID == ProgressTracker.CS) {
			if(countdown > 0)
				countdown--;
			else {
				Game.loadLevel(ProgressTracker.CS + 1);
				player.setPosition(80, 208);
				return;
			}
			ship.update();
			//Spawn asteroids
			if(cam.getY() > 192) {
				if(randNextInt(100) < 2 * (((3600 - countdown) / 1200) + 1))
					addEntity(new Asteroid(randNextInt(184) * 4 + 16, cam.getY() - 64, this));
			}
		}
		else {
			tick++;
			if(tick >= tickMax)
				tick = 0;
			
			updateTiles();
			
			player.update();
		}
		for(int i = 0; i < ents.size(); i++) 
			ents.get(i).update();
		for(int i = 0; i < bullets.size(); i++)
			bullets.get(i).update();
		
		destroyDead();
		
		if(endGameTimer > 0) {
			canEndGame = true;
			endGameTimer--;
		}
		else if(canEndGame)
			Game.setState(Game.ENDGAME);
	}
	
	private void updateTiles() {
		for(int i = 0; i < tiles.length; i++) {
			Tile t = Tile.getTile(tiles[i]);
			
			if(t.getSpawn() == 0)
				continue;
			
			int x = (i % width) << Tile.SHIFT;
			int y = (i / width) << Tile.SHIFT;
			x += 16; //center of block
			y += 16; //center of block
			
			if(t.getSpawn() == Tile.VIRUS && tick % 420 == 0) {
				addEntity(new HomingVirus(x - 16, y - 16, this));
				continue;
			}
			if(t.getSpawn() == Tile.MOTHBROWN && tick % 180 == 0) {
				addEntity(new MothBrown(x - 15, y - 25, this));
				continue;
			}
		}
	}
	
	public void render(Screen scr) {
		for(Door d : doors)
			d.render(scr);
		for(Packet p : packets)
			p.render(scr);
		for(Entity e : ents)
			e.render(scr);
		for(Attack a : bullets)
			a.render(scr);
		if(lvlID != ProgressTracker.CS)
			player.render(scr);
		else
			ship.render(scr);
		//Tiles
		int x0 = (int)cam.getX() >> Tile.SHIFT; //left most column of tiles to render
		int x1 = (int)cam.getX() + scr.getWidth() >> Tile.SHIFT; //right most column to render
		int y0 = (int)cam.getY() >> Tile.SHIFT; //top most row
		int y1 = ((int)cam.getY() + scr.getHeight() >> Tile.SHIFT) - 1; //bottom most row
		for(int y = y0; y <= y1; y++) {
			for(int x = x0; x <= x1; x++) {
				Tile t = getTile(x, y);
				if(t == null)
					continue;
				scr.renderSprite(x << Tile.SHIFT, y << Tile.SHIFT, t.spr, false);
			}
		}
		
		if(messTimer <= 0) {
			if(!messageQueue.isEmpty())
				messageQueue.get(0).render(scr);
		}
		
		player.drawHUD(scr);
		
		if(countdown > 0)
			scr.drawCountdown((countdown / 60) + 1);
	}
	
	public void tileCollision(int x, int y, Entity e) {
		Tile t = Tile.getTile(tiles[x + y * width]);
		if(e.isPlayer()) {
			if(t.isHUpgrade()) {
				player.upgradeHealth();
				ProgressTracker.healthCollected[lvlID] = true;
				tiles[x + y * width] = 0;
				addMessage(120, "ENERGY UPGRADED");
				if(ProgressTracker.healthUpgrades == 1) {
					addMessage(300, "THIS UPGRADE INCREASES YOUR HEALTH BY 100.");
					addMessage(300, "EACH GREEN BOX NEXT TO YOUR HEALTH BAR CONTAINS 100 HEALTH.");
				}
			}
			else if(t.isAUpgrade()) {
				player.upgradeAmmo();
				ProgressTracker.ammoCollected[lvlID] = true;
				tiles[x + y * width] = 0;
				if(ProgressTracker.ammoUpgrades == 1) {
					addMessage(120, "DELETER ACQUIRED");
					addMessage(300, "FIRE THIS WEAPON WITH THE RIGHT MOUSE BUTTON. USE IT TO OPEN RED DOORS.");
				}
				else
					addMessage(120, "DELETER AMMO UPGRADED");
			}
			else if(t.isDJUpgrade()) {
				ProgressTracker.djCollected = true;
				tiles[x + y * width] = 0;
				addMessage(120, "DOUBLE JUMP ACQUIRED");
				addMessage(300, "TAP THE JUMP BUTTON WHILE IN THE AIR TO JUMP A SECOND TIME.");
			}
		}
	}
	
	public Tile getTile(int x, int y) {
		if(x >= width || x < 0 || y >= height || y < 0)
			return Tile.getTile(0);
		return Tile.getTile(tiles[y * width + x]);
	}
	
	public void addEntity(Entity e) {
		ents.add(e);
	}
	
	public void addAttack(Attack a) {
		bullets.add(a);
	}
	
	public void addPacket(Packet p) {
		packets.add(p);
	}
	
	public static void addMessage(int time, String s) {
		messageQueue.add(new Message(time, s));
	}
	
	private void destroyDead() {
		for(int i = 0; i < ents.size(); i++) {
			if(!ents.get(i).isAlive()) {
				ents.remove(i);
				i--;
				
				if(ents.size() == 0 && lvlID == ProgressTracker.HUB - 1) {
					addMessage(300, "AGENT AV! THE FLASH DRIVE IS EJECTING! GET BACK TO THE MOTHERBOARD");
					ProgressTracker.clearedUSB = true;
					countdown = 3599;
				}
			}
		}
		
		for(int i = 0; i < packets.size(); i++) {
			if(!packets.get(i).isAlive()) {
				packets.remove(i);
				i--;
			}
		}
		
		for(int i = 0; i < bullets.size(); i++) {
			if(!bullets.get(i).isAlive()) {
				bullets.remove(i);
				i--;
			}
		}
		
		if(!messageQueue.isEmpty()) {
			if(!messageQueue.get(0).isShown()) {
				messageQueue.remove(0);
				messTimer = messageDelay;
			}
		}
	}
	
	public Vector getGravity() { return gravity; }
	public Vector getPlayerPos() { return new Vector(player.getCenterX(), player.getCenterY()); }
	public Player getPlayer() { return player; }
	public int width() { return width; }
	public int height() { return height; }
	public int getMaxX() { return maxX; }
	public int getMinX() { return minX; }
	public int getMaxY() { return maxY; }
	public int getMinY() { return minY; }
	public int getLID() { return lvlID; }
	public int entListLength() { return ents.size(); }
	public int bulletListLength() { return bullets.size(); }
	public int packetListLength() { return packets.size(); }
	public int doorListLength() { return doors.size(); }
	public Entity getEntity(int i) { return ents.get(i); }
	public Attack getBullet(int i) { return bullets.get(i); }
	public Packet getPacket(int i) { return packets.get(i); }
	public Door getDoor(int i) { return doors.get(i); }
	
	private Entity toEnt(byte e, byte x, byte y) { //Bosses are special cases
		int ex = (int)x << Tile.SHIFT;
		int ey = (int)y << Tile.SHIFT;
		switch(e) {
		case LevelLoader.WORM_MIN:
			return new WormMinion(ex, ey, this);
		case LevelLoader.MOTH_RED:
			return new MothRed(ex, ey, this);
		case LevelLoader.MOTH_BLUE:
			return new MothBlue(ex, ey, this);
		case LevelLoader.HOME_VIR:
			return new HomingVirus(ex, ey, this);
		case LevelLoader.CHAR_VIR:
			return new ChargerVirus(ex, ey, this);
		case LevelLoader.WORM_BOSS:
			return new WormBossHead(this);
		case LevelLoader.VIR_BOSS:
			return new VirusBoss(this);
		case LevelLoader.SPIDER:
			return new Spider(this);
		default:
			return new WormMinion(ex, ey, this);
		}
	}
	
	private Door toDoor(byte type, byte id, byte x, byte y) {
		int dx = (int)x << Tile.SHIFT;
		int dy = (int)y << Tile.SHIFT;
		switch(type) {
			case 0:
				return new DoorNormal(dx, dy, id);
			case 1:
				return new DoorMissile(dx, dy, id);
			default:
				return new DoorNormal(dx, dy, id);
		}
	}
	
	public void addDoor(int x, int y, int id, int type) {
		int dx = (int)x << Tile.SHIFT;
		int dy = (int)y << Tile.SHIFT;
		if(type == 0)
			doors.add(new DoorNormal(dx, dy, id));
		else if(type == 1)
			doors.add(new DoorMissile(dx, dy, id));
	}
	
	private void getMessages(int lid) {
		if(inRoom == lid)
			return;
		if(lid == ProgressTracker.USB) {
			inRoom = ProgressTracker.USB;
			addMessage(120, "FLASH DRIVE");
			if(!ProgressTracker.reachedUSB) {
				addMessage(300, "SCANS ARE SHOWING THAT SOMETHING IS ON THIS FLASH DRIVE.");
				addMessage(300, "USE THE A AND D KEYS TO MOVE. PRESS THE W OR SPACE KEY TO JUMP AND ENTER DOORS.");
				addMessage(300, "FIRE YOUR WEAPON WITH THE LEFT MOUSE BUTTON TO OPEN DOORS AND DESTROY VIRUSES.");
				ProgressTracker.reachedUSB = true;
			}
		}
		else if(lid == ProgressTracker.HUB) {
			inRoom = ProgressTracker.HUB;
			addMessage(180, "WELCOME BACK TO THE CHIPSET AGENT AV!");
			if(!ProgressTracker.reachedHUB) {
				addMessage(300, "FROM HERE YOU CAN ACCESS THE DIFFERENT PARTS OF THE COMPUTER.");
				addMessage(300, "UNFORTUNATELY SOME VIRUSES AND WORMS MANAGED TO INFECT SOME FILES...");
				addMessage(300, "HURRY TO THE HARD DRIVE AND STOP THEM BEFORE THEY INFECT MORE!");
				ProgressTracker.reachedHUB = true;
			}
			else if(!ProgressTracker.retWithAmmo && ProgressTracker.ammoUpgrades > 0) {
				addMessage(300, "GREAT! YOU FOUND THE DELETER! USE IT TO CLEAR OUT THE RAM!");
				ProgressTracker.retWithAmmo = true;
			}
			else if(!ProgressTracker.retWithDJ && ProgressTracker.djCollected) {
				addMessage(300, "AWESOME! NOW THAT THE COMPUTER IS CLEARED OUT YOU CAN ATTACK THE SOURCE!");
				addMessage(300, "GO THROUGH THE WIRELESS CARD AND USE THE SPACESHIP TO HEAD OFF INTO CYBERSPACE!");
				ProgressTracker.retWithDJ = true;
			}
		}
		else if(lid == ProgressTracker.HD) {
			inRoom = ProgressTracker.HD;
			addMessage(120, "HARD DRIVE");
		}
		else if(lid == ProgressTracker.RAM) {
			inRoom = ProgressTracker.RAM;
			addMessage(120, "RAM");
		}
		else if(lid == ProgressTracker.WC) {
			inRoom = ProgressTracker.WC;
			addMessage(120, "WIRELESS CARD");
			if(!ProgressTracker.reachedWC) {
				addMessage(300, "IT LOOKS LIKE MORE VIRUSES ARE COMING THROUGH THIS CARD!");
				addMessage(240, "HURRY AND GET TO THE SPACESHIP!");
				ProgressTracker.reachedWC = true;
			}
		}
		else if(lid == ProgressTracker.CS - 1) {
			addMessage(240, "THE SPACE SHIP IS THROUGH THAT DOOR!");
		}
		else if(lid == ProgressTracker.CS) {
			inRoom = ProgressTracker.CS;
			addMessage(120, "CYBERSPACE");
			addMessage(240, "USE THE A AND D KEYS TO STEER THE SHIP.");
			addMessage(240, "AVOID THE INCOMING DATA FOR 60 SECONDS!");
		}
	}
	
	public static void respawnPlayer() {
		player.respawn();
	}
	
	public static void resetStatics() {
		player = new Player(null);
		ship = new PlayerShip(null);
		messageQueue = new ArrayList<Message>();
		messTimer = 0;
		inRoom = -1;
	}
	
	public void triggerEndGame() {
		endGameTimer = 120;
	}
}
