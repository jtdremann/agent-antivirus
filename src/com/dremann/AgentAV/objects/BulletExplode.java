package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.math.Vector;

public class BulletExplode extends Bullet {
	
	public BulletExplode(int x, int y, int bounces, Vector to, boolean friend, Level level) {
		super(x, y, 25, 20, EXPLOSION, to, friend, Sprite.expBullet, level);
	}
	
	public BulletExplode(Vector pos, int bounces, Vector to, boolean friend, Level level) {
		super(pos, 25, 20, EXPLOSION, to, friend, Sprite.expBullet, level);
	}
	
	protected void collided() {
		alive = false;
		lvl.addAttack(new Explosion(getCenterX(), getCenterY(), friendly, lvl));
	}
	
}
