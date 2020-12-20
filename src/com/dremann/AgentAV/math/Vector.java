package com.dremann.AgentAV.math;

public class Vector {
	
	public float[] xy;
	
	public Vector(float x, float y) {
		xy = new float[2];
		xy[0] = x;
		xy[1] = y;
	}
	
	public Vector() {
		xy = new float[2];
		xy[0] = 0.0f;
		xy[1] = 0.0f;
	}
	
	public Vector(Vector v) {
		xy = new float[2];
		xy[0] = v.xy[0];
		xy[1] = v.xy[1];
	}
	
	public void set(float x, float y) {
		xy[0] = x;
		xy[1] = y;
	}
	
	public void set(Vector v) {
		xy[0] = v.xy[0];
		xy[1] = v.xy[1];
	}

	public float x() {
		return xy[0];
	}
	
	public float y() {
		return xy[1];
	}
	
	public Vector add(Vector v) {
		return new Vector(xy[0] + v.xy[0], xy[1] + v.xy[1]);
	}
	
	public Vector subtract(Vector v) {
		return new Vector(xy[0] - v.xy[0], xy[1] - v.xy[1]);
	}
	
	public Vector add(float x, float y) {
		return new Vector(xy[0] + x, xy[1] + y);
	}
	
	public Vector subtract(float x, float y) {
		return new Vector(xy[0] - x, xy[1] - y);
	}
	
	public Vector normalize() {
		return new Vector(xy[0], xy[1]).divide(length());
	}
	
	public Vector multiply(float n) {
		return new Vector(xy[0] * n, xy[1] * n);
	}
	
	public Vector divide(float n) {
		return new Vector(xy[0] / n, xy[1] / n);
	}
	
	public float length() {
		return (float) java.lang.Math.sqrt(xy[0]*xy[0] + xy[1]*xy[1]);
	}
	
	public float lengthSqr() { //used for faster comparisons
		return xy[0]*xy[0] + xy[1]*xy[1];
	}
	
	public static float dotProduct(Vector a, Vector b) {
		return a.xy[0] * b.xy[0] + a.xy[1] * b.xy[1];
	}
	
	public String toString() {
		return "<" + xy[0] + ", " + xy[1] + ">";
	}
	
	public boolean equals(Vector v) {
		return xy[0] == v.xy[0] && xy[1] == v.xy[1];
	}
	
	public boolean equals(float x, float y) {
		return xy[0] == x && xy[1] == y;
	}
}
