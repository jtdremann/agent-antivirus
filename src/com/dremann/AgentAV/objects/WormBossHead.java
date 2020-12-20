package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.ProgressTracker;
import com.dremann.AgentAV.graphics.AnimatedSprite;
import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.level.tile.Tile;
import com.dremann.AgentAV.math.Vector;

public class WormBossHead extends Mob {
	
	protected static int withinTileRange = 4;
	protected static int prevPosLen = 4;
	protected static int attAcrWait = 60;
	private static int roomFloorHeight = Tile.SIZE * 8;
	
	protected Vector[] prevPos;
	private static float attAcrSpeed = 13f;
	private static float attAcrPassed = 4f;
	private static float attJumpYSpeed = -6f;
	private static float undergroundSpeed = 6f;
	
	private static float maxChange = 1f;
	private static float ugMinYChange = 0.25f;
	private static float gravity = 0.25f;
	private static float minChange = 0.1f; //x while in air,
	
	protected int wait; //do nothing if > 0
	private int attCooldown;
	private int segmentsLeft = 9;
	
	private WormBossBody[] segments;
	
	private boolean attacking;
	private boolean attJump;
	private boolean attJStartAttempt;
	private boolean attJStopAttempt;
	private boolean attAcrScreen;
	private boolean attReady;
	private boolean stopJump;
	
	private boolean goLeft;
	
	public WormBossHead(Level level) {
		super(0, 384, 6, 6, 26, 26, 500, false, new AnimatedSprite(0, 1, AnimatedSprite.wormBoss), level);
		tileCol = false;
		
		prevPos = new Vector[prevPosLen];
		for(int i = 0; i < prevPos.length; i++)
			prevPos[i] = new Vector(position);
		
		attack = 20;
		knockback = 15;
		attCooldown = 300;
		//body segments
		segments = new WormBossBody[segmentsLeft];
		segments[0] = new WormBossBody(level, null, this);
		
		for(int i = 1; i < segments.length; i++) {
			segments[i] = new WormBossBody(level, segments[i - 1], this);
		}
		//add body segments to lvl
		for(int i = 0; i < segments.length; i++) {
			lvl.addEntity(segments[i]);
		}
	}
	
	public void update() {
		if(wait > 0) {
			wait--;
			return;
		}
		
		if(health <= 0) {
			destroy();
			return;
		}
		
		if(attCooldown > 0)
			attCooldown--;
		//choose to attack
		if(!attacking && attCooldown <= 0) {
			//attack across screen
			if(getCenterX() < Tile.SIZE * 2 && goLeft) {
				if(Level.randNextInt(10) < 4) {
					attAcrScreen = true;
					attacking = true;
				}
			}
			else if(getCenterX() > 768 - Tile.SIZE * 2 && !goLeft) {
				if(Level.randNextInt(10) < 4) {
					attAcrScreen = true;
					attacking = true;
				}
			}
			else if(!attJStartAttempt && Math.abs(lvl.getPlayerPos().x() - getCenterX()) < Tile.SIZE * 4) {
				if(Level.randNextInt(10) < 5) {
					attJump = true;
					attacking = true;
				}
			}
			attJStartAttempt = Math.abs(lvl.getPlayerPos().x() - getCenterX()) < Tile.SIZE * 4;
		}
		else { //Stop attack
			if(attAcrScreen || stopJump) {
				if((getCenterX() > 768 + 768 / 2 && goLeft) || (getCenterX() < -768 / 2 && !goLeft)) {
					attacking = false;
					attAcrScreen = false;
					attCooldown = 300;
					attReady = false;
					stopJump = false;
				}
			}
			else if(attJump) {
				if(!attJStopAttempt && getCenterY() < roomFloorHeight) {
					if(Level.randNextInt(10) < 3) {
						stopJump = true;
						attJump = false;
						attCooldown = 300;
						attReady = false;
					}
				}
				attJStopAttempt = getCenterY() < roomFloorHeight;
			}
		}
		//START MOVE
		if(!attacking) {
			position.set(position.x(), 384);
			
			if(getCenterX() < Tile.SIZE * 2) {
				goLeft = false;
			}
			else if(getCenterX() > 768 - Tile.SIZE * 2) {
				goLeft = true;
			}
			
			float xVel = velocity.x();
			if(goLeft)
				xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, -undergroundSpeed, maxChange);
			else
				xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, undergroundSpeed, maxChange);
			
