package com.dremann.AgentAV.math;

/**
 * 
 * Various math bits that don't fit in other classes.
 *
 */
public class Math {
	
	//Gradually approaches a speed
	public static float approachVel(float cur, float goal, float change) {
		float difference = goal - cur; // how far away is our goal?
		
		if(difference > change) // if we can add change and not exceed our goal
			return cur + change;
		if(difference < -change)
			return cur - change; // if we can subtract change and not exceed our goal (for opp direction)
		
		return goal; // we would've exceeded our goal with the change
	}
	
}
