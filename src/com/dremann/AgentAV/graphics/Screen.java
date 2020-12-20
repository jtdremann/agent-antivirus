package com.dremann.AgentAV.graphics;

import com.dremann.AgentAV.ProgressTracker;

public class Screen {
	
	private int width, height, xOff, yOff;
	public int pixels[];
	
	public Screen(int w, int h) {
		width = w;
		height = h;
		pixels = new int[width * height];
		xOff = 0;
		yOff = 0;
	}
	
	public void renderSprite(int xPos, int yPos, Sprite spr, boolean fixed) {
		if(!fixed) {
			xPos -= xOff;
			yPos -= yOff;
		}
		
		for(int y = 0; y < spr.height; y++) {
			int yAbs = yPos + y;
			if(yAbs < 0 || yAbs >= height)
				continue;
			for(int x = 0; x < spr.width; x++) {
				int xAbs = xPos + x;
				if(xAbs < 0 || xAbs >= width)
					continue;
				
				int col = spr.pixels[y * spr.width + x];
				if(col != 0xFFFF00FF)
					pixels[yAbs * width + xAbs] = col;
			}
		}
	}
	
	public void drawHealth(int xP, int yP, int len, float per) {
		int sx = (int) xP - xOff;
		int sy = (int) yP - yOff;
		int ex = len + sx;
		int ey = sy + 4;
		
		for(int y = sy; y < ey; y++) {
			if(y < 0 || y >= height)
				continue;
			for(int x = sx; x < ex; x++) {
				if(x < 0 || x >= width)
					continue;
				
				int col = 0x00CC00;
				if(per < 0.25f)
					col = 0xCC0000;
				else if(per < 0.75f)
					col = 0xCCCC00;
				
				pixels[y * width + x] = col;
			}
		}
	}
	
	public void drawHUD(float per, int ammo, int hbarsFull) {
		//health
		drawRect(0, height - 32, width, 32, 2);
		
		int sx = 48; 
		int sy = height - 20;
		int ex = sx + (int)(128 * per);
		int ey = height - 12;
		
		renderSprite(sx - 32, height - 32, Sprite.healthHUD, true);
		
		for(int y = sy; y < ey; y++) {
			if(y < 0 || y >= height)
				continue;
			for(int x = sx; x < ex; x++) {
				if(x < 0 || x >= width)
					continue;
				
				if(per < 0.25f)
					pixels[y * width + x] = 0xCC0000;
				else if(per < 0.75f)
					pixels[y * width + x] = 0xCCCC00;
				else
					pixels[y * width + x] = 0x00CC00;
			}
		}
		
		for(int i = 0; i < ProgressTracker.healthUpgrades; i++) {
			for(int y = sy; y < ey; y++) {
				if(y < 0 || y >= height)
					continue;
				
				for(int x = 0; x < 8; x++) {
					int xPos = sx + (16 * i) + 136 + x;
					if(i < hbarsFull)
						pixels[y * width + xPos] = 0x00CC00;
					else
						pixels[y * width + xPos] = 0x3333AA;
				}
			}
		}
		
		if(ProgressTracker.ammoUpgrades > 0) {
			renderSprite(width - 80, height - 32, Sprite.ammoHUD, true);
			
			if(ammo > 99)
				ammo = 99;
			
			String am = Integer.toString(ammo);
			for(int i = 0; i < am.length(); i++)
				renderSprite(width - 47 + (i * 10), height - 24, Sprite.text[charToInt(am.charAt(i))], true);
		}
	}
	
	public void drawCountdown(int sec) {
		if(sec <= 0)
			return;
		
		if(sec > 99)
			sec = 99;
		
		int x = (width / 2) - 9;
		int y = height - 24;
		
		String s = Integer.toString(sec);
		if(sec < 10)
			s = "0" + s;
		
		for(int i = 0; i < s.length(); i++)
			renderSprite(x + (10 * i), y, Sprite.text[charToInt(s.charAt(i))], true);
	}
	
	public void drawBossHealth(float per) {
		//health
		int sx = 64; 
		int sy = height - 56;
		int ex = sx + (int)((width - sx * 2) * per);
		int ey = height - 40;
		
		for(int y = sy; y < ey; y++) {
			if(y < 0 || y >= height)
				continue;
			for(int x = sx; x < ex; x++) {
				if(x < 0 || x >= width)
					continue;
				
				int col = 0x00CC00;
				if(per < 0.25f)
					col = 0xCC0000;
				else if(per < 0.75f)
					col = 0xCCCC00;
				
				pixels[y * width + x] = col;
			}
		}
	}
	
	public void setOffsets(int x, int y) {
		xOff = x;
		yOff = y;
	}
	
	public void clear() {
		for(int i = 0; i < pixels.length; i++)
			pixels[i] = 0;
	}
	
