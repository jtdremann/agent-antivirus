package com.dremann.AgentAV.math;

public class AABB {
	
	public Vector vecMin, vecMax;
	
	public AABB(float xMin, float yMin, float xMax, float yMax) {
		vecMin = new Vector(xMin, yMin);
		vecMax = new Vector(xMax, yMax);
	}
	
	public AABB(Vector min, Vector max) {
		vecMin = min;
		vecMax = max;
	}
	
	private static boolean clipLine(int a, final AABB box, final Vector from, final Vector to, CollisionHelper c) { // a is axis
		float colLow, colHigh;
		
		colLow = (box.vecMin.xy[a] - from.xy[a]) / (to.xy[a] - from.xy[a]);
		colHigh = (box.vecMax.xy[a] - from.xy[a]) / (to.xy[a] - from.xy[a]);
		
		if(colLow > colHigh) { //swap
			float temp = colLow;
			colLow = colHigh;
			colHigh = temp;
		}
		
		if(colHigh < c.low)
			return false;
		if(colLow > c.high)
			return false;
		
		c.low = colLow > c.low ? colLow : c.low; //shrinks the collision ray from low side
		c.high = colHigh < c.high ? colHigh : c.high; // shrinks ray from high
		
		if(c.low > c.high)
			return false;
		
		return true;
	}
	
	public static boolean lineAABBIntersection(final AABB box, final Vector from, final Vector to, CollisionHelper c) {
		if(!clipLine(0, box, from, to, c))
			return false;
		if(!clipLine(1, box, from, to, c))
			return false;
		
		return true;
	}
	
	/*public static boolean pointAABBIntersection(final AABB box, final Vector point) {
		if(point.x() >= box.vecMin.x() && point.x() <= box.vecMax.x() && 
		   point.y() >= box.vecMin.y() && point.y() <= box.vecMax.y())
			return true;
		return false;
	}//*/
	
	public String toString() {
		return "Min: " + vecMin + " | Max: " + vecMax;
	}
	
}
