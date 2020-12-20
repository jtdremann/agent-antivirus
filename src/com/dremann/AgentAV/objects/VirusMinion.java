package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.ProgressTracker;
import com.dremann.AgentAV.graphics.AnimatedSprite;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.math.Vector;

public class VirusMinion extends Mob {
	
	private static final int hbx = 8;
	private static final int hby = 8;
	
	protected VirusBoss par;
	
	private int degChange, distFromPar;
	
	public VirusMinion(int x, int y, int posi, VirusBoss parent, Level level) {
		super(x, y, 2, 2, 14, 16, 25, false, new AnimatedSprite(4, 7, AnimatedSprite.virusMinion), level);
		tileCol = false;
		par = parent;
		
		degChange = posi * (360 / VirusBoss.MINION_MAX);
	}
	
	public void update() {
		if(par.destMin) {
			par.destMin();
			alive = false;
			return;
		}
		if(!par.alive) {
			parDestroy();
			return;
		}
		
		double ang = (par.minionCount * VirusBoss.DEG_PER_COUNT + degChange) % 360;
		ang *= Math.PI / 180;
		
		if(par.shielding) {
			if(distFromPar < par.minDist)
				distFromPar += 2;
			else if(distFromPar > par.minDist)
				distFromPar -= 2;
		}
		else
			distFromPar += 2;
		
		Vector to = new Vector((float) Math.cos(ang), (float) Math.sin(ang)).multiply(distFromPar);
		
		position = to.add(par.getCenterX() - hbx, par.getCenterY() - hby);
		
		super.update();
	}
	
	protected void destroy() {
		alive = false;
		int r = Level.randNextInt(250);
		if(r < 10)
			lvl.addPacket(new SmallHP(getCenterX(), getCenterY(), lvl));
		else if(r < 15)
			lvl.addPacket(new MediumHP(getCenterX(), getCenterY(), lvl));
		else if(r < 25 && ProgressTracker.ammoUpgrades > 0)
			lvl.addPacket(new SmallAP(getCenterX(), getCenterY(), lvl));
		else if(r < 30 && ProgressTracker.ammoUpgrades > 0)
			lvl.addPacket(new MediumAP(getCenterX(), getCenterY(), lvl));
		par.destMin();
	}
	
	protected void parDestroy() {
		alive = false;
		lvl.addPacket(new MediumHP(getCenterX(), getCenterY(), lvl));
		par.destMin();
	}
	
	@Override
	protected void collidedAttack(Attack a) {
		if(tCD >= 0)
			return;
		health -= a.attack;
		tCD = 30;
	}
	
}
