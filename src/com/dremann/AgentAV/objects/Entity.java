package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.level.tile.Tile;
import com.dremann.AgentAV.math.AABB;
import com.dremann.AgentAV.math.CollisionHelper;
import com.dremann.AgentAV.math.Vector;

public abstract class Entity {
	
	protected boolean alive;
	protected boolean player, friendly, tileCol;//tileCol: if it is solid with tiles
	protected int attack, knockback, tCD;; //tCD is time able to be touched again
	protected Level lvl;
	protected Sprite spr;
	
	protected Vector position, velocity;
	protected AABB bounds;
	//collision points
	protected Vector tl, tr, bl, br;
	
	public Entity(int x, int y, int xBmin, int yBmin, int xBmax, int yBmax, boolean friend, Sprite s, Level level) {
		position = new Vector(x, y);
		velocity = new Vector();
		bounds = new AABB(xBmin, yBmin, xBmax, yBmax);
		//init col points
		updateColPoints();
		spr = s;
		friendly = friend;
		player = false;
		alive = true;
		tileCol = true;
		lvl = level;
	}
	
	public void update() {
		if(alive == false)
			return;
		
		if(tCD >= 0)
			tCD--;
		
		if(tileCol) {
			boolean colB = false;
			boolean colT = false;
			boolean colL = false;
			boolean colR = false;
			boolean collided = false;
			if(checkColPoints(bl.add(1, 0), br.add(-1, 0))) {
				colB = true;
				if(velocity.y() >= 0){
					collidedVer();
					collided = true;
				}
			}
			if(checkColPoints(tl.add(1, 0), tr.add(-1, 0))) {
				colT = true;
				if(velocity.y() <= 0) {
					collidedVer();
					collided = true;
				}
			}
			if(checkColPoints(tl.add(0, 1), bl.add(0, -1))) {
				colL = true;
				if(velocity.x() <= 0) {
					collidedHor();
					collided = true;
				}
			}
			if(checkColPoints(tr.add(0, 1), br.add(0, -1))) {
				colR = true;
				if(velocity.x() >= 0) {
					collidedHor();
					collided = true;
				}
			}
			
			if(!colR && !collided) {
				//check bottom first for grav-ents
				if(!colB && checkColPoints(br, br))
					collidedCor(br);
				else if(!colT && checkColPoints(tr, tr))
					collidedCor(tr);
			}
			
			if(!colL && !collided) {
				if(!colB && checkColPoints(bl, bl))
					collidedCor(bl);
				else if(!colT && checkColPoints(tl, tl))
					collidedCor(tl);
			}
		}
		
		checkCollisions(velocity);
		
		updateColPoints();
	}
	
	protected void updateColPoints() {
		//corners
		tl = position.add(bounds.vecMin).add(-1, -1);
		tr = position.add(bounds.vecMax.x(), bounds.vecMin.y() - 1);
		bl = position.add(bounds.vecMin.x() - 1, bounds.vecMax.y());
		br = position.add(bounds.vecMax);
	}
	
	protected boolean checkColPoints(Vector p1, Vector p2) {
		int sx = (int)p1.x() >> Tile.SHIFT;
		int sy = (int)p1.y() >> Tile.SHIFT;
		int ex = (int)p2.x() >> Tile.SHIFT;
		int ey = (int)p2.y() >> Tile.SHIFT;
		
		for(int y = sy; y <= ey; y++) {
			for(int x = sx; x <= ex; x++) {
				if(!canPass(lvl.getTile(x, y))) {
					lvl.tileCollision(x, y, this);
					return true;
				}
			}
		}
		
		return false;
	}
	