			velocity.set(xVel, 0);
		}
		else if(attAcrScreen) {
			if(getCenterX() < -768 && goLeft) {
				Vector halfBounds = bounds.vecMax.add(bounds.vecMin).multiply(0.5f);
				
				position.set(-halfBounds.x(), roomFloorHeight - halfBounds.y());
				wait = attAcrWait;
				velocity.set(0, 0);
				attReady = true;
				
				anim.setRow(1);
				
				return;
			}
			else if(getCenterX() > 768 * 2 && !goLeft) {
				Vector halfBounds = bounds.vecMax.add(bounds.vecMin).multiply(0.5f);
				
				position.set(768 - halfBounds.x(), roomFloorHeight - halfBounds.y());
				wait = attAcrWait;
				velocity.set(0, 0);
				attReady = true;
				
				anim.setRow(3);
				
				return;
			}
			
			if(attReady) { //goLeft is opposite
				boolean pOnLeft = lvl.getPlayerPos().x() - getCenterX() < 0;
				float maxX = attAcrSpeed;
				if((goLeft && pOnLeft) || (!goLeft && !pOnLeft))
					maxX = attAcrPassed;
				
				float xVel = velocity.x();
				if(goLeft)
					xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, maxX, maxChange);
				else
					xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, -maxX, maxChange);
				
				velocity.set(xVel, 0);
			}
		}
		else if(attJump) {
			float maxX = undergroundSpeed;
			if(lvl.getPlayerPos().x() - getCenterX() < 0)
				maxX = -maxX;
			
			float xVel = velocity.x();
			float yVel = velocity.y();
			
			if(getCenterY() > roomFloorHeight) {
				xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, maxX, maxChange);
				if(yVel <= 0) {
					if(Math.abs(lvl.getPlayerPos().x() - getCenterX()) < Tile.SIZE * withinTileRange)
						yVel = com.dremann.AgentAV.math.Math.approachVel(yVel, attJumpYSpeed, maxChange);
					else
						yVel = com.dremann.AgentAV.math.Math.approachVel(yVel, 0, maxChange);
				}
				else
					yVel = com.dremann.AgentAV.math.Math.approachVel(yVel, 0, ugMinYChange);
			}
			else {
				xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, maxX, minChange);
				yVel += gravity;
			}
			
			velocity.set(xVel, yVel);
		}
		else if(stopJump) {
			float xVel = velocity.x();
			
			float maxX = attAcrPassed;;
			if(!goLeft)
				maxX = -maxX;

			xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, maxX, maxChange);
			float yVel = velocity.y();
			
			if(Math.abs(getCenterY() - roomFloorHeight) <= yVel) {
				position.set(position.x(), roomFloorHeight - (bounds.vecMax.add(bounds.vecMin).y() / 2));
				yVel = 0;
			}
			else if(getCenterY() > roomFloorHeight) {
				yVel = com.dremann.AgentAV.math.Math.approachVel(yVel, 1f, ugMinYChange);
			}
			else if(getCenterY() < roomFloorHeight) {
				yVel += gravity;
			}
			
			velocity.set(xVel, yVel);
		}
		
		if(Math.abs(velocity.x()) > Math.abs(velocity.y())) {
			if(velocity.x() < 0)
				anim.setRow(3);
			else
				anim.setRow(1);
		}
		else {
			if(velocity.y() < 0)
				anim.setRow(0);
			else
				anim.setRow(2);
		}
		
		super.update();
		
		for(int i = prevPos.length - 1; i > 0; i--) {
			prevPos[i] = prevPos[i - 1];
		}
		prevPos[0] = position;
	}
	
	protected void destroy() {
		if(segmentsLeft > 0) {
			segments[--segmentsLeft].destroy();
			wait = 10;
			return;
		}
		
		alive = false;
		lvl.addPacket(new LargeHP(getCenterX(), getCenterY(), lvl));
		ProgressTracker.killedWormBoss = true;
		lvl.addDoor(20, 6, 15, 0);
	}
	
	public void collidedAttack(Attack a) {
		if(tCD >= 0)
			return;
		health -= a.attack;
		tCD = 30;
	}
	
	@Override
	public void render(Screen scr) {
		if(tCD < 0 || tCD / 2 % 2 == 1)
			scr.renderSprite((int)position.x(), (int)position.y(), spr, false);
		
		float percent = (float)health / maxHealth;
		scr.drawBossHealth(percent);
	}
	
}
