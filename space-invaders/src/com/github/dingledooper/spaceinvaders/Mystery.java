package com.github.dingledooper.spaceinvaders;

import javax.swing.ImageIcon;

public class Mystery extends Sprite {
	
	public static final String MYSTERY_IMG = "res/mystery.png";
	public static final int WIDTH = 48, HEIGHT = 21, MYSTERY_X = -32, MYSTERY_Y = 60;
	public static final int SPEED = 2;
	public static final int POINTS = 200;
	public static final int TICKS_UNTIL_MYSTERY = 2400;
	
	private int audioTick = 0;
	private int dir = -1;

	public Mystery() {
		super(MYSTERY_X, MYSTERY_Y, WIDTH, HEIGHT);
		
		ImageIcon icon = new ImageIcon(MYSTERY_IMG);
		setImage(icon.getImage());
		setRemoved(true);
	}
	
	public void move() {
		setX(getX() + SPEED * dir);
		if (getX() < -WIDTH || getX() > Game.WIDTH) {
			setRemoved(true);
		}
		
		if (audioTick++ % 30 == 0) {
			Audio.play(8);
		}
	}
	
	public int getDirection() {
		return dir;
	}
	
	public void changeDirection() {
		dir = dir == 1 ? -1 : 1;
	}

}