	public int getXOff() { return xOff; }
	public int getYOff() { return yOff; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public void drawLine(int sx, int sy, int ex, int ey, int col) {
		//switch ponts
		if(ex < sx) {
			int tx = sx;
			sx = ex;
			ex = tx;
			int ty = sy;
			sy = ey;
			ey = ty;
		}
		else if(ex == sx) {
			if(sy > ey) {
				int ty = sy;
				sy = ey;
				ey = ty;
			}
			
			for(int i = sy; i < ey; i++)
				pixels[i * width + sx] = col;
		}
		else if(sy == ey) {
			for(int i = sx; i < ex; i++)
				pixels[sy * width + i] = col;
		}
		
		double slope = (double) (ey - sy) / (ex - sx);
		boolean neg = slope < 0;
		int y = 0;
		
		for(int i = 0; i < ex - sx; i++) {
			int xPos = sx + i;
			if(xPos >= width || xPos < 0)
				continue;
			
			boolean once = false;
			if(neg) {
				while(y > slope * (i + 1)) {
					once = true;
					int yPos = sy + y;
					if(!(yPos < 0 || yPos >= height))
						pixels[yPos * width + xPos] = col;
					y--;
				}
				if(!once) {
					int yPos = sy + y;
					if(!(yPos < 0 || yPos >= height))
						pixels[yPos * width + xPos] = col;
				}
			}
			else {
				while(y < slope * (i + 1)) {
					once = true;
					int yPos = sy + y;
					if(!(yPos < 0 || yPos >= height))
						pixels[yPos * width + xPos] = col;
					y++;
				}
				if(!once) {
					int yPos = sy + y;
					if(!(yPos < 0 || yPos >= height))
						pixels[yPos * width + xPos] = col;
				}
			}
		}
	}//*/
	
	public void drawRect(int x, int y, int w, int h, int thickness) {
		for(int yy = 0; yy < h; yy++) {
			int yPos = yy + y;
			if(yPos < 0 || yPos >= height)
				continue;
			for(int xx = 0; xx < w; xx++) {
				int xPos = xx + x;
				if(xPos < 0 || xPos >= width)
					continue;
				if(yy < thickness || yy >= h - thickness || xx < thickness || xx >= w - thickness)
					pixels[yPos * width + xPos] = 0xBFBFBF;
				else
					pixels[yPos * width + xPos] = 0;
			}
		}
	}
	
	public void drawMessage(int x, int y, int w, int h, String mess) {
		int[] rowLen = new int[4];
		
		int total = 0;
		for(int i = 0; i < rowLen.length; i++) {
			rowLen[i] = findLastSpace(total, total + 22, mess) - total;
			total += rowLen[i];
		}
		
		int numRows = 0;
		if(rowLen[0] > 0)
			numRows++;
		for(int i = 1; i < rowLen.length; i++) {
			if(rowLen[i] > 0)
				numRows++;
		}
		if(numRows == 0)
			return;
		
		drawRect(x, y, w, h, 2);
		
		int hX = x + (w / 2);
		int hY = y + (h / 2);
		
		int charPos = 0;
		for(int i = 0; i < numRows; i++) {
			int rowStartX = hX - ((rowLen[i] * 10 - 2) / 2);
			int rowStartY = hY - ((numRows - 1) * 10 + 8); //8 then 18 then 28 then 38
			
			int j = 0;
			while(j < rowLen[i]) {
				renderSprite(rowStartX + (j * 10), rowStartY + (i * 20), Sprite.text[charToInt(mess.charAt(charPos))], true);
				j++;
				charPos++;
			}
		}
	}
	
	private int findLastSpace(int start, int max, String mess) {
		if(start >= mess.length())
			return start;
		
		if(mess.length() > max) {
			int rLen = max;
			while(rLen >= start) {
				if(mess.charAt(rLen) == ' ')
					break;
				rLen--;
			}
			if(rLen <= start)
				return max;
			
			return rLen;
		}
		else {
			return mess.length();
		}
	}
	
	public void drawCenteredText(int y, String m) {
		int wid = m.length() * 10 - 2;
		int x = (width / 2) - (wid / 2);
		
		for(int i = 0; i < m.length(); i++) {
			renderSprite(x + (i * 10), y, Sprite.text[charToInt(m.charAt(i))], true);
		}
	}
	
	private static int charToInt(char c) {
		switch(c) {
		case ' ':
			return 0;
		case 'A':
			return 1;
		case 'B':
			return 2;
		case 'C':
			return 3;
		case 'D':
			return 4;
		case 'E':
			return 5;
		case 'F':
			return 6;
		case 'G':
			return 7;
		case 'H':
			return 8;
		case 'I':
			return 9;
		case 'J':
			return 10;
		case 'K':
			return 11;
		case 'L':
			return 12;
		case 'M':
			return 13;
		case 'N':
			return 14;
		case 'O':
			return 15;
		case 'P':
			return 16;
		case 'Q':
			return 17;
		case 'R':
			return 18;
		case 'S':
			return 19;
		case 'T':
			return 20;
		case 'U':
			return 21;
		case 'V':
			return 22;
		case 'W':
			return 23;
		case 'X':
			return 24;
		case 'Y':
			return 25;
		case 'Z':
			return 26;
		case '0':
			return 27;
		case '1':
			return 28;
		case '2':
			return 29;
		case '3':
			return 30;
		case '4':
			return 31;
		case '5':
			return 32;
		case '6':
			return 33;
		case '7':
			return 34;
		case '8':
			return 35;
		case '9':
			return 36;
		case '.':
			return 37;
		case '!':
			return 38;
		case '%':
			return 39;
		default:
			return 38;
		}
	}
}
