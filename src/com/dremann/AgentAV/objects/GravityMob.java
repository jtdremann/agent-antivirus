package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.AnimatedSprite;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.math.Vector;

public abstract class GravityMob extends Mob {
	
	private static float stickyModifier = 0.5f;
	
	protected boolean onGround;
	protected int stickyCount;
	
	public GravityMob(int x, int y, int xBmin, int yBmin, int xBmax, int yBmax, int hlth, boolean friend, AnimatedSprite aSpr, Level level) {
		super(x, y, xBmin, yBmin, xBmax, yBmax, hlth, friend, aSpr, level);
	}
	
	public void update() {
		if(!onGround)
			velocity = velocity.add(lvl.getGravity());
		
		//if(velocity.y() > maxYVel)
		//	velocity.set(velocity.x(), maxYVel);
		
		if(velocity.y() >= 0 && checkColPoints(bl.add(1, 0), br.add(-1, 0)))
			onGround = true;
		else
			onGround = false;
		
		if(stickyCount > 0) {
			stickyCount--;
			velocity.set(velocity.x() * stickyModifier, velocity.y());
		}
		
		super.update();
	}//*/
	
	protected void collidedCor(Vector entCor) {
		if(velocity.y() >= 0 && (entCor.equals(bl) || entCor.equals(br))) {
			velocity.set(velocity.x(), 0);
			onGround = true;
		}
	}
	
	@Override
	protected void collidedAttack(Attack a) {
		if(a.type == Attack.STICKY)
			stickyCount = 300;
		
		super.collidedAttack(a);
	}
}
