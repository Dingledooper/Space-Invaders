package com.github.dingledooper.spaceinvaders;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Invaders {
	
	public static final String[] OCTO_IMG =  new String[] {"res/octo1.png", "res/octo2.png", "res/octodeath.png"};
	public static final String[] CRAB_IMG =  new String[] {"res/crab1.png", "res/crab2.png", "res/crabdeath.png"};
	public static final String[] SQUID_IMG = new String[] {"res/squid1.png", "res/squid2.png", "res/squiddeath.png"};
	
	public static final int ROWS = 5, COLS = 11;
	public static final int INVADERS_X = 38;
	public static final int GAP_X = 45, GAP_Y = 40;
	public static final int MIN_X = 0, MAX_X = 560, MAX_Y = 600;
	public static final int SHIFT_X = 5;
	public static final int SHIFTS_UNTIL_MOVE_DOWN = 5;
	public static final int DEATH_EFFECT_TICKS = 10;
			
	public static final int SCALE = 3;
	public static final int OCTO_WIDTH = 12 * SCALE, OCTO_HEIGHT = 8 * SCALE;
	public static final int CRAB_WIDTH = 11 * SCALE, CRAB_HEIGHT = 8 * SCALE;
	public static final int SQUID_WIDTH = 8 * SCALE, SQUID_HEIGHT = 8 * SCALE;

	private int shiftY = 20;
	private int numEnemies = ROWS * COLS;
	private Game game;
	private Random r;
	private Invader[][][] invaders = new Invader[ROWS][COLS][3];

	public Invaders(Game game) {
		this.game = game;
		r = new Random();
		
		initRow(Invaders.SQUID_IMG, 0, SQUID_WIDTH, SQUID_HEIGHT);
		initRow(Invaders.CRAB_IMG, 1, CRAB_WIDTH, CRAB_HEIGHT);
		initRow(Invaders.CRAB_IMG, 2, CRAB_WIDTH, CRAB_HEIGHT);
		initRow(Invaders.OCTO_IMG, 3, OCTO_WIDTH, OCTO_HEIGHT);
		initRow(Invaders.OCTO_IMG, 4, OCTO_WIDTH, OCTO_HEIGHT);
	}
	
	public void draw(Game game, Graphics g, int frame) {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				Invader[] invader = invaders[i][j];
				if (!invader[frame].isRemoved()) {
					invader[frame].draw(g);
				}
				
				if (game.deathEffect[i][j] > 0) {
					g.drawImage(invader[2].getImage(), invader[2].getX(), invader[2].getY(), invader[2].getWidth(), invader[2].getHeight(), null);
					game.deathEffect[i][j]--;
				}
			}
		}
	}
	
	public void move(Graphics g, int x, int y) {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				Invader[] invader = invaders[i][j];
				if (!invader[0].isRemoved()) {
					invader[0].setX(invader[0].getX() + x);
					invader[0].setY(invader[0].getY() + y);
					invader[1].setX(invader[1].getX() + x);
					invader[1].setY(invader[1].getY() + y);
					invader[2].setX(invader[2].getX() + x);
					invader[2].setY(invader[2].getY() + y);
				}
			}
		}
	}
	
	private void initRow(String[] type, int row, int width, int height) {
		for (int col = 0; col < COLS; col++) {
			invaders[row][col][0] = new Invader(type[0], INVADERS_X + GAP_X * col, game.getInvadersY() + GAP_Y * row, width, height);
			invaders[row][col][1] = new Invader(type[1], INVADERS_X + GAP_X * col, game.getInvadersY() + GAP_Y * row, width, height);
			invaders[row][col][2] = new Invader(type[2], INVADERS_X + GAP_X * col, game.getInvadersY() + GAP_Y * row, width, height);
		}
	}
	
	public Invader getRandomFrontInvader() {
		List<Invader> frontInvaders = new ArrayList<Invader>();
		for (int i = 0; i < COLS; i++) {
			for (int j = ROWS - 1; j >= 0; j--) {
				Invader invader = invaders[j][i][0];
				if (!invader.isRemoved()) {
					frontInvaders.add(invader);
					break;
				}
			}
		}
		return frontInvaders.get(r.nextInt(frontInvaders.size()));
	}
	
	public Invader[][][] getInvaders() {
		return invaders;
	}
	
	public int getNumEnemies() {
		return numEnemies;
	}
	
	public int getShiftY() {
		return shiftY;
	}
	
	public void setNumEnemies(int numEnemies) {
		this.numEnemies = numEnemies;
	}
	
	public void setShiftY(int shiftY) {
		this.shiftY = shiftY;
	}
	
}