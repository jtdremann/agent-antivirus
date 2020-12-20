package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.Game;
import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.graphics.Sprite;

public class MenuPlayButton extends MenuButton {
	
	public MenuPlayButton(int x, int y, Game g) {
		super(x, y, g);
	}
	
	protected void press() {
		game.playGame();
	}
	
	public void render(Screen scr) {
		if(!hover)
			scr.renderSprite((int) position.x(), (int) position.y(), Sprite.menuPlay, true);
		else
			scr.renderSprite((int) position.x(), (int) position.y(), Sprite.menuPlayHover, true);
	}
}
