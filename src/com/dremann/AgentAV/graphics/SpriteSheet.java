package com.dremann.AgentAV.graphics;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class SpriteSheet {
	
	public static final int TILE_SIZE = 16;
	
	protected int tWidth, tHeight;
	protected int width, height;
	public int[] pixels;
	
	public static SpriteSheet menuTitle = new SpriteSheet(690, 183, "/graphics/misc/MenuTitle.png", false);
	public static SpriteSheet pause = new SpriteSheet(540, 90, "/graphics/misc/Pause.png", false);
	public static SpriteSheet menuPlay = new SpriteSheet(192, 130, "/graphics/misc/MenuPlay.png", false);
	public static SpriteSheet menuCont = new SpriteSheet(192, 130, "/graphics/misc/MenuContinue.png", false);
	public static SpriteSheet menuQuit = new SpriteSheet(192, 130, "/graphics/misc/MenuQuit.png", false);
	
	public static SpriteSheet tiles = new SpriteSheet(8, 8, "/graphics/tiles/tiles.png", true);
	
	public static SpriteSheet HUD = new SpriteSheet(4, 2, "/graphics/misc/HUD.png", true);
	public static SpriteSheet misc8 = new SpriteSheet(32, 16, "/graphics/misc/8x8.png", false);
	public static SpriteSheet explosion = new SpriteSheet(5, 1, "/graphics/misc/Explosion.png", true);
	
	public static SpriteSheet text = new SpriteSheet(80, 64, "/graphics/misc/Text.png", false);
	
	public static SpriteSheet spaceship = new SpriteSheet(4, 4, "/graphics/sprites/SpaceShip.png", true);
	
	public static SpriteSheet homingVirus = new SpriteSheet(16, 4, "/graphics/sprites/HomingVirus.png", true);
	public static SpriteSheet chargerVirus = new SpriteSheet(16, 6, "/graphics/sprites/ChargerVirus.png", true);
	public static SpriteSheet wormMinion = new SpriteSheet(12, 2, "/graphics/sprites/WormMinion.png", true);
	public static SpriteSheet mothBlue = new SpriteSheet(8, 4, "/graphics/sprites/MothBlue.png", true);
	public static SpriteSheet mothBrown = new SpriteSheet(8, 4, "/graphics/sprites/MothBrown.png", true);
	public static SpriteSheet mothRed = new SpriteSheet(8, 4, "/graphics/sprites/MothRed.png", true);
	
	public static SpriteSheet virusBoss = new SpriteSheet(4, 16, "/graphics/sprites/VirusBoss.png", true);
	public static SpriteSheet virusMinion = new SpriteSheet(7, 1, "/graphics/sprites/VirusMinion.png", true);
	public static SpriteSheet wormBoss = new SpriteSheet(2, 4, "/graphics/sprites/WormBoss.png", true);
	public static SpriteSheet spiderBoss = new SpriteSheet(96, 12, "/graphics/sprites/Spider.png", true);
	
	public static SpriteSheet playerWalking = new SpriteSheet(12, 6, "/graphics/sprites/PlayerWalking.png", true);
	public static SpriteSheet playerThrow = new SpriteSheet(6, 2, "/graphics/sprites/PlayerArmThrow.png", true);
	public static SpriteSheet playerMisc = new SpriteSheet(2, 1, "/graphics/sprites/PlayerMisc.png", true);
	
	public void load(String path) {
		try {
			BufferedImage img = ImageIO.read(SpriteSheet.class.getResource(path));
			int w = img.getWidth();
			int h = img.getHeight();
			pixels = img.getRGB(0, 0, w, h, pixels, 0, w);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public SpriteSheet(int wide, int high, String path, boolean inTiles) {
		if(inTiles) {
			tWidth = wide;
			tHeight = high;
			width = tWidth * TILE_SIZE;
			height = tHeight * TILE_SIZE;
		}
		else {
			width = wide;
			height = high;
			tWidth = 0;
			tHeight = 0;
		}
		pixels = new int[width * height];
		
		load(path);
	}
	
	public SpriteSheet(int wide, int high, int[] pix) {
		tWidth = wide;
		tHeight = high;
		width = tWidth * TILE_SIZE;
		height = tHeight * TILE_SIZE;
		
		pixels = pix;
	}
}
