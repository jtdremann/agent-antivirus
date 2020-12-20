package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.level.Level;

public class Asteroid extends Entity {
	
	private static final int MAXYSPD = 3;
	
	public Asteroid(int x, int y, Level level) {
		super(x, y, 0, 0, 32, 32, false, Sprite.normBlock, level);
		velocity.set(0, (Level.randNextInt(50) % MAXYSPD) + 1);
	}
	
	@Override
	protected void collidedVer() {
		destroy();
	}
	
	@Override
	protected void collidedEnt(Entity e) {
		
	}
	
	@Override
	protected void collidedAttack(Attack a) {
		
	}
	
}
