package com.github.dingledooper.spaceinvaders;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Game extends JPanel {

	private static final long serialVersionUID = -9073386402588264995L;

	public static final int WIDTH = 560, HEIGHT = 640;
	public static final int FPS = 60;
	public static final ImageIcon LOGO_ICON = new ImageIcon("res/logo.png");

	public int mysteryScoreTick = 0;
	public int[][] deathEffect = new int[Invaders.ROWS][Invaders.COLS];

	private int frame = 0, dir = 1;
	private int score = 0, level = 1;
	private int tick = 0, audioTick = 0, mysteryTick = 1;
	private int invadersY = 140;
	private int moveClip = 0;
	private String scoreString, levelString;
	
	private Bunker[] bunkers;
	private Cannon cannon;
	private CannonShot cannonShot;
	private Font spaceInvadersFont;
	private Invaders invaders;
	private Invader[][][] invadersArray;
	private List<InvaderShot> invaderShots;
	private Menu menu;
	private Mystery mystery;
	private ScheduledExecutorService executor;
	private Status status;

	public Game() {
		setFocusable(true);
		setDoubleBuffered(true);
		setBackground(Color.BLACK);
		
		new Window(this, "Space Invaders", WIDTH, HEIGHT);
		
		status = Status.MENU;
		
		try {
			spaceInvadersFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/spaceinvaders.ttf"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(spaceInvadersFont);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		invaders = new Invaders(this);
		invadersArray = invaders.getInvaders();
		invaderShots = new ArrayList<InvaderShot>();
		
		cannon = new Cannon();
		cannonShot = new CannonShot();
		addKeyListener(new KeyInput(cannon, cannonShot));
		
		bunkers = new Bunker[] {
			new Bunker(Bunker.BUNKER_POS[0].x, Bunker.BUNKER_POS[0].y),
			new Bunker(Bunker.BUNKER_POS[1].x, Bunker.BUNKER_POS[1].y),
			new Bunker(Bunker.BUNKER_POS[2].x, Bunker.BUNKER_POS[2].y),
			new Bunker(Bunker.BUNKER_POS[3].x, Bunker.BUNKER_POS[3].y)
		};
		
		mystery = new Mystery();

		menu = new Menu(this, cannon);
		addMouseListener(menu);
		
		Audio.load();
		
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				repaint();
			}
		}, 0, 1000 / FPS, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (cannon.isRemoved() && getStatus() == Status.PLAY) {
			setStatus(Status.GAME_OVER);
		}
		
		setScoreString(Integer.toString(getScore()));
		setLevelString(Integer.toString(getLevel()));
		
		if (getStatus() != Status.PLAY) {
			menu.render(g);
		} else {
			g.setColor(Color.WHITE);
			g.setFont(spaceInvadersFont.deriveFont(18F));
			g.drawString("Score: " + getScore(), 40, 40);
			g.drawString("Level: " + getLevel(), 370, 40);
			g.drawString("Lives: " + cannon.getLives(), 40, 600);
	
			for (Bunker b : bunkers) b.drawData(g);
			
			invaders.draw(this, g, frame);
			
			cannon.draw(g);
			cannon.move();

			
			if (!cannonShot.isRemoved()) {
				cannonShot.draw(g);
				cannonShot.checkCollisions(this, g, invaders, bunkers, mystery);
				cannonShot.move();
				
				if (cannonShot.getY() < 0) {
					cannonShot.setRemoved(true);
				}
			}

			if (Math.random() < 0.015) {
				Invader frontInvader = invaders.getRandomFrontInvader();
				InvaderShot invaderShot = new InvaderShot();
				
				invaderShot.setRemoved(false);
				invaderShot.setX(frontInvader.getX() + frontInvader.getWidth() / 2);
				invaderShot.setY(frontInvader.getY() + frontInvader.getHeight());
				
				invaderShots.add(invaderShot);
			}
			
			for (int i = 0; i < invaderShots.size(); i++) {
				if (invaderShots.get(i).isRemoved()) {
					invaderShots.remove(i);
				}
			}

			for (InvaderShot invaderShot : invaderShots) {
				if (!invaderShot.isRemoved()) {
					invaderShot.draw(g);
					invaderShot.checkCollisions(this, invaderShots, bunkers, g, cannon, cannonShot);
					invaderShot.move();
					
					if (invaders.getNumEnemies() == 0) {
						nextLevel();
						return;
					}
				}
			}

			if (audioTick++ % (invaders.getNumEnemies() + 5) == 0) {
				Audio.play(moveClip++ % 4 + 4);
			}
			
			if (tick++ % invaders.getNumEnemies() == 0) {
				frame ^= 1;
				
				int minX = Game.WIDTH, maxX = 0;
				for (int i = 0; i < Invaders.ROWS; i++) {
					for (int j = 0; j < Invaders.COLS; j++) {
						Invader invader0 = invadersArray[i][j][0], invader1 = invadersArray[i][j][1], invader2 = invadersArray[i][j][2];
						invader0.checkCollisions(g, cannon, bunkers);
						invader0.move(Invaders.SHIFT_X * dir, 0);
						invader1.move(Invaders.SHIFT_X * dir, 0);
						invader2.move(Invaders.SHIFT_X * dir, 0);
						
						minX = Math.min(minX, invader0.getX());
						maxX = Math.max(maxX, invader0.getX() + invader0.getWidth());
					}
				}
				
				if (minX < Invaders.MIN_X || maxX > Invaders.MAX_X) {
					dir *= -1;
					invaders.move(g, Invaders.SHIFT_X * dir, 0);
					invaders.move(g, 0, invaders.getShiftY());
				}
			}
			
			if (!mystery.isRemoved()) {
				mystery.draw(g);
				mystery.move();
			}
			
			if (mystery.isRemoved() && mysteryTick++ % Mystery.TICKS_UNTIL_MYSTERY == 0) {
				mystery.setRemoved(false);
				mystery.changeDirection();
			}
			
			if (mysteryScoreTick != 0) {
				mysteryScoreTick--;
				g.setColor(Color.WHITE);
				g.setFont(getSpaceInvadersFont().deriveFont(10F));
				g.drawString(Integer.toString(Mystery.POINTS), mystery.getX() + (mystery.getDirection() == 1 ? -30 : 30), mystery.getY());
			}
			
			Toolkit.getDefaultToolkit().sync();
			g.dispose();
		}
	}
	
	private void nextLevel() {
		invaders.setNumEnemies(55);
		cannon.setLives(cannon.getLives() + 1);
		setLevel(getLevel() + 1);
		
		if (getLevel() <= 5) {
			setInvadersY(getInvadersY() + 30);
			invaders.setShiftY(invaders.getShiftY() + 3);
		}
		
		bunkers = new Bunker[] {
			new Bunker(Bunker.BUNKER_POS[0].x, Bunker.BUNKER_POS[0].y),
			new Bunker(Bunker.BUNKER_POS[1].x, Bunker.BUNKER_POS[1].y),
			new Bunker(Bunker.BUNKER_POS[2].x, Bunker.BUNKER_POS[2].y),
			new Bunker(Bunker.BUNKER_POS[3].x, Bunker.BUNKER_POS[3].y)
		};
		
		cannonShot.setRemoved(true);
		
		for (int i = 0; i < Invaders.ROWS; i++) {
			for (int j = 0; j < Invaders.COLS; j++) {
				Invader[] invader = invadersArray[i][j];
				for (Invader inv : invader) {
					inv.setRemoved(false);
					inv.setX(Invaders.INVADERS_X + Invaders.GAP_X * j);
					inv.setY(getInvadersY() + Invaders.GAP_Y * i);
				}
			}
		}
		
		for (InvaderShot shot : invaderShots) {
			shot.setRemoved(true);
		}
		
		mystery.setRemoved(true);
		mysteryTick = 1;
	}
	
	public static int clamp(int val, int min, int max) {
		return val >= max ? max : val <= min ? min : val;
	}
	
	public int getScore() {
		return score;
	}
	
	public String getScoreString() {
		return scoreString;
	}
	
	public int getLevel() {
		return level;
	}
	
	public String getLevelString() {
		return levelString;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public int getInvadersY() {
		return invadersY;
	}
	
	public Font getSpaceInvadersFont() {
		return spaceInvadersFont;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void setScoreString(String scoreString) {
		this.scoreString = scoreString;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setLevelString(String levelString) {
		this.levelString = levelString;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public void setInvadersY(int invadersY) {
		this.invadersY = invadersY;
	}

}