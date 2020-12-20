package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.AnimatedSprite;
import com.dremann.AgentAV.level.Level;

public class WormMinion extends GravityMob {
	
	private static int speed = 1;
	
	private boolean left;
	private int scaredCount;
	private int prevFrame;
	
	public WormMinion(int x, int y, Level level) {
		super(x, y + 16, 8, 8, 40, 16, 100, false, new AnimatedSprite(10, 8, AnimatedSprite.wormMinion), level);
		left = true;
	}
	
	public void update() {
		float xVel = 0;
		
		if(prevFrame != anim.getFrame()) {
			//move 1 pix every frame
			
			if(left)
				xVel = -speed;
			else
				xVel = speed;
			prevFrame = anim.getFrame();
		}
		
		if(scaredCount > 0) {
			xVel *= 4;
			scaredCount--;
		}
		else {
			anim.setRate(10);
		}
		
		velocity.set(xVel, velocity.y());
		
		super.update();
	}
	
	@Override
	public void collidedHor() {
		left = !left;
		super.collidedHor();
	}
	
	@Override
	public void collidedAttack(Attack a) {
		if(tCD >= 0)
			return;
		health -= a.attack;
		tCD = 30;
		
		scaredCount = 600;
		anim.setRate(4);
	}
}
