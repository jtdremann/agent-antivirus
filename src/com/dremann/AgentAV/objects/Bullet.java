package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.level.tile.Tile;
import com.dremann.AgentAV.math.AABB;
import com.dremann.AgentAV.math.CollisionHelper;
import com.dremann.AgentAV.math.Vector;

public abstract class Bullet extends AttackBounded {
	
	private static float maxSpeed = 16;
	
	protected Vector velocity;
	//collision points
	
	public Bullet(int x, int y, int att, int kb, int t, Vector to, boolean friend, Sprite sprite, Level level) {
		super(x - 4, y - 4, att, kb, t, friend, new AABB(0, 0, 8, 8), sprite, level);
		
		if(!to.equals(position))
			velocity = to.subtract(position.add(4, 4)).normalize();
		else
			velocity = new Vector(1, 0);
		velocity = velocity.multiply(maxSpeed);
	}
	
	public Bullet(Vector pos, int att, int kb, int t, Vector to, boolean friend, Sprite sprite, Level level) {
		super(pos.subtract(4, 4), att, kb, t, friend, new AABB(0, 0, 8, 8), sprite, level);
		
		if(!to.equals(position))
			velocity = to.subtract(position.add(4, 4)).normalize();
		else
			velocity = new Vector(1, 0);
		velocity = velocity.multiply(maxSpeed);
	}
	
	public void update() {
		if(alive == false)
			return;
		
		checkCollisions(velocity);
	}
	
	protected void checkCollisions(final Vector velocity) {
		Vector to = position.add(velocity);
		boolean col = false;
		float frac = 1.0f;
		
		Vector tmin = Tile.BOUNDS.vecMin.subtract(bounds.vecMax);
		Vector tmax = Tile.BOUNDS.vecMax.subtract(bounds.vecMin);
		
		int tileX = 0;
		int tileY = 0;
		
		int tx = 0;
		int ty = 0;
		int ex = 0;
		int ey = 0;
		//Checks only tiles around player, based on direction
		if(velocity.y() >= 0) {
			ty = (int)position.add(bounds.vecMin).y() >> Tile.SHIFT;
			ey = (int)to.add(bounds.vecMax).y() - 1 >> Tile.SHIFT;
			ey++;
		}
		else {
			ty = (int)to.add(bounds.vecMin).y() >> Tile.SHIFT;
			ey = (int)position.add(bounds.vecMax).y() - 1 >> Tile.SHIFT;
			ey++;
		}
		if(velocity.x() >= 0) {
			tx = (int)position.add(bounds.vecMin).x() >> Tile.SHIFT;
			ex = (int)to.add(bounds.vecMax).x() - 1 >> Tile.SHIFT;
			ex++;
		}
		else {
			tx = (int)to.add(bounds.vecMin).x() >> Tile.SHIFT;
			ex = (int)position.add(bounds.vecMax).x() - 1 >> Tile.SHIFT;
			ex++;
		}
		
		for(int y = ty; y < ey; y++) {
			for(int x = tx; x < ex; x++) {
				Tile t = lvl.getTile(x, y);
				if(!t.isSolid())
					continue;
				
				Vector tPos = new Vector(x << Tile.SHIFT, y << Tile.SHIFT);
				AABB check = new AABB(tPos.add(tmin), tPos.add(tmax));
				CollisionHelper c = new CollisionHelper();
				
				if(AABB.lineAABBIntersection(check, position, to, c)) {
					col = true;
					if(frac > c.low) {
						tileX = x;
						tileY = y;
						frac = c.low;
					}
				}
			}
		}
		
		Entity ent = null;
		boolean eCol = false;
		
		for(int i = 0; i < lvl.entListLength(); i++) {
			Entity e = lvl.getEntity(i);
			if(friendly == e.friendly)
				continue;
			
			Vector bmin = e.bounds.vecMin.subtract(bounds.vecMax);
			Vector bmax = e.bounds.vecMax.subtract(bounds.vecMin);
			
			AABB check = new AABB(e.position.add(bmin), e.position.add(bmax));
			CollisionHelper c = new CollisionHelper();
			
			if(AABB.lineAABBIntersection(check, position, to, c)) {
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
			
			Vector bmin = p.bounds.vecMin.subtract(bounds.vecMax);
			Vector bmax = p.bounds.vecMax.subtract(bounds.vecMin);
			
			AABB check = new AABB(p.position.add(bmin), p.position.add(bmax));
			CollisionHelper c = new CollisionHelper();
			
			if(AABB.lineAABBIntersection(check, position, to, c)) {
				col = true;
				if(frac > c.low) {
					frac = c.low;
					ent = p;
					eCol = true;
				}
			}
		}
		
		Door door = null;
		boolean dCol = false;
		
		for(int i = 0; i < lvl.doorListLength(); i++) {
			Door d = lvl.getDoor(i);
			if(d.isOpen())
				continue;
			
			Vector bmin = Door.bounds.vecMin.subtract(bounds.vecMax);
			Vector bmax = Door.bounds.vecMax.subtract(bounds.vecMin);
			
			AABB check = new AABB(d.position.add(bmin), d.position.add(bmax));
			CollisionHelper c = new CollisionHelper();
			
			if(AABB.lineAABBIntersection(check, position, to, c)) {
				col = true;
				if(frac > c.low) {
					frac = c.low;
					door = d;
					eCol = false;
					dCol = true;
				}
			}
		}
		
		if(col) {
			to = position.add(velocity.multiply(frac));
			if(!eCol && !dCol)
				collidedTile(tileX, tileY);
			collided();
		}
		
		position = to;
		
		if(eCol)
			collidedEnt(ent);
		
		if(dCol)
			collidedDoor(door);
	}
	
	protected void collidedDoor(Door d) {
		d.collidedBullet(this);
	}
	
	protected void collidedTile(int x, int y) {
		
	}
	
	protected void collided() {
		alive = false;
	}
	
	//public String toString() {
	//	return "X: " + x + " | Y: " + y + " | ";
	//}
}
