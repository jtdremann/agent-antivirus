package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.level.Level;

public class MediumHP extends Packet {
	
	public MediumHP(int x, int y, Level level) {
		super(x, y, HEALTH20, Sprite.mediumHP, level);
	}
	
}
