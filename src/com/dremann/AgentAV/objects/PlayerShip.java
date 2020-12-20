package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.input.KeyInput;
import com.dremann.AgentAV.level.Level;

public class PlayerShip extends Entity {
	
	private static final int YSPEED = -1;
	private static final int XSPEED = 3;
	
	private int esplode;
	
	public PlayerShip(Level level) {
		super(352, 3632, 17, 8, 47, 53, true, Sprite.spaceship, level);
	}
	
	public void update() {
		if(esplode > 0) {
			if(esplode % 20 == 0)
				lvl.addAttack(new Explosion(Level.randNextInt(38) + 13 + (int)position.x(), Level.randNextInt(42) + 7 + (int)position.y(), true, lvl));
			esplode--;
			return;
		}
		else if(!alive) {
			Level.respawnPlayer();
			return;
		}
		
		float xVel = velocity.x();
		if(KeyInput.left && !KeyInput.right && getCenterX() > 48)
			xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, -XSPEED, 1f);
		else if(KeyInput.right && !KeyInput.left && getCenterX() < 720)
			xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, XSPEED, 1f);
		else
			xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, 0, 1f);
		
		velocity.set(xVel, YSPEED);
		
		super.update();
	}
	
	@Override
	public void render(Screen scr) {
		if(!alive)
			return;
		super.render(scr);
	}
	
	@Override
	protected void collidedVer() {
		if(!alive)
			return;
		esplode = 120;
		destroy();
	}
	
	@Override
	protected void collidedEnt(Entity e) {
		if(!alive)
			return;
		esplode = 120;
		destroy();
	}
	
	@Override
	protected void collidedAttack(Attack a) {
		if(!alive)
			return;
		esplode = 120;
		destroy();
	}
	
}
