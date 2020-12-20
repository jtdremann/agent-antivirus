package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.math.Vector;

public class BulletNormal extends Bullet {
	
	public BulletNormal(int x, int y, Vector to, boolean friend, Level level) {
		super(x, y, 15, 10, NORMAL, to, friend, Sprite.normBullet, level);
	}
	
	public BulletNormal(Vector pos, Vector to, boolean friend, Level level) {
		super(pos, 15, 10, NORMAL, to, friend, Sprite.normBullet, level);
	}
	
}
