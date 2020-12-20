package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.Game;
import com.dremann.AgentAV.ProgressTracker;
import com.dremann.AgentAV.graphics.AnimatedSprite;
import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.input.KeyInput;
import com.dremann.AgentAV.input.Mouse;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.math.Vector;

public class Player extends GravityMob {
	
	private static final boolean RIGHT = false;
	private static final boolean LEFT = true;
	
	private static final int XSPEED = 4;
	private static final int JUMPSPEED = 19;
	private static final int ANIMRATE = 4;
	
	private int ammo, maxAmmo, secFT, fireCD; //cooldown to prevent firing of both weapons at the same time
	private int armYMod, respWait; //spawn x and y, arm y draw modifier, respawn wait
	private boolean upLastUpdate;
	private boolean canDoubleJump;
	private boolean attacking;
	private boolean dir;
	private AnimatedSprite animArm;
	private Sprite arm;
	
	public Player(Level level) {
		super(104, 208, 3, 1, 14, 48, 100, true, new AnimatedSprite(4, 12, AnimatedSprite.playerWalking), level);
		arm = AnimatedSprite.playerThrow[0];
		player = true;
		ammo = 0;
		animArm = new AnimatedSprite(2, 6, AnimatedSprite.playerThrow);
		dir = RIGHT;
	}
	
