package com.dremann.AgentAV.level;

import com.dremann.AgentAV.math.Vector;

public class Camera {
	
	private Vector position;
	
	public Camera(int x, int y) {
		position = new Vector(x, y);
	}
	
	public Camera() {
		position = new Vector();
	}
	
	public void update(int x, int y) {
		position.set(x, y);
	}
	
	public int getX() { return (int)position.x(); }
	public int getY() { return (int)position.y(); }
}
