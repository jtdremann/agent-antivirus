package com.dremann.AgentAV.graphics;

public class Message {
	
	//CHAR_LIMIT = 88;
	
	private int time, length;
	private boolean show;
	private String message;
	
	public Message(int l, String mess) {
		message = mess.toUpperCase();
		time = 0;
		length = l;
		show = true;
		
		if(message.equals(""))
			destroy();
	}
	
	public void update() {
		time++;
		if(time > length)
			destroy();
	}
	
	private void destroy() {
		show = false;
	}
	
	public boolean isShown() { return show; }
	
	public void render(Screen scr) {
		if(show)
			scr.drawMessage(scr.getWidth() / 2 - 128, scr.getHeight() - 128, 256, 96, message);
	}
	
}
