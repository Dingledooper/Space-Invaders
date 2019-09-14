package com.github.dingledooper.spaceinvaders;

import java.awt.Graphics;
import java.util.List;

import javax.swing.ImageIcon;

public class InvaderShot extends Sprite {
	
	public static final String INVADER_SHOT_IMG = "res/invadershot.png";
	public static final int WIDTH = 6, HEIGHT = 14;
	public static final int SPEED = 4;
	
	public InvaderShot() {
		super(0, 0, WIDTH, HEIGHT);
		
		ImageIcon icon = new ImageIcon(INVADER_SHOT_IMG);
		setImage(icon.getImage());
		setRemoved(true);
	}
	
	public void move() {
		setY(getY() + SPEED);
	}
	
	public void checkCollisions(Game game, List<InvaderShot> invaderShots, Bunker[] bunkers, Graphics g, Cannon cannon, CannonShot cannonShot) {
		if (isIntersecting(getX(), getY(), getWidth(), getHeight(), cannon.getX(), cannon.getY() + 9, cannon.getWidth(), cannon.getHeight())) {
			
			setRemoved(true);
			cannon.setLives(cannon.getLives() - 1);

			if (cannon.getLives() == 0) {
				cannon.setRemoved(true);
			}
			
			Audio.play(2);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		int hitX = Game.clamp(getX() / 3 * 3, 0, Game.WIDTH - 1), hitY = Game.clamp((getY() + HEIGHT) / 3 * 3, 0, Game.HEIGHT - 1);
		for (Bunker b : bunkers) {
			if (b.getX() <= hitX && hitX < b.getX() + Bunker.WIDTH && 
				b.getY() <= hitY && hitY < b.getY() + Bunker.HEIGHT &&
				b.getBunkerData()[(hitY - b.getY()) / 3][(hitX - b.getX()) / 3] == 1) {
				
				hitBunker(g, b, hitX, hitY);
				setRemoved(true);
			}
		}
		
		if (isIntersecting(getX(), getY(), getWidth(), getHeight(), cannonShot.getX(), cannonShot.getY(), cannonShot.getWidth(), cannonShot.getHeight())) {
			setRemoved(true);
			cannonShot.setRemoved(true);
		}
	}
	
	public static void hitBunker(Graphics g, Bunker bunker, int x, int y) {
		int ax = (x - bunker.getX()) / 3, ay = (y - bunker.getY()) / 3;
		bunker.setBunkerData(ax - 1, ay, 2, 7);
		bunker.setBunkerData(ax - 3, ay + 1, 1, 1);
		bunker.setBunkerData(ax + 1, ay + 1, 1, 1);
		bunker.setBunkerData(ax + 2, ay + 2, 1, 1);
		bunker.setBunkerData(ax + 1, ay + 3, 1, 4);
		bunker.setBunkerData(ax - 2, ay + 3, 1, 1);
		bunker.setBunkerData(ax - 3, ay + 4, 1, 1);
		bunker.setBunkerData(ax - 2, ay + 5, 1, 1);
		bunker.setBunkerData(ax - 3, ay + 6, 1, 1);
		bunker.setBunkerData(ax - 2, ay + 7, 1, 1);
		bunker.setBunkerData(ax + 2, ay + 5, 1, 1);
		bunker.setBunkerData(ax + 2, ay + 7, 1, 1);
		bunker.setBunkerData(ax, ay + 7, 1, 1);
	}

}