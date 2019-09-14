package com.github.dingledooper.spaceinvaders;

import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Invader extends Sprite {
	
	private boolean isRemoved = false;
	
	public Invader(String type, int x, int y, int width, int height) {
		super(x, y, width, height);

		ImageIcon icon = new ImageIcon(type);
		setImage(icon.getImage());
	}
	
	public void move(int x, int y) {
		if (!isRemoved()) {
			setX(getX() + x);
			setY(getY() + y);
		}
	}
	
	public void checkCollisions(Graphics g, Cannon cannon, Bunker[] bunkers) {
		if (!isRemoved()) {
			if (isIntersecting(getX(), getY(), getWidth(), getHeight(), cannon.getX(), cannon.getY(), cannon.getWidth(), cannon.getHeight()) || getY() > Invaders.MAX_Y) {
				cannon.setRemoved(true);
			}
			
			for (Bunker b : bunkers) {
				if (isIntersecting(getX(), getY(), getWidth(), getHeight(), b.getX(), b.getY(), b.getWidth(), b.getHeight())) {
					b.setBunkerData((getX() - b.getX()) / 3 * 3, (getY() - b.getY()) / 3 * 3, getWidth(), getHeight());
				}
			}
		}
	}
	
	public int getScore() {
		int width = getWidth();
		return width == Invaders.OCTO_WIDTH ? 10 : width == Invaders.CRAB_WIDTH ? 20 : 30;
	}
	
	public void setRemoved(boolean isRemoved) {
		this.isRemoved = isRemoved;
	}
	
	public boolean isRemoved() {
		return isRemoved;
	}

}