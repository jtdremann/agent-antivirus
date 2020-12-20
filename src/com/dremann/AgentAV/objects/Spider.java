package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.AnimatedSprite;
import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.math.Vector;

public class Spider extends Mob {
	
	private static final int animWalkSpd = 8;
	private static final int animChargeSpd = 4;
	
	private static final int walkSpeed = 1;
	private static final int chargeSpeed = 10;
	private static final int lowHealth = 1250;
	private static final int lowestHealth = 500;
	private static final int idleWait = 60;
	private static final int idleMoveTime = 96;
	private static final int attWait = 180;
	private static final int lowAttWait = 240;
	private static final int lowestAttWait = 300;
	
	private boolean idle;
	private boolean onLeft;
	private int wait; //movement wait
	private int notGo;
	private int moveTime;
	private int attCD;
	
	public Spider(Level level) {
		super(496, 160, 86, 32, 240, 96, 2000, false, new AnimatedSprite(8, 6, AnimatedSprite.spiderBoss), level);
		idle = true;
		moveTime = idleMoveTime;
		attCD = attWait;
	}
	
	public void update() {
		if(attCD > 0)
			attCD--;
		if(wait > 0) {
			anim.setRate(0);
			anim.setFrame(0);
			wait--;
		}
		
		if(wait <= 0) {
			if(moveTime <= 0) {
				if(idle && ((attCD <= 0 && Level.randNextInt(1000) < 600) || (attCD <= 0 && notGo >= 3))) {
					idle = false;
					onLeft = false;
					notGo = 0;
				}
				else {
					moveTime = idleMoveTime;
					notGo++;
				}
			}
		}
		//StartMove
		if(wait <= 0) {
			float xVel = velocity.x();
			if(idle) {
				if(moveTime > 0) {
					moveTime--;
					if(onLeft) {
						xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, walkSpeed, 0.5f);
						anim.setRate(-animWalkSpd);
					}
					else {
						xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, -walkSpeed, 0.5f);
						anim.setRate(animWalkSpd);
					}
				}
				
				if(moveTime <= 0) {
					onLeft = !onLeft;
					wait = idleWait;
				}
			}
			else {
				if(onLeft) {
					xVel = chargeSpeed;
					anim.setRate(-animWalkSpd);
				}
				else {
					xVel = -chargeSpeed;
					anim.setRate(animChargeSpd);
				}
			}
			velocity.set(xVel, 0);
		}
		else {
			velocity.set(0, 0);
			if(!idle) {
				int attCheck = wait - 121;
				if(attCheck >= 0 && attCheck % 60 == 0)
					fireWeb();
			}
		}
		
		super.update();
	}
	
	private void fireWeb() {
		Vector player = lvl.getPlayerPos();
		if(player.x() > position.x() + 250)
			lvl.addAttack(new Web(position.add(250, 46), player, friendly, lvl));
	}
	
	@Override
	protected void collidedHor() {
		if(velocity.x() != 0) {
			if(!idle) {
				if(!onLeft) {
					onLeft = true;
					if(health <= lowestHealth)
						wait = lowestAttWait;
					else if(health <= lowHealth)
						wait = lowAttWait;
					else
						wait = attWait;
				}
				else {
					idle = true;
					onLeft = false;
					wait = idleWait;
					attCD = 180;
				}
			}
		}
		super.collidedHor();
	}
	
	@Override
	protected void collidedAttack(Attack a) {
		if(tCD >= 0)
			return;
		health -= a.attack;
		tCD = 30;
	}
	
	protected void destroy() {
		if(!alive)
			return;
		
		alive = false;
		lvl.triggerEndGame();
	}
	
	@Override
	public void render(Screen scr) {
		if(tCD < 0 || tCD / 2 % 2 == 1)
			scr.renderSprite((int)position.x(), (int)position.y(), spr, false);
		
		float percent = (float)health / maxHealth;
		scr.drawBossHealth(percent);
	}
	
}
