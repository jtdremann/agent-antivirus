package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.math.Vector;

public abstract class Attack {
	
	public static final int NORMAL = 0;
	public static final int EXPLOSION = 1;
	public static final int STICKY = 2;
	
	protected boolean friendly, alive;
	protected int attack, knockback, type;
	
	protected Level lvl;
	protected Vector position;
	
	public Attack(int x, int y, int att, int kb, int t, boolean friend, Level level) {
		position = new Vector(x, y);
		friendly = friend;
		lvl = level;
		
		attack = att;
		knockback = kb;
		type = t;
		
		alive = true;
	}
	
	public Attack(Vector pos, int att, int kb, int t, boolean friend, Level level) {
		position = pos;
		friendly = friend;
		lvl = level;
		
		attack = att;
		knockback = kb;
		type = t;
		
		alive = true;
	}
	
	protected void collidedEnt(Entity e) {
		e.collidedAttack(this);
	}
	
	public int getCenterX() {
		return (int) position.x();
	}
	
	public int getCenterY() {
		return (int) position.y();
	}
	
	public abstract void update();
	
	public abstract void render(Screen scr);
	
	public boolean isAlive() { return alive; }
	
}
