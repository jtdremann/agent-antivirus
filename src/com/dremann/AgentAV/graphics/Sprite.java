package com.dremann.AgentAV.graphics;

public class Sprite {
	
	protected int width, height;
	public int[] pixels;
	//Spaceship
	public static Sprite spaceship = new Sprite(4, 4, SpriteSheet.spaceship, 0, true);
	//menu
	public static Sprite menuTitle = new Sprite(690, 183, SpriteSheet.menuTitle, 0, false);
	public static Sprite pause = new Sprite(540, 90, SpriteSheet.pause, 0, false);
	public static Sprite menuPlay = new Sprite(192, 65, SpriteSheet.menuPlay, 0, false);
	public static Sprite menuPlayHover = new Sprite(192, 65, SpriteSheet.menuPlay, 1, false);
	public static Sprite menuCont = new Sprite(192, 65, SpriteSheet.menuCont, 0, false);
	public static Sprite menuContHover = new Sprite(192, 65, SpriteSheet.menuCont, 1, false);
	public static Sprite menuQuit = new Sprite(192, 65, SpriteSheet.menuQuit, 0, false);
	public static Sprite menuQuitHover = new Sprite(192, 65, SpriteSheet.menuQuit, 1, false);
	//misc
	public static Sprite healthHUD = new Sprite(2, 2, SpriteSheet.HUD, 0, true);
	public static Sprite ammoHUD = new Sprite(2, 2, SpriteSheet.HUD, 2, true);
	public static Sprite normBullet = new Sprite(8, 8, SpriteSheet.misc8, 0, false);
	public static Sprite expBullet = new Sprite(8, 8, SpriteSheet.misc8, 1, false);
	//Blocks
	public static Sprite normBlock = new Sprite(2, 2, SpriteSheet.tiles, 0, true);
	public static Sprite infBlock = new Sprite(2, 2, SpriteSheet.tiles, 2, true);
	public static Sprite mothSpawner = new Sprite(2, 2, SpriteSheet.tiles, 4, true);
	public static Sprite virusSpawner = new Sprite(2, 2, SpriteSheet.tiles, 6, true);
	public static Sprite healthUp = new Sprite(2, 2, SpriteSheet.tiles, 16, true);
	public static Sprite ammoUp = new Sprite(2, 2, SpriteSheet.tiles, 18, true);
	public static Sprite djUpgrade = new Sprite(2, 2, SpriteSheet.tiles, 20, true);
	//Door
	public static Sprite doorNormOpen = new Sprite(2, 4, SpriteSheet.tiles, 32, true);
	public static Sprite doorInfOpen = new Sprite(2, 4, SpriteSheet.tiles, 34, true);
	public static Sprite doorNormClosed = new Sprite(2, 4, SpriteSheet.tiles, 36, true);
	public static Sprite doorInfClosed = new Sprite(2, 4, SpriteSheet.tiles, 38, true);
	//Packets
	public static Sprite smallHP = new Sprite(8, 8, SpriteSheet.misc8, 2, false);
	public static Sprite mediumHP = new Sprite(8, 8, SpriteSheet.misc8, 3, false);
	public static Sprite largeHP = new Sprite(8, 8, SpriteSheet.misc8, 4, false);
	public static Sprite smallAP = new Sprite(8, 8, SpriteSheet.misc8, 5, false);
	public static Sprite mediumAP = new Sprite(8, 8, SpriteSheet.misc8, 6, false);
	public static Sprite largeAP = new Sprite(8, 8, SpriteSheet.misc8, 7, false);
	//WormBoss
	public static Sprite wormBossHeadUp = new Sprite(2, 2, SpriteSheet.wormBoss, 0, true);
	public static Sprite wormBossHeadRight = rotate90(wormBossHeadUp);
	public static Sprite wormBossHeadDown = rotate180(wormBossHeadUp);
	public static Sprite wormBossHeadLeft = rotate270(wormBossHeadUp);
	public static Sprite wormBossBody = new Sprite(2, 2, SpriteSheet.wormBoss, 4, true);
	
	public static Sprite[] playerMisc = {
		new Sprite(1, 1, SpriteSheet.playerMisc, 0, true),
		new Sprite(1, 1, SpriteSheet.playerMisc, 1, true)
	};
	
	/*public static Sprite[] playerMisc = {
		new Sprite(1, 2, SpriteSheet.playerMisc, 0, true),
		new Sprite(1, 2, SpriteSheet.playerMisc, 6, true),
		new Sprite(1, 2, SpriteSheet.playerMisc, 1, true),
		new Sprite(1, 2, SpriteSheet.playerMisc, 7, true),
		new Sprite(1, 2, SpriteSheet.playerMisc, 2, true),
		new Sprite(1, 2, SpriteSheet.playerMisc, 8, true),
	};//*/
	
