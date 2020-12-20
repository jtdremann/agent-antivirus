package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.Game;
import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.graphics.Sprite;

public class MenuQuitButton extends MenuButton {

	public MenuQuitButton(int x, int y, Game g) {
		super(x, y, g);
	}
	
	protected void press() {
		game.quitGame();
	}
	
	public void render(Screen scr) {
		if(!hover)
			scr.renderSprite((int) position.x(), (int) position.y(), Sprite.menuQuit, true);
		else
			scr.renderSprite((int) position.x(), (int) position.y(), Sprite.menuQuitHover, true);
	}
}