	protected void checkCollisions(final Vector velocity) {
		Vector to = position.add(velocity);
		boolean col = false;
		float frac = 1.0f;
		
		if(tileCol) {
			Vector tmin = Tile.BOUNDS.vecMin.subtract(bounds.vecMax);
			Vector tmax = Tile.BOUNDS.vecMax.subtract(bounds.vecMin);
			
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
					if(canPass(t))
						continue;
					
					Vector tPos = new Vector(x << Tile.SHIFT, y << Tile.SHIFT);
					AABB check = new AABB(tPos.add(tmin), tPos.add(tmax));
					CollisionHelper c = new CollisionHelper();
					
					if(AABB.lineAABBIntersection(check, position, to, c)) {
						col = true;
						if(frac > c.low)
							frac = c.low;
					}
				}
			}
		}
		
		Entity ent = null;
		boolean eCol = false;
		
		if(tCD < 0) {
			for(int i = 0; i < lvl.entListLength(); i++) {
				Entity e = lvl.getEntity(i);
				if(e == this || friendly == e.friendly)
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
		}
		
		if(col)
			to = position.add(velocity.multiply(frac));
		
		position = to;
		
		if(eCol) {
			collidedEnt(ent);
			ent.collidedEnt(this);
		}
	}
	
	private boolean canPass(Tile t) {
		return !t.isSolid() || (!friendly && t.isInfected());
	}
	
	protected void destroy() {
		alive = false;
	}
	
	public void render(Screen scr) {
		if(tCD < 0 || tCD / 2 % 2 == 1)
			scr.renderSprite((int)position.x(), (int)position.y(), spr, false);
		//Render collision points
		/*
		scr.renderSprite((int)tl.x(), (int)tl.y(), new Sprite(1, 1, false, 0xAAAA00), false);
		scr.renderSprite((int)tr.x(), (int)tr.y(), new Sprite(1, 1, false, 0xAAAA00), false);
		scr.renderSprite((int)bl.x(), (int)bl.y(), new Sprite(1, 1, false, 0xAAAA00), false);
		scr.renderSprite((int)br.x(), (int)br.y(), new Sprite(1, 1, false, 0xAAAA00), false);//*/
		//Render bound box
		/*
		for(int x = (int)bounds.vecMin.x(); x < (int)bounds.vecMax.x(); x++) {
			scr.renderSprite((int)position.x() + x, (int)position.y() + (int)bounds.vecMin.y(), new Sprite(1, 1, false,0xFF0000), false);
			scr.renderSprite((int)position.x() + x, (int)position.y() + (int)bounds.vecMax.y() - 1, new Sprite(1, 1, false,0xFF0000), false);
		}
		for(int y = (int)bounds.vecMin.y(); y < (int)bounds.vecMax.y(); y++) {
			scr.renderSprite((int)position.x() + (int)bounds.vecMin.x(), (int)position.y() + y, new Sprite(1, 1, false,0xFF0000), false);
			scr.renderSprite((int)position.x() + (int)bounds.vecMax.x() - 1, (int)position.y() + y, new Sprite(1, 1, false,0xFF0000), false);
		}
		//*/
	}
	
	protected abstract void collidedEnt(Entity e);
	protected abstract void collidedAttack(Attack a);
	
	protected void collidedVer() {
		velocity.set(velocity.x(), 0);
	}
	
	protected void collidedHor() {
		velocity.set(0, velocity.y());
	}
	
	protected void collidedCor(Vector entCor) {
		velocity.set(velocity.x(), 0);
	}
	
	public int getCenterX() { 
		return (int)(position.x() + (bounds.vecMax.x() + bounds.vecMin.x()) / 2);
	}

	public int getCenterY() { 
		return (int)(position.y() + (bounds.vecMax.y() + bounds.vecMin.y()) / 2);
		//return (int)(position.y() + bounds.vecMin.y() + (bounds.vecMax.y() - bounds.vecMin.y()) / 2);
	}
	
	public int getX() { return (int)position.x(); }
	public int getY() { return (int)position.y(); }
	public boolean isAlive() { return alive; }
	public boolean isPlayer() { return player; }
}
