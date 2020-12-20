package com.dremann.AgentAV.objects;

import com.dremann.AgentAV.Game;
import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.graphics.Sprite;

public class MenuContinue extends MenuButton {
	
	public MenuContinue(int x, int y, Game g) {
		super(x, y, g);
	}
	
	protected void press() {
		if(Game.getState() == Game.PAUSE)
			Game.setState(Game.PLAY);
		else if(Game.getState() == Game.ENDGAME)
			Game.setState(Game.MENU);
	}
	
	public void render(Screen scr) {
		if(!hover)
			scr.renderSprite((int) position.x(), (int) position.y(), Sprite.menuCont, true);
		else
			scr.renderSprite((int) position.x(), (int) position.y(), Sprite.menuContHover, true);
	}
}
