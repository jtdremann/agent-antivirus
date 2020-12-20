package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.level.Level;

public class LargeHP extends Packet {

	public LargeHP(int x, int y, Level level) {
		super(x, y, HEALTH50, Sprite.largeHP, level);
	}

}
