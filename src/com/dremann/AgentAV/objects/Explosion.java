package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.AnimatedSprite;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.math.AABB;

public class Explosion extends AttackBounded {
	
	protected AnimatedSprite anim;
	
	boolean end;
	
	public Explosion(int x, int y, boolean friend, Level level) {
		super(x - 8, y - 8, 20, 15, EXPLOSION, friend, new AABB(-8, -8, 24, 24), AnimatedSprite.explosion[1], level);
		anim = new AnimatedSprite(4, 5, AnimatedSprite.explosion);
	}
	
	@Override
	public void update() {
		if(anim.getFrame() == anim.getLength() - 1)
			alive = false;
		
		checkCollisions();
		
		anim.update();
		spr = anim.getSprite();
	}
	
	protected void checkCollisions() {
		for(int i = 0; i < lvl.entListLength(); i++) {
			Entity e = lvl.getEntity(i);
			if(friendly == e.friendly)
				continue;
			
			if(position.x() + bounds.vecMax.x() >= e.position.x() + e.bounds.vecMin.x() && 
			   position.x() + bounds.vecMin.x() < e.position.x() + e.bounds.vecMax.x() && 
			   position.y() + bounds.vecMax.y() >= e.position.y() + e.bounds.vecMin.y() && 
			   position.y() + bounds.vecMin.y() < e.position.y() + e.bounds.vecMax.y()) {
				collidedEnt(e);
			}
		}
	}
	
}
