package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.level.Level;

public class LargeAP extends Packet {
	
	public LargeAP(int x, int y, Level level) {
		super(x, y, AMMO5, Sprite.largeAP, level);
	}
	
}
