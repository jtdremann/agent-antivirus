package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.level.Level;

public class SmallAP extends Packet {
	
	public SmallAP(int x, int y, Level level) {
		super(x, y, AMMO1, Sprite.smallAP, level);
	}
	
}
