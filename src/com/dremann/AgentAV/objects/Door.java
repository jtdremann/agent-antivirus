package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.math.AABB;
import com.dremann.AgentAV.math.Vector;

public abstract class Door {
	
	//Types use bullet types
	
	private int type;
	private int id;
	private boolean open;
	
	protected Sprite spr;
	//private AnimatedSprite anim;
	protected Vector position;
	protected static final AABB bounds = new AABB(0, 0, 32, 64);
	
	public Door(int x, int y, int t, int id, Sprite s) {
		position = new Vector(x, y);
		
		this.id = id;
		type = t;
		spr = s;
	}
	
	public void collidedBullet(Bullet b) {
		if(!b.friendly || open)
			return;
		if(type == 0)
			open = true;
		if(type == b.type)
			open = true;
		
		if(open == true)
			changeSprite();
	}
	
	protected abstract void changeSprite();
	
	public void render(Screen scr) {
		scr.renderSprite((int)position.x(), (int)position.y(), spr, false);
	}
	
	public boolean isOpen() { return open; }
	public int getID() { return id; }
}
