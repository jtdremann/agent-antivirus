package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.AnimatedSprite;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.math.Vector;

public class MothBrown extends Mob {
	
	private static int speed = 3;
	
	boolean left;
	boolean up;
	
	public MothBrown(int x, int y, Level level) {
		super(x, y, 3, 21, 27, 30, 1, false, new AnimatedSprite(3, 4, AnimatedSprite.mothBrown), level);
		tileCol = false;
		up = true;
		if(lvl.getPlayerPos().x() < getCenterX()) {
			left = true;
			anim.setRow(0);
		}
		else {
			left = false;
			anim.setRow(1);
		}
	}
	
	public void update() {
		if(up)
			UPdate();
		else
			moveHor();
		
		if(position.x() + bounds.vecMax.x() < lvl.getMinX() || position.x() > lvl.getMaxX() || 
		   position.y() + bounds.vecMax.y() < lvl.getMinY() || position.y() > lvl.getMaxY())
			alive = false;
		
		super.update();
	}
	
	private void UPdate() { // haha pun
		Vector pPos = lvl.getPlayerPos();
		
		if(lvl.getPlayerPos().x() < getCenterX()) {
			left = true;
			anim.setRow(0);
		}
		else {
			left = false;
			anim.setRow(1);
		}
		
		if(pPos.y() > getCenterY()) {
			up = false;
			moveHor();
			return;
		}
		
		float yVel = -2;
		
		velocity.set(0, yVel);
	}
	
	private void moveHor() {
		float xVel = 0;
		
		if(left)
			xVel = -speed;
		else
			xVel = speed;
		
		velocity.set(xVel, 0);
	}
	
	public void collidedAttack(Attack a) {
		if(tCD >= 0)
			return;
		health -= a.attack;
		tCD = 30;
	}
}
