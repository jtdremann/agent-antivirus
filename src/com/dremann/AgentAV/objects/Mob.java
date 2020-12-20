package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.ProgressTracker;
import com.dremann.AgentAV.graphics.AnimatedSprite;
import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.math.Vector;

public abstract class Mob extends Entity {
	
	protected int health, maxHealth, fireTime;
	
	protected AnimatedSprite anim;
	
	public Mob(int x, int y, int xBmin, int yBmin, int xBmax, int yBmax, int hlth, boolean friend, AnimatedSprite aSpr, Level level) {
		super(x, y, xBmin, yBmin, xBmax, yBmax, friend, aSpr.getSprite(), level);
		anim = aSpr;
		
		health = hlth;
		maxHealth = hlth;
		
		attack = 10;
		knockback = 15;
	}
	
	public void update() {
		if(health <= 0) {
			destroy();
			return;
		}
		
		anim.update();
		spr = anim.getSprite();
		
		super.update();
	}
	
	protected void destroy() {
		alive = false;
		int r = Level.randNextInt(100);
		if(r < 10)
			lvl.addPacket(new SmallHP(getCenterX(), getCenterY(), lvl));
		else if(r < 13)
			lvl.addPacket(new MediumHP(getCenterX(), getCenterY(), lvl));
		else if(r < 15)
			lvl.addPacket(new LargeHP(getCenterX(), getCenterY(), lvl));
		else if(r < 30 && ProgressTracker.ammoUpgrades > 0)
			lvl.addPacket(new SmallAP(getCenterX(), getCenterY(), lvl));
		else if(r < 35 && ProgressTracker.ammoUpgrades > 0)
			lvl.addPacket(new MediumAP(getCenterX(), getCenterY(), lvl));
		else if(r < 37 && ProgressTracker.ammoUpgrades > 0)
			lvl.addPacket(new LargeAP(getCenterX(), getCenterY(), lvl));
	}
	
	protected void collidedEnt(Entity e) {
		if(tCD >= 0)
			return;
		if(!e.player) { //friendly != e.friendly might be needed
			health -= e.attack;
			tCD = 30;
			
			Vector center = new Vector(getCenterX(), getCenterY());
			Vector ec = new Vector(e.getCenterX(), e.getCenterY());
			
			Vector kbDir = center.subtract(ec);
			if(!center.equals(ec))
				kbDir = center.subtract(ec).normalize();
			else
				kbDir = new Vector(1, 0);
			
			velocity = kbDir.multiply(e.knockback);
		}
	}
	
	protected void collidedAttack(Attack a) {
		if(tCD >= 0)
			return;
		health -= a.attack;
		tCD = 30;
		
		Vector center = new Vector(getCenterX(), getCenterY());
		Vector ec = new Vector(a.getCenterX(), a.getCenterY());
		
		Vector kbDir = center.subtract(ec);
		if(!center.equals(ec))
			kbDir = center.subtract(ec).normalize();
		else
			kbDir = new Vector(1, 0);
		
		velocity = kbDir.multiply(a.knockback);
	}
	
	public void render(Screen scr) {
		if(tCD < 0 || tCD / 2 % 2 == 1)
			scr.renderSprite((int)position.x(), (int)position.y(), spr, false);
		
		if(health == maxHealth)
			return;
		float bLen = bounds.vecMax.subtract(bounds.vecMin).x();
		int hbY = (int) position.y() - 12;
		int hbX = (int) (getCenterX() - bLen);
		
		float percent = (float) health / maxHealth;
		int hblen = (int) (bLen * 2 * percent);
		scr.drawHealth(hbX, hbY, hblen, percent);
	}
}
