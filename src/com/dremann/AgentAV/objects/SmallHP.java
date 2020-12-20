package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.level.Level;

public class SmallHP extends Packet {
	
	public SmallHP(int x, int y, Level level) {
		super(x, y, HEALTH10, Sprite.smallHP, level);
	}
	
}
