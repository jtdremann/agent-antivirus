package com.dremann.AgentAV.level.tile;

import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.math.AABB;

public abstract class Tile {
	
	public static final int SIZE = 32;
	public static final int SHIFT = 5; //used to divide faster
	public static final AABB BOUNDS = new AABB(0, 0, SIZE, SIZE);
	//public static final AABB DEST1X3 = new AABB(0, 0, SIZE, SIZE * 3);
	//public static final AABB DEST2X1 = new AABB(0, 0, SIZE * 2, SIZE);
	
	//Types
	//public static final int VOID = 0;
	public static final int SOLID = 1;
	public static final int H_UPGRADE = 2;
	public static final int A_UPGRADE = 3;
	public static final int DJ_UPGRADE = 4;
	
	//Spawn Types
	//public static final int NONE = 0;
	public static final int VIRUS = 1;
	public static final int MOTHBROWN = 2;
	
	public Sprite spr;
	protected int spawnType;
	private int type;
	
	public static Tile[] tiles = {
		new VoidTile(),
		new Block(),
		new FakeBlock(Sprite.normBlock),
		new HealthUpgrade(),
		new AmmoUpgrade(),
		new DoubleJumpUpgrade(),
		new Spawner(MOTHBROWN, Sprite.mothSpawner),
		new Spawner(VIRUS, Sprite.virusSpawner),
	};
	
	public Tile(int t, Sprite sprite) {
		type = t;
		spr = sprite;
	}
	
	public static boolean exists(int id) { return id >= 0 && id < tiles.length; }
	public static Tile getTile(int id) { return tiles[id]; }

	public boolean isSolid() { return type != 0; }
	public boolean isAUpgrade() { return type == A_UPGRADE; }
	public boolean isHUpgrade() { return type == H_UPGRADE; }
	public boolean isDJUpgrade() { return type == DJ_UPGRADE; }
	public boolean isInfected() { return spawnType != 0; }
	
	public int getSpawn() { return spawnType; }
}