	public static Sprite[] text = {
		new Sprite(8, 16, SpriteSheet.text, 0, false),
		new Sprite(8, 16, SpriteSheet.text, 1, false),
		new Sprite(8, 16, SpriteSheet.text, 2, false),
		new Sprite(8, 16, SpriteSheet.text, 3, false),
		new Sprite(8, 16, SpriteSheet.text, 4, false),
		new Sprite(8, 16, SpriteSheet.text, 5, false),
		new Sprite(8, 16, SpriteSheet.text, 6, false),
		new Sprite(8, 16, SpriteSheet.text, 7, false),
		new Sprite(8, 16, SpriteSheet.text, 8, false),
		new Sprite(8, 16, SpriteSheet.text, 9, false),
		new Sprite(8, 16, SpriteSheet.text, 10, false),
		new Sprite(8, 16, SpriteSheet.text, 11, false),
		new Sprite(8, 16, SpriteSheet.text, 12, false),
		new Sprite(8, 16, SpriteSheet.text, 13, false),
		new Sprite(8, 16, SpriteSheet.text, 14, false),
		new Sprite(8, 16, SpriteSheet.text, 15, false),
		new Sprite(8, 16, SpriteSheet.text, 16, false),
		new Sprite(8, 16, SpriteSheet.text, 17, false),
		new Sprite(8, 16, SpriteSheet.text, 18, false),
		new Sprite(8, 16, SpriteSheet.text, 19, false),
		new Sprite(8, 16, SpriteSheet.text, 20, false),
		new Sprite(8, 16, SpriteSheet.text, 21, false),
		new Sprite(8, 16, SpriteSheet.text, 22, false),
		new Sprite(8, 16, SpriteSheet.text, 23, false),
		new Sprite(8, 16, SpriteSheet.text, 24, false),
		new Sprite(8, 16, SpriteSheet.text, 25, false),
		new Sprite(8, 16, SpriteSheet.text, 26, false),
		new Sprite(8, 16, SpriteSheet.text, 27, false),
		new Sprite(8, 16, SpriteSheet.text, 28, false),
		new Sprite(8, 16, SpriteSheet.text, 29, false),
		new Sprite(8, 16, SpriteSheet.text, 30, false),
		new Sprite(8, 16, SpriteSheet.text, 31, false),
		new Sprite(8, 16, SpriteSheet.text, 32, false),
		new Sprite(8, 16, SpriteSheet.text, 33, false),
		new Sprite(8, 16, SpriteSheet.text, 34, false),
		new Sprite(8, 16, SpriteSheet.text, 35, false),
		new Sprite(8, 16, SpriteSheet.text, 36, false),
		new Sprite(8, 16, SpriteSheet.text, 37, false),
		new Sprite(8, 16, SpriteSheet.text, 38, false),
		new Sprite(8, 16, SpriteSheet.text, 39, false),
	};//*/
	
	private void load(int xPos, int yPos, SpriteSheet s) {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				pixels[(y * width) + x] = s.pixels[(yPos + y) * s.width + xPos + x];
			}
		}
	}
	
	public Sprite(int wid, int hei, SpriteSheet s, int pos, boolean inTiles) {
		int xPos;
		int yPos;
		if(inTiles) {
			xPos = (pos % s.tWidth) * SpriteSheet.TILE_SIZE;
			yPos = (pos / s.tWidth) * SpriteSheet.TILE_SIZE;
			height = hei * SpriteSheet.TILE_SIZE;
			width = wid * SpriteSheet.TILE_SIZE;
		}
		else {
			xPos = (pos % (s.width / wid)) * wid;
			yPos = (pos / (s.width / wid)) * hei;
			height = hei;
			width = wid;
		}
		
		pixels = new int[width * height];
		load(xPos, yPos, s);
	}
	
	public Sprite(int w, int h, boolean inTiles, int col) {
		if(inTiles) {
			height = h * SpriteSheet.TILE_SIZE;
			width = w * SpriteSheet.TILE_SIZE;
		}
		else {
			width = w;
			height = h;
		}
		pixels = new int[width * height];
		
		for(int i = 0; i < pixels.length; i++)
			pixels[i] = col;
	}
	
	public Sprite(int w, int h, int[] pix) {
		width = w;
		height = h;
		pixels = pix;
	}
	
	public static Sprite rotate90(Sprite spr) {
		int w = spr.height;
		int h = spr.width;
		
		int[] pix = new int[w * h];
		
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				pix[y * w + x] = spr.pixels[(spr.height - x - 1) * spr.width + y];
			}
		}
		
		return new Sprite(w, h, pix);
	}
	
	public static Sprite rotate180(Sprite spr) {
		int w = spr.width;
		int h = spr.height;
		
		int[] pix = new int[w * h];
		
		for(int i = 0; i < pix.length; i++) {
			pix[i] = spr.pixels[spr.pixels.length - i - 1];
		}
		
		return new Sprite(w, h, pix);
	}
	
	public static Sprite rotate270(Sprite spr) {
		int w = spr.height;
		int h = spr.width;
		
		int[] pix = new int[w * h];
		
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				pix[y * w + x] = spr.pixels[x * spr.width + (spr.width - y - 1)];
			}
		}
		
		return new Sprite(w, h, pix);
	}
	
	public static Sprite flipHor(Sprite spr) {
		int w = spr.width;
		int h = spr.height;
		
		int[] pix = new int[w * h];
		
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				pix[y * w + x] = spr.pixels[y * w + (w - x - 1)];
			}
		}
		
		return new Sprite(w, h, pix);
	}
	
	public static Sprite flipVer(Sprite spr) {
		int w = spr.width;
		int h = spr.height;
		
		int[] pix = new int[w * h];
		
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				pix[y * w + x] = spr.pixels[(h - y - 1) * w + x];
			}
		}
		
		return new Sprite(w, h, pix);
	}
	
}
