package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.ProgressTracker;
import com.dremann.AgentAV.graphics.AnimatedSprite;
import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.math.Vector;

public class VirusBoss extends Mob {
	
	private static final int LEFT_BOUND = 32;
	private static final int RIGHT_BOUND = 710;
	
	private static final int maxSpeed = 10;
	private static final int skyAttSpd = 3;
	private static final int xDistFromPlayer = 96;
	private static final int shieldDist = 64;
	private static final int groundAttDist = 160;
	private static final int shieldRespTime = 300;
	private static final int attackCD = 300;
	private static final int fireCD = 300;
	protected static final int DEG_PER_COUNT = 4;
	protected static final int MIN_COUNT_MAX = 360 / DEG_PER_COUNT;
	protected static final int MINION_MAX = 12;
	
	protected boolean shielding, canSpawnShield, skyAtt, startAtt, noMins;
	protected boolean onLeft, destMin;
	protected int minionCount, minDist, mins;
	protected int fireTime, shieldTime, attackTime;
	protected int pingTimer = 0;
	
	public VirusBoss(Level level) {
		super(448, 48, 6, 4, 58, 60, 500, false, new AnimatedSprite(0, 1, AnimatedSprite.virusBoss), level);
		tileCol = false;
		
		spawnShield();
	}
	
	public void update() {
		minionCount++;
		if(!shielding)
			shieldTime++;
		if(shieldTime >= shieldRespTime) {
			shieldTime = 0;
			canSpawnShield = true;
			destMin = true;
		}
		if(attackTime > 0)
			attackTime--;
		//CHOOSE TO ATTACK
		if(attackTime <= 0 && shielding) {
			if(skyAtt) {
				if(position.x() <= LEFT_BOUND) {
					minDist = shieldDist;
					skyAtt = false;
					attackTime = attackCD;
				}
			}
			else {
				if(mins == 0) {
					shielding = false;
				}
				else {
					int r = Level.randNextInt(1000);
					int check = (MINION_MAX + 1 - mins) * 20;
					if(r < check) //10
						shielding = false;
					else if(mins > 6 && r < check + 350) {
						startAtt = true;
						skyAtt = true;
					}
					else if(!noMins && r < check + 50) {
						startAtt = true;
						skyAtt = true;
					}
				}
			}
		}
		if(mins == 0 && canSpawnShield) {
			spawnShield();
		}
		//START MOVE
		if(!skyAtt) {
			if(Level.randNextInt(10) < 3)
				onLeft = !onLeft;
			
			if(position.x() <= LEFT_BOUND)
				onLeft = false;
			if(position.x() > RIGHT_BOUND)
				onLeft = true;
			
			Vector to;
			if(onLeft)
				to = new Vector(lvl.getPlayerPos().x() - xDistFromPlayer - 32, 48);
			else
				to = new Vector(lvl.getPlayerPos().x() + xDistFromPlayer - 32, 48);
			
			boolean leftOfTo = to.x() > position.x() ? true : false;
			
			float xVel = velocity.x();
			
			if(leftOfTo)
				xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, maxSpeed, 0.5f);
			else
				xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, -maxSpeed, 0.5f);
			
			velocity.set(xVel, 0);
		}
		else {
			float xVel = velocity.x();
			if(startAtt) {
				xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, maxSpeed, 0.5f);
				if(position.x() > RIGHT_BOUND)
					startAtt = false;
				
				if(!startAtt)
					minDist = groundAttDist;
			}
			else
				xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, -skyAttSpd, 0.5f);
			velocity.set(xVel, 0);
		}
		
		fireTime++;
		if(fireTime >= fireCD) {
			fire();
			fireTime = 0;
		}
		anim.setRow(fireTime / 75); //(fireCD / anim.getLength()) = 45
		anim.update();
		
		super.update();
	}
	
	private void fire() {
		Vector center = new Vector(getCenterX(), getCenterY());
		Vector t1 = center.add(-18, 8);
		Vector t2 = center.add(18, 8);
		Vector t3 = center.add(0, -20);
		
		lvl.addAttack(new BulletNormal(t1, t1.subtract(center).add(t1), false, lvl));
		lvl.addAttack(new BulletNormal(t2, t2.subtract(center).add(t2), false, lvl));
		lvl.addAttack(new BulletNormal(t3, t3.subtract(center).add(t3), false, lvl));
		lvl.addAttack(new BulletNormal(center, lvl.getPlayerPos(), false, lvl));
	}
	
	private void spawnShield() {
		destMin = false;
		
		for(int i = 0; i < MINION_MAX; i++)
			lvl.addEntity(new VirusMinion(getCenterX(), getCenterY(), i, this, lvl));
		
		minDist = shieldDist;
		shielding = true;
		canSpawnShield = false;
		
		attackTime = attackCD;
		mins = 12;
	}
	
	@Override
	protected void collidedAttack(Attack a) {
		if(tCD >= 0)
			return;
		health -= a.attack;
		tCD = 30;
	}
	
	protected void destMin() {
		mins--;
	}
	
	protected void destroy() {
		if(!alive)
			return;
		
		alive = false;
		lvl.addPacket(new LargeHP(getCenterX(), getCenterY(), lvl));
		ProgressTracker.killedVirusBoss = true;
		lvl.addDoor(20, 6, 9, 0);
	}
	
	@Override
	public void render(Screen scr) {
		if(tCD < 0 || tCD / 2 % 2 == 1)
			scr.renderSprite((int)position.x(), (int)position.y(), spr, false);
		
		float percent = (float)health / maxHealth;
		scr.drawBossHealth(percent);
	}
}
