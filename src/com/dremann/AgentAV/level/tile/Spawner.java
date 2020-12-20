package com.dremann.AgentAV.level.tile;

import com.dremann.AgentAV.graphics.Sprite;

public class Spawner extends Tile {
	
	public Spawner(int type, Sprite spr) {
		super(SOLID, spr);
		spawnType = type;
	}
	
}
