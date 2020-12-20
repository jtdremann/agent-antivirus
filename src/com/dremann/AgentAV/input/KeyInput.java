package com.dremann.AgentAV.input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener, FocusListener {
	
	private static boolean[] buttons = new boolean[128];
	public static boolean esc;
	public static boolean left;
	public static boolean right;
	public static boolean up;
	
	public void update() {
		up = buttons[KeyEvent.VK_W] || buttons[KeyEvent.VK_UP] || buttons[KeyEvent.VK_SPACE];
		left = buttons[KeyEvent.VK_A] || buttons[KeyEvent.VK_LEFT];
		right = buttons[KeyEvent.VK_D] || buttons[KeyEvent.VK_RIGHT];
		esc = buttons[KeyEvent.VK_ESCAPE];
	}
	
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if(code >= 0 && code < buttons.length)
			buttons[code] = true;
	}
	
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if(code >= 0 && code < buttons.length)
			buttons[code] = false;
	}
	
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void focusGained(FocusEvent e) {
		
	}
	
	public void focusLost(FocusEvent e) {
		for(int i = 0; i < buttons.length; i++)
			buttons[i] = false;
	}
	
}
