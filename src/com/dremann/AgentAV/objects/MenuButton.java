package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.Game;
import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.input.Mouse;
import com.dremann.AgentAV.math.Vector;

public abstract class MenuButton {
	
	protected Vector position, bounds;
	protected Game game;
	protected boolean hover;
	
	public MenuButton(int x, int y, Game g) {
		position = new Vector(x, y);
		bounds = new Vector(192, 65);
		game = g;
	}
	
	public void update(boolean canClick) {
		hover = (Mouse.position.x() >= position.x() && Mouse.position.x() < position.x() + bounds.x() &&
				 Mouse.position.y() >= position.y() && Mouse.position.y() < position.y() + bounds.y());
		
		if(hover && Mouse.left && canClick) {
			press();
		}
	}
	
	protected abstract void press();
	public abstract void render(Screen scr);
	
}
