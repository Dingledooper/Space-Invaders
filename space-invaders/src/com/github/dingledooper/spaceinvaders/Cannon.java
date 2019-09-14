package com.github.dingledooper.spaceinvaders;

import javax.swing.ImageIcon;

public class Cannon extends Sprite {
	
	public static final String CANNON_IMG = "res/cannon.png";
	public static final int WIDTH = 39, HEIGHT = 24, CANNON_X = (Game.WIDTH - WIDTH) / 2, CANNON_Y = 530; 
	public static final int MIN_X = 10, MAX_X = Game.WIDTH - Cannon.WIDTH - MIN_X;
	public static final int SPEED = 4;
	
	private int lives = 3;
	private int velX = 0;
	
	public Cannon() {
		super(CANNON_X, CANNON_Y, WIDTH, HEIGHT);
		
		ImageIcon icon = new ImageIcon(CANNON_IMG);
		setImage(icon.getImage());
	}
	
	public void move() {
		setX(getX() + velX);
		setX(Game.clamp(getX(), MIN_X, MAX_X));
	}
	
	public int getLives() { 
		return lives;
	}
	
	public void setLives(int lives) {
		this.lives = lives;
	}
	
	public void setVelX(int velX) {
		this.velX = velX;
	}

}