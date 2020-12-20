package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.level.tile.Tile;
import com.dremann.AgentAV.math.AABB;
import com.dremann.AgentAV.math.CollisionHelper;
import com.dremann.AgentAV.math.Vector;

public class Web extends Attack {
	
	private static final int maxSpeed = 20;
	private static final int timeAlive = 90;
	
	protected Vector to, velocity;
	
	protected boolean collided;
	protected int timer;
	
	public Web(int x, int y, int ex, int ey, boolean friend, Level level) {
		super(x, y, 15, 0, STICKY, friend, level);
		to = new Vector(x, y);
		
		collided = false;
		timer = timeAlive;
		
		Vector go = new Vector(ex, ey);
		if(go.equals(position))
			velocity = new Vector(12, 0);
		else
			velocity = go.subtract(position).normalize().multiply(maxSpeed);
	}
	
	public Web(Vector pos, Vector towards, boolean friend, Level level) {
		super(pos, 15, 10, STICKY, friend, level);
		to = pos;
		
		collided = false;
		timer = timeAlive;
		
		if(towards.equals(position))
			velocity = new Vector(12, 0);
		else
			velocity = towards.subtract(position).normalize().multiply(maxSpeed);
	}
	
	public void update() {
		if(!collided)
			checkCollisions(to.add(velocity));
		else {
			timer--;
			if(timer <= 0) {
				alive = false;
				return;
			}
			checkCollisions();
		}
	}
	
	public void checkCollisions(Vector nextTo) {
		boolean col = false;
		float frac = 1.0f;
		
		int tx = 0;
		int ty = 0;
		int ex = 0;
		int ey = 0;
		//Checks only tiles around player, based on direction
		if(velocity.y() >= 0) {
			ty = (int)position.y() >> Tile.SHIFT;
			ey = (int)nextTo.y() - 1 >> Tile.SHIFT;
			ey++;
		}
		else {
			ty = (int)nextTo.y() >> Tile.SHIFT;
			ey = (int)position.y() - 1 >> Tile.SHIFT;
			ey++;
		}
		if(velocity.x() >= 0) {
			tx = (int)position.x() >> Tile.SHIFT;
			ex = (int)nextTo.x() - 1 >> Tile.SHIFT;
			ex++;
		}
		else {
			tx = (int)nextTo.x() >> Tile.SHIFT;
			ex = (int)position.x() - 1 >> Tile.SHIFT;
			ex++;
		}
		
		for(int y = ty; y < ey; y++) {
			for(int x = tx; x < ex; x++) {
				Tile t = lvl.getTile(x, y);
				if(!t.isSolid())
					continue;
				
				Vector tPos = new Vector(x << Tile.SHIFT, y << Tile.SHIFT);
				AABB check = new AABB(tPos.add(Tile.BOUNDS.vecMin), tPos.add(Tile.BOUNDS.vecMax));
				CollisionHelper c = new CollisionHelper();
				
				if(AABB.lineAABBIntersection(check, position, nextTo, c)) {
					col = true;
					if(frac > c.low)
						frac = c.low;
				}
			}
		}
		
		Entity ent = null;
		boolean eCol = false;
		
		for(int i = 0; i < lvl.entListLength(); i++) {
			Entity e = lvl.getEntity(i);
			if(friendly == e.friendly)
				continue;
			
			AABB check = new AABB(e.position.add(e.bounds.vecMin), e.position.add(e.bounds.vecMax));
			CollisionHelper c = new CollisionHelper();
			
			if(AABB.lineAABBIntersection(check, position, nextTo, c)) {
				col = true;
				if(frac > c.low) {
					frac = c.low;
					ent = e;
					eCol = true;
				}
			}
		}
		
		if(friendly == false) {
			Player p = lvl.getPlayer();
			
			AABB check = new AABB(p.position.add(p.bounds.vecMin), p.position.add(p.bounds.vecMax));
			CollisionHelper c = new CollisionHelper();
			
			if(AABB.lineAABBIntersection(check, position, nextTo, c)) {
				col = true;
				if(frac > c.low) {
					frac = c.low;
					ent = p;
					eCol = true;
				}
			}
		}
		
		if(col) {
			nextTo = position.add(nextTo.subtract(position).multiply(frac));
			collided();
		}
		
		to = nextTo;
		
		if(eCol)
			collidedEnt(ent);
	}
	
	public void checkCollisions() {
		Entity ent = null;
		boolean eCol = false;
		
		for(int i = 0; i < lvl.entListLength(); i++) {
			Entity e = lvl.getEntity(i);
			if(friendly == e.friendly)
				continue;
			
			AABB check = new AABB(e.position.add(e.bounds.vecMin), e.position.add(e.bounds.vecMax));
			CollisionHelper c = new CollisionHelper();
			
			if(AABB.lineAABBIntersection(check, position, to, c)) {
				ent = e;
				eCol = true;
			}
		}
		
		if(friendly == false) {
			Player p = lvl.getPlayer();
			
			AABB check = new AABB(p.position.add(p.bounds.vecMin), p.position.add(p.bounds.vecMax));
			CollisionHelper c = new CollisionHelper();
			
			if(AABB.lineAABBIntersection(check, position, to, c)) {
				ent = p;
				eCol = true;
			}
		}
		
		if(eCol)
			collidedEnt(ent);
	}
	
	private void collided() {
		collided = true;
	}
	
	@Override
	protected void collidedEnt(Entity e) {
		e.collidedAttack(this);
		alive = false;
	}
	
	public void render(Screen scr) {
		scr.drawLine((int)position.x(), (int)position.y(), (int)to.x(), (int)to.y(), 0xFFFFFF);
	}
	
}
