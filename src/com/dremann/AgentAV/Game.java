package com.dremann.AgentAV;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.dremann.AgentAV.graphics.Screen;
import com.dremann.AgentAV.graphics.Sprite;
import com.dremann.AgentAV.input.KeyInput;
import com.dremann.AgentAV.input.Mouse;
import com.dremann.AgentAV.level.Level;
import com.dremann.AgentAV.level.LevelLoader;
import com.dremann.AgentAV.objects.MenuContinue;
import com.dremann.AgentAV.objects.MenuPlayButton;
import com.dremann.AgentAV.objects.MenuQuitButton;

public class Game extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	public static final int MENU = 0;
	public static final int PLAY = 1;
	public static final int PAUSE = 2;
	public static final int ENDGAME = 3;
	public static int F_WID = 1024;
	public static int F_HEI = 780;
	public static float SCALE = 4f / 3f;
	public static int WIDTH = F_WID / 4 * 3;
	public static int HEIGHT = F_HEI / 4 * 3;
	
	private boolean running = false;
	private boolean escLastUp;
	private static int state = 0;
	private static int lastState = 0;
	private int ups = 60;
	
	private static Level lvl;
	
	//Menu
	private MenuPlayButton mpButton = new MenuPlayButton(96, 490, this);
	private MenuContinue mcButton = new MenuContinue(96, 490, this);
	private MenuQuitButton mqButton = new MenuQuitButton(480, 490, this);
	
	private BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	private JFrame frame;
	private KeyInput key;
	private Mouse mouse;
	private Screen screen;
	private String title = "Agent Antivirus";
	private Thread thread;
	
	public void update() {
		escLastUp = KeyInput.esc;
		key.update();
		
		if(state == PLAY) {
			if(KeyInput.esc && !escLastUp) {
				state = PAUSE;
				return;
			}
			
			mouse.update(screen.getXOff(), screen.getYOff());
			lvl.update();
			lvl.setOffsets(screen);
		}
		else if(state == PAUSE) {
			if(KeyInput.esc && !escLastUp) {
				state = PLAY;
				return;
			}
			
			mouse.update();
			mcButton.update(lastState == state);
			mqButton.update(lastState == state);
		}
		else if(state == MENU){
			if(KeyInput.esc && !escLastUp) {
				quitGame();
				return;
			}
			
			mouse.update();
			mpButton.update(lastState == state);
			mqButton.update(lastState == state);
		}
		else if(state == ENDGAME) {
			if(KeyInput.esc && !escLastUp) {
				state = MENU;
				return;
			}
			
			mouse.update();
			mcButton.update(lastState == state);
			mqButton.update(lastState == state);
		}
		
		if(!Mouse.left)
			lastState = state;
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.clear();
		if(state == PLAY)
			lvl.render(screen);
		else if(state == PAUSE) {
			screen.renderSprite(114, 30, Sprite.pause, true);
			mcButton.render(screen);
			mqButton.render(screen);
		}
		else if(state == MENU){
			screen.renderSprite(39, 30, Sprite.menuTitle, true);
			screen.drawCenteredText(228, "CREATED BY JACOB DREMANN");
			screen.drawCenteredText(259, "PRESS THE LEFT MOUSE BUTTON TO SHOOT");
			screen.drawCenteredText(290, "PRESS THE RIGHT MOUSE BUTTON TO SHOOT DELETER IF YOU HAVE AMMO");
			screen.drawCenteredText(321, "PRESS THE A AND D KEYS TO MOVE LEFT AND RIGHT");
			screen.drawCenteredText(352, "PRESS THE W KEY TO JUMP");
			screen.drawCenteredText(383, "PRESS THE ESCAPE KEY TO PAUSE");
			mpButton.render(screen);
			mqButton.render(screen);
		}
		else if(state == ENDGAME) {
			screen.drawCenteredText(60, "CONGRATULATIONS! YOU HAVE WON!");
			screen.drawCenteredText(84, "AGENT ANTIVIRUS WAS CREATED BY JACOB DREMANN");
			screen.drawCenteredText(284, getPercent() + "% OF UPGRADES COLLECTED");
			mcButton.render(screen);
			mqButton.render(screen);
		}
		
		for(int i = 0; i < pixels.length; i++)
			pixels[i] = screen.pixels[i];
		
		Graphics g = bs.getDrawGraphics();
		
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		
		g.dispose();
		bs.show();
	}
	
	public void run() {
		int frames = 0;
		int updates = 0;
		double toUPS = 1000000000.0 / ups;
		double count = 0;
		long prevTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		
		requestFocus();
		while(running) {
			long curTime = System.nanoTime();
			count += (curTime - prevTime) / toUPS;
			prevTime = curTime;
			
			while(count >= 1.0) {
				update();
				updates++;
				count--;
			}
			
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer >= 1000) {
				frame.setTitle(title + " | FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;
				timer += 1000;
			}
		}
		
		stop();
	}
	
	private synchronized void start() {
		running = true;
		thread = new Thread(this);
		thread.run();
	}
	
	private synchronized void stop() {
		running = false;
		
		try {
			thread.join();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		System.exit(0);
	}
	
	public void playGame() {
		ProgressTracker.reset();
		Level.resetStatics();
		
		loadLevel(0);
		
		state = PLAY;
	}
	
	public void quitGame() {
		stop();
	}
	
	public static void loadLevel(int id) {
		//LevelLoader.converter(id);
		lvl = LevelLoader.loadLevel(id);
	}
	
	public static int getState() { return state; }
	public static void setState(int s) { state = s; }
	
	public int getPercent() {
		int upsCol = ProgressTracker.ammoUpgrades + ProgressTracker.healthUpgrades;
		if(ProgressTracker.djCollected)
			upsCol++;
		return (int)((double)upsCol / ProgressTracker.MAXUPS * 100);
	}
	
	public Game() {
		frame = new JFrame(title);
		Dimension d = new Dimension(1024, 780);
		setPreferredSize(d);
		
		screen = new Screen(WIDTH, HEIGHT);
		key = new KeyInput();
		mouse = new Mouse();
		
		addKeyListener(key);
		addFocusListener(key);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		
		state = MENU;
	}
	
	public static void main(String args[]) {
		Game game = new Game();
		//if screen resolution is less than game resolution, mouse does not function as it should
		
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setResizable(false);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setVisible(true);
		game.start();
	}
	
}
