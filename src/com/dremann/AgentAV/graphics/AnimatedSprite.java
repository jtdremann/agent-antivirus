package com.dremann.AgentAV.graphics;

public class AnimatedSprite {
	
	public static final Sprite[] explosion = getSpriteArray(1, 1, SpriteSheet.explosion);
	
	public static final Sprite[] chargerVirus = getSpriteArray(2, 3, SpriteSheet.chargerVirus);
	public static final Sprite[] homingVirus = getSpriteArray(2, 2, SpriteSheet.homingVirus);
	public static final Sprite[] wormMinion = getSpriteArray(3, 1, SpriteSheet.wormMinion);
	public static final Sprite[] playerWalking = getSpriteArray(1, 3, SpriteSheet.playerWalking);
	public static final Sprite[] playerThrow = getSpriteArray(1, 1, SpriteSheet.playerThrow);
	public static final Sprite[] mothBlue = getSpriteArray(2, 2, SpriteSheet.mothBlue);
	public static final Sprite[] mothBrown = getSpriteArray(2, 2, SpriteSheet.mothBrown);
	public static final Sprite[] mothRed = getSpriteArray(2, 2, SpriteSheet.mothRed);
	
	public static final Sprite[] virusBoss = getSpriteArray(4, 4, SpriteSheet.virusBoss);
	public static final Sprite[] virusMinion = getSpriteArray(1, 1, SpriteSheet.virusMinion);
	public static final Sprite[] wormBoss = {
		Sprite.wormBossHeadUp,
		Sprite.wormBossHeadRight,
		Sprite.wormBossHeadDown,
		Sprite.wormBossHeadLeft,
	};
	public static final Sprite[] spiderBoss = getSpriteArray(16, 6, SpriteSheet.spiderBoss);
	
	private int frame = 0;
	private int rate = 1;
	private int length = 1;
	private int row = 0;
	private int rowLen = 1;
	private int counter = 0;
	
	private Sprite[] sprs;
	
	public AnimatedSprite(int r, int len, Sprite[] s) {
		rate = r;
		
		sprs = s;
		if(len > s.length || len <= 0)
			len = s.length;
		length = len;
		
		rowLen = s.length / len;
	}
	
	public void update() {
		if(rate == 0)
			return;
		counter++;
		if(rate > 0 && counter >= rate) {
			counter = 0;
			frame++;
			if(frame >= length)
				frame = 0;
		}
		else if(rate < 0 && counter >= rate * -1) {
			counter = 0;
			frame--;
			if(frame < 0)
				frame = length - 1;
		}
	}
	
	public Sprite getSprite() {
		return sprs[row * length + frame];
	}
	
	public void setRate(int r) {
		rate = r;
	}
	
	public void setFrame(int f) {
		frame = f;
	}
	
	public void setRow(int r) {
		if(r >= rowLen)
			r = 0;
		row = r;
	}
	
	public int getLength() { return length; }
	public int getRows() { return rowLen; }
	
	public static Sprite[] getSpriteArray(int tw, int th, SpriteSheet s) {
		int w = s.tWidth / tw;
		int h = s.tHeight / th;
		
		Sprite[] sprs = new Sprite[w * h];
		
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				sprs[y * w + x] = new Sprite(tw, th, s, (y * th * s.tWidth + x * tw), true);
			}
		}
		
		return sprs;
	}
	
	public int getFrame() { return frame; }
	public int getRate() { return rate; }
}
