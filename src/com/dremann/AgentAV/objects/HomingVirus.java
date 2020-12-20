package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.AnimatedSprite;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.math.Vector;

public class HomingVirus extends Mob {
	
	private static float sChange = 0.5f;
	private static float maxSpeed = 6;
	
	public HomingVirus(int x, int y, Level level) {
		super(x, y, 6, 6, 26, 26, 75, false, new AnimatedSprite(3, 16, AnimatedSprite.homingVirus), level);
		attack = 15;
	}
	
	public void update() {
		Vector pPos = lvl.getPlayerPos();
		Vector center = new Vector(getCenterX(), getCenterY());
		Vector dir;
		if(pPos.equals(center))
			dir = new Vector(0, 0);
		else
			dir = pPos.subtract(center).normalize();
		Vector maxS = dir.multiply(maxSpeed);
		
		float xVel = velocity.x();
		float yVel = velocity.y();
		
		xVel = com.dremann.AgentAV.math.Math.approachVel(xVel, maxS.x(), Math.abs(dir.x()) * sChange);
		yVel = com.dremann.AgentAV.math.Math.approachVel(yVel, maxS.y(), Math.abs(dir.y()) * sChange);
		
		velocity.set(xVel, yVel);
		
		super.update();
	}
}
