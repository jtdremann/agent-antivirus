package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.math.Vector;
import com.dremann.AgentAV.graphics.AnimatedSprite;
import com.dremann.AgentAV.level.Level;

public class ChargerVirus extends Mob {
	
	private static int attackCharge = 180; //3 second charge
	private static int attackTime = 10;
	private static float sChange = 0.5f;
	private static int dist = 192;
	
	private float maxSpeed = 17;
	private int attCount = 0;
	private boolean onLeft;
	private boolean locked, attacking;
	
	public ChargerVirus(int x, int y, Level level) {
		super(x, y, 8, 8, 24, 40, 150, false, new AnimatedSprite(30, 16, AnimatedSprite.chargerVirus), level);
		onLeft = true;
		tileCol = false;
		attack = 20;
	}
	
	public void update() {
		int animRow = 0;
		if(attacking)
			//animRow = 3;
			anim.setRate(0);
		else {
			if(attCount > 120)
				anim.setRate(1);
			else if(attCount > 60)
				anim.setRate(2);
			else if(attCount >= 0)
				anim.setRate(4);
		}
		
		if(locked)
			locked();
		else if(attacking) {
			attCount--;
			if(attCount <= 0) {
				attacking = false;
				onLeft = !onLeft;
			}
		}
		else
			unlocked();
		
		anim.setRow(animRow);
		
		super.update();
	}
	
	private void locked() {
		if(!locked)
			return;
		Vector center = new Vector(getCenterX(), getCenterY());
		if(center.x() < lvl.getMinX()) {
			locked = false;
			onLeft = false;
			unlocked();
			return;
		}
		else if(center.x() >= lvl.getMaxX()) {
			locked = false;
			onLeft = true;
			unlocked();
			return;
		}
		
		Vector to = lvl.getPlayerPos();
		if(onLeft)
			to = to.add(-dist, -dist);
		else
			to = to.add(dist, -dist);
		velocity = to.subtract(center);
		
		attCount++;
		if(attCount > attackCharge) {
			attacking = true;
			locked = false;
			attCount = attackTime;
			
			Vector p = lvl.getPlayerPos();
			velocity.set(p.subtract(center).normalize().multiply(maxSpeed)); //Velocity doesn't change while attacking
		}
	}
	
	private void unlocked() {
		if(locked)
			return;
		
		Vector center = new Vector(getCenterX(), getCenterY());
		/*if(center.x() < lvl.getMinX())
			onLeft = false;						Use if change onLeft while unlocked
		else if(center.x() >= lvl.getMaxX())
			onLeft = true;//*/
		
		Vector to = lvl.getPlayerPos();
		if(onLeft)
			to = to.add(-dist, -dist);
		else
			to = to.add(dist, -dist);
		to = to.subtract(center);
		Vector maxS = new Vector(0, 0);
		if(!to.equals(0, 0))
			maxS = to.normalize().multiply(maxSpeed);
		
		float xVel = velocity.x();
		float yVel = velocity.y();
		xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, maxS.x(), sChange);
		yVel = com.dremann.AgentAV.math.Math.approachVel(yVel, maxS.y(), sChange);
		boolean inX = Math.abs(xVel) >= Math.abs(to.x());
		boolean inY = Math.abs(yVel) >= Math.abs(to.y());
		if(inX)
			xVel = to.x();
		if(inY) {
			yVel = to.y();
			if(inX)
				locked = true;
		}
		
		velocity.set(xVel, yVel);
	}
	
	public void collidedEnt(Entity e) {
		if(tCD >= 0)
			return;
		if(!e.player) {
			health -= e.attack;
			tCD = 30;
		}
	}
	
	@Override
	public void collidedAttack(Attack a) {
		if(tCD >= 0)
			return;
		health -= a.attack;
		tCD = 30;
	}
	
}