	public void update() {
		if(respWait > 0) {
			respWait--;
			return;
		}
		else if(!alive)
			respawn();
		
		float yVel = velocity.y();
		if(KeyInput.up && !upLastUpdate) {
			if(onGround) {
				if(!goThroughDoor() && stickyCount <= 0)
					yVel = -JUMPSPEED;
			}
			else if(canDoubleJump && stickyCount <= 0) {
				canDoubleJump = false;
				yVel = -JUMPSPEED;
			}
		}
		
		canDoubleJump = (ProgressTracker.djCollected && onGround) || canDoubleJump;
		
		upLastUpdate = KeyInput.up;
		
		float xVel = velocity.x();
		if(KeyInput.left && !KeyInput.right) {
			xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, -XSPEED, 1f);
			dir = LEFT;
		}
		else if(KeyInput.right && !KeyInput.left) {
			xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, XSPEED, 1f);
			dir = RIGHT;
		}
		else
			xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, 0, 1f);
		
		boolean facing;
		if(Mouse.position.x() > getCenterX())
			facing = RIGHT;
		else
			facing = LEFT;
		
		//***SPRITE CHOSING***
		int chooser = 0;
		if(facing == LEFT)
			chooser = 1;
		
		if(facing != dir)
			anim.setRate(-ANIMRATE);
		else
			anim.setRate(ANIMRATE);
		
		if(xVel != 0 && onGround)
			anim.setRow(chooser);
		else {
			anim.setRow(chooser);
			anim.setFrame(0);
			anim.setRate(0);
		}
		
		if(!onGround && !attacking) {
			arm = Sprite.playerMisc[chooser];
			armYMod = -15;
		}
		else {
			armYMod = 0;
			if(attacking)
				animArm.setRate(2);
			else
				animArm.setRate(0);
			animArm.setRow(chooser);
			animArm.update();
			arm = animArm.getSprite();
			if(animArm.getFrame() == 0)
				attacking = false;
		}
		
		velocity.set(xVel, yVel);
		
		super.update();
		
		updateWeapons();
		collectPackets();
	}
	
	private void updateWeapons() {
		if(fireCD >= 0)
			fireCD--;
		if(fireTime >= 0)
			fireTime--;
		if(secFT >= 0)
			secFT--;
		
		if(fireCD < 0) {
			if(Mouse.left && fireTime < 0) {
				firePrim(getCenterX(), getCenterY(), Mouse.position);
				animArm.setFrame(1);
				attacking = true;
				return;
			}
			if(Mouse.right && secFT < 0 && ammo > 0) {
				fireSec(getCenterX(), getCenterY(), Mouse.position);
				animArm.setFrame(1);
				attacking = true;
				return;
			}
		}
	}
	
	private void firePrim(int x, int y, Vector m) {
		lvl.addAttack(new BulletNormal(x, y, m, true, lvl));
		fireTime = 15;
		fireCD = 5;
	}
	
	private void fireSec(int x, int y, Vector m) {
		lvl.addAttack(new BulletExplode(x, y, 0, m, true, lvl));
		secFT = 30;
		fireCD = 5;
		ammo--;
	}
	
	public void respawn() {
		if(!ProgressTracker.reachedHUB) {
			Level.resetStatics();
			ProgressTracker.reset();
			Game.loadLevel(ProgressTracker.USB);
			position.set(104, 208);
		}
		else {
			Game.loadLevel(ProgressTracker.HUB);
			position.set(656, 360);
		}
		if(health <= 0) {
			Level.addMessage(240, "UH OH! IT LOOKS LIKE YOU LOST ALL YOUR ENERGY!");
			Level.addMessage(240, "GOOD THING THERE IS A BACKUP OF YOU.");
			Level.addMessage(240, "TRY TO BE MORE CAREFUL SO YOU DO NOT LOSE PROGRESS!");
		}
		health = 100;
		if(ProgressTracker.ammoUpgrades > 0)
			ammo = 5;
		velocity.set(0, 0);
		alive = true;
	}
	
	protected void destroy() {
		if(respWait <= 0)
			respWait = 120;
		
		alive = false;
	}
	
	public void setLevel(Level l) {
		lvl = l;
	}
	
	@Override
	public void render(Screen scr) {
		if(alive == false)
			return;
		
		if(tCD < 0 || tCD / 2 % 2 == 1) {
			scr.renderSprite((int)position.x(), (int)position.y(), spr, false);
			scr.renderSprite((int)position.x() + 1, (int)position.y() + armYMod + 14, arm, false);
		}
	}
	
	public void upgradeHealth() {
		ProgressTracker.healthUpgrades++;
		maxHealth += 100;
		health = maxHealth;
	}
	
	public void upgradeAmmo() {
		ProgressTracker.ammoUpgrades++;
		maxAmmo += 5;
		ammo += 5;
	}
	
	private void collectPackets() {
		float px1 = position.x() + bounds.vecMin.x();
		float px2 = position.x() + bounds.vecMax.x();
		float py1 = position.y() + bounds.vecMin.y();
		float py2 = position.y() + bounds.vecMax.y();
		for(int i = 0; i < lvl.packetListLength(); i++) {
			Packet p = lvl.getPacket(i);
			if(!p.isAlive())
				continue;
			
			float x1 = p.position.x() + Packet.bounds.vecMin.x();
			float x2 = p.position.x() + Packet.bounds.vecMax.x();
			float y1 = p.position.y() + Packet.bounds.vecMin.y();
			float y2 = p.position.y() + Packet.bounds.vecMax.y();
			if(px1 < x2 && px2 > x1 && py1 < y2 && py2 > y1) {
				if(p.getID() == Packet.HEALTH10)
					health += 10;
				else if(p.getID() == Packet.HEALTH20)
					health += 20;
				else if(p.getID() == Packet.HEALTH50)
					health += 50;
				else if(p.getID() == Packet.AMMO1)
					ammo += 1;
				else if(p.getID() == Packet.AMMO3)
					ammo += 3;
				else if(p.getID() == Packet.AMMO5)
					ammo += 5;
				p.collect();
			}
		}
		
		if(health > maxHealth)
			health = maxHealth;
		if(ammo > maxAmmo)
			ammo = maxAmmo;
	}
	
	protected boolean goThroughDoor() {
		float px1 = position.x() + bounds.vecMin.x();
		float px2 = position.x() + bounds.vecMax.x();
		float py1 = position.y() + bounds.vecMin.y();
		float py2 = position.y() + bounds.vecMax.y();
		
		for(int i = 0; i < lvl.doorListLength(); i++) {
			Door d = lvl.getDoor(i);
			if(!d.isOpen())
				continue;
			
			float x1 = d.position.x() + Door.bounds.vecMin.x();
			float x2 = d.position.x() + Door.bounds.vecMax.x();
			float y1 = d.position.y() + Door.bounds.vecMin.y();
			float y2 = d.position.y() + Door.bounds.vecMax.y();
			if(px1 < x2 && px2 > x1 && py1 < y2 && py2 > y1) {
				int dID = d.getID();
				int oldID = lvl.getLID();
				if(oldID == 0 && dID == 4 && !ProgressTracker.clearedUSB) {
					if(!ProgressTracker.seenUSBMess) {
						Level.addMessage(300, "AGENT AV! YOU MUST CLEAR OUT THE USB BEFORE YOU RETURN TO THE MOTHERBOARD!");
						ProgressTracker.seenUSBMess = true;
					}
					return true;
				}
				
				Game.loadLevel(dID);
				
				if(oldID == 0 && dID == 4)
					position.set(656, 360);
				else if(dID == ProgressTracker.HD_BOSS || dID == ProgressTracker.RAM_BOSS || dID == ProgressTracker.CS)
					position.set(80, 208);
					setNewPos(oldID); //has to be after loadLevel
				return true;
			}
		}
		
		return false;
	}
	
	private void setNewPos(int oldLID) {
		for(int i = 0; i < lvl.doorListLength(); i++) {
			Door d = lvl.getDoor(i);
			
			if(d.getID() != oldLID)
				continue;
			
			position.set(d.position.x() + 8, d.position.y() + 16);
		}
	}
	
	public void drawHUD(Screen scr) {
		int h = health % 100;
		int hfull = health / 100;
		if(h == 0 && health > 0) {
			hfull--;
			h += 100;
		}
		float percent = (float)h / 100;
		scr.drawHUD(percent, ammo, hfull);
	}
	
	public void setPosition(int x, int y) {
		position.set(x, y);
	}
}
