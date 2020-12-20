package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.math.AABB;
import com.dremann.AgentAV.math.Vector;

public abstract class AttackBounded extends Attack {
	
	protected static AABB bounds;
	protected Sprite spr;
	
	public AttackBounded(int x, int y, int att, int kb, int t, boolean friend, AABB aabb, Sprite sprite, Level level) {
		super(x, y, att, kb, t, friend, level);
		bounds = aabb;
		spr = sprite;
	}
	
	public AttackBounded(Vector pos, int att, int kb, int t, boolean friend, AABB aabb, Sprite sprite, Level level) {
		super(pos, att, kb, t, friend, level);
		bounds = aabb;
		spr = sprite;
	}
	
	public void render(Screen scr) {
		scr.renderSprite((int)position.x(), (int)position.y(), spr, false);
	}
	
	public int getCenterX() {
		return (int)((bounds.vecMax.x() + bounds.vecMin.x()) / 2 + position.x());
	}
	
	public int getCenterY() {
		return (int)((bounds.vecMax.y() + bounds.vecMin.y()) / 2 + position.y());
	}
	
}
