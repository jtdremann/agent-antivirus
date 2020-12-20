package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.math.Vector;

public class WormBossBody extends Entity {
	
	protected WormBossBody parent;
	protected WormBossHead head;
	protected boolean hasParent;
	protected Vector[] prevPos;
	
	public WormBossBody(Level level, WormBossBody par, WormBossHead he) {
		super(0, 384, 6, 6, 26, 26, false, Sprite.wormBossBody, level);
		tileCol = false;
		parent = par;
		head = he;
		if(parent != null)
			hasParent = true;
		
		prevPos = new Vector[WormBossHead.prevPosLen];
		for(int i = 0; i < prevPos.length; i++)
			prevPos[i] = new Vector(position);
		
		attack = 10;
		knockback = 15;
	}
	
	public void update() {
		if(head.wait > 0)
			return;
		
		//move
		tCD = head.tCD;
		
		if(hasParent)
			velocity.set(parent.prevPos[parent.prevPos.length - 1].subtract(position));
		else
			velocity.set(head.prevPos[head.prevPos.length - 1].subtract(position));
		
		super.update();
		//set PrevPositions
		for(int i = prevPos.length - 1; i > 0; i--) {
			prevPos[i] = prevPos[i - 1];
		}
		prevPos[0] = position;
	}
	
	protected void destroy() {
		alive = false;
		lvl.addPacket(new MediumHP(getCenterX(), getCenterY(), lvl));
	}
	
	public void collidedEnt(Entity e) {
		//do nothing
	}
	
	public void collidedAttack(Attack a) {
		head.collidedAttack(a);
	}
}
