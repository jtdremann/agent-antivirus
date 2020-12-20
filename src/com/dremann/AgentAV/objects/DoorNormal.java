package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Sprite;

public class DoorNormal extends Door {

	public DoorNormal(int x, int y, int id) {
		super(x, y, 0, id, Sprite.doorNormClosed);
		// TODO Auto-generated constructor stub
	}
	
	protected void changeSprite() {
		spr = Sprite.doorNormOpen;
	}

}
