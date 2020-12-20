package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.AnimatedSprite;
import com.dremann.AgentAV.level.Level;

public class MothRed extends Mob {
	
	private static int speed = 3;
	
	boolean left;
	
	public MothRed(int x, int y, Level level) {
		super(x, y, 3, 21, 27, 30, 1, false, new AnimatedSprite(3, 4, AnimatedSprite.mothRed), level);
		left = Level.randNextInt(100) < 50;
		// TODO Auto-generated constructor stub
	}
	
	public void update() {
		float xVel = 0;
		
		if(left) {
			anim.setRow(0);
			xVel = -speed;
		}
		else {
			anim.setRow(1);
			xVel = speed;
		}
		
		velocity.set(xVel, 0);
		
		super.update();
	}
	
	public void collidedHor() {
		left = !left;
		super.collidedHor();
	}
	
	public void collidedAttack(Attack a) {
		
	}
}
