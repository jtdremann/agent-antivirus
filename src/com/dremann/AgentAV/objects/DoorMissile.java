package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Sprite;

public class DoorMissile extends Door {
	
	public DoorMissile(int x, int y, int id) {
		super(x, y, Bullet.EXPLOSION, id, Sprite.doorInfClosed);
	}
	
	protected void changeSprite() {
		spr = Sprite.doorInfOpen;
	}
	
}
