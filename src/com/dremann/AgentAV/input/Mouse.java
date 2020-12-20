package com.dremann.AgentAV.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import com.dremann.AgentAV.Game;
import com.dremann.AgentAV.math.Vector;

public class Mouse implements MouseListener, MouseMotionListener {
	
	public static boolean left, right;
	public static float x, y;
	public static Vector position = new Vector();
	
	public void update(int xOff, int yOff) {
		position.set(x + xOff, y + yOff);
	}
	
	public void update() {
		position.set(x, y);
	}
	
	public void mouseMoved(MouseEvent e) {
		x = e.getX() / Game.SCALE;
		y = e.getY() / Game.SCALE;
	}
	
	public void mouseDragged(MouseEvent e) {
		x = e.getX() / Game.SCALE;
		y = e.getY() / Game.SCALE;
	}
	
	public void mousePressed(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e))
			left = true;
		if(SwingUtilities.isRightMouseButton(e))
			right = true;
	}
	
	public void mouseReleased(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e))
			left = false;
		if(SwingUtilities.isRightMouseButton(e))
			right = false;
	}
	
	public void mouseClicked(MouseEvent e) {
		
	}
	
	public void mouseEntered(MouseEvent e) {
		
	}
	
	public void mouseExited(MouseEvent e) {
		
	}

}
