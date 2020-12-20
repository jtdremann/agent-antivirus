package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.math.AABB;
import com.dremann.AgentAV.math.Vector;

public abstract class Packet {
	
	public static final int HEALTH10 = 0;
	public static final int HEALTH20 = 1;
	public static final int HEALTH50 = 2;
	public static final int AMMO1 = 3;
	public static final int AMMO3 = 4;
	public static final int AMMO5 = 5;
	
	private int id;
	private boolean alive;
	
	protected Level lvl;
	protected Sprite spr;
	protected Vector position;
	protected static final AABB bounds = new AABB(0, 0, 8, 8);
	
	public Packet(int x, int y, int i, Sprite sprite, Level level) {
		position = new Vector(x - 4, y - 4);
		spr = sprite;
		lvl = level;
		alive = true;
		
		id = i;
	}
	
	public void collect() {
		alive = false;
	}
	
	public void render(Screen scr) {
		scr.renderSprite((int)position.x(), (int)position.y(), spr, false);
	}
	
	public int getID() { return id; }
	public boolean isAlive() { return alive; }
	
}
