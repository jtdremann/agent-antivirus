package com.dremann.AgentAV;

public class ProgressTracker {
	
	public static final int MAXUPS = 15;
	public static final int USB = 0;
	public static final int HUB = 4;
	public static final int HD = 5;
	public static final int HD_BOSS = 8;
	public static final int RAM = 11;
	public static final int RAM_BOSS = 14;
	public static final int WC = 17;
	public static final int CS = 21;
	
	public static int ammoUpgrades;
	public static int healthUpgrades;
	public static boolean seenUSBMess = false;
	public static boolean reachedUSB = false;
	public static boolean reachedHUB = false;
	public static boolean reachedWC = false;
	public static boolean retWithAmmo = false;
	public static boolean retWithDJ = false;
	public static boolean clearedUSB = false;
	public static boolean killedWormBoss = false;
	public static boolean killedVirusBoss = false;
	
	private static int numLevels = 25;
	
	//Ammo flags
	public static boolean[] ammoCollected = new boolean[numLevels];
	
	//Health flags
	public static boolean[] healthCollected = new boolean[numLevels];
	
	//Special upgrade flags
	public static boolean djCollected;
	
	public static void reset() {
		ammoUpgrades = 0;
		healthUpgrades = 0;
		reachedUSB = false;
		reachedHUB = false;
		reachedWC = false;
		retWithAmmo = false;
		retWithDJ = false;
		clearedUSB = false;
		killedWormBoss = false;
		killedVirusBoss = false;
		djCollected = false;
		for(int i = 0; i < numLevels; i++) {
			ammoCollected[i] = false;
			healthCollected[i] = false;
		}
	}
}
