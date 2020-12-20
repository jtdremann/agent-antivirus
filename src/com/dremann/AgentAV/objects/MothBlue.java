package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.AnimatedSprite;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.math.Vector;

public class MothBlue extends Mob {
	
	private static int speed = 3;
	private static int dist = 192;
	
	boolean left;
	boolean onGround;
	boolean landing;
	
	public MothBlue(int x, int y, Level level) {
		super(x, y, 3, 21, 27, 30, 35, false, new AnimatedSprite(3, 4, AnimatedSprite.mothBlue), level);
		onGround = false; //This is where I typed false = true
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
		if(landing && onGround)
			landing = false;
		
		Vector pPos = lvl.getPlayerPos();
		Vector center = new Vector(getCenterX(), getCenterY());
		
		if(lvl.getPlayerPos().x() < center.x()) {
			left = true;
			anim.setRow(0);
		}
		else {
			left = false;
			anim.setRow(1);
		}

		Vector sub;
		if(pPos.equals(center))
			sub = new Vector(0, 0);
		else
			sub = pPos.subtract(center);
		if(health >= maxHealth) {
			if(Math.abs(sub.x()) >= dist) {
				landing = true;
			}
		}
		
		if(!landing) {
			if(onGround && pPos.y() < center.y()) {
				if(health < maxHealth || (center.y() - pPos.y() < dist && Math.abs(sub.x()) < dist)) {
					onGround = false;
					anim.setRate(3);
				}
			}
			
			if(!onGround) {
				if(sub.equals(0, 0))
					velocity.set(0, 0);
				else
					velocity.set(sub.normalize().multiply(speed));
			}
		}
		else {
			float xVel = com.dremann.AgentAV.math.Math.approachVel(velocity.x(), 0.0f, 0.05f);
			float yVel = com.dremann.AgentAV.math.Math.approachVel(velocity.y(), speed, 0.25f);
			
			velocity.set(xVel, yVel);
		}
		
		if(velocity.y() >= 0 && checkColPoints(bl.add(1, 0), br.add(-1, 0))) {
			onGround = true;
			anim.setFrame(0);
			anim.setRate(0);
			velocity.set(0, 0);
		}
		else
			onGround = false;
		
		super.update();
	}
	
	@Override
	protected void collidedCor(Vector entCor) {
		velocity.set(0, velocity.y());
	}
	
	public void collidedAttack(Attack a) {
		if(tCD >= 0)
			return;
		health -= a.attack;
		tCD = 30;
	}
	
}
