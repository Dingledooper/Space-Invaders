package com.github.dingledooper.spaceinvaders;

import java.awt.Graphics;
import java.util.Random;

import javax.swing.ImageIcon;

public class CannonShot extends Sprite {
	
	public static final String CANNON_SHOT_IMG = "res/cannonshot.png";
	public static final int WIDTH = 1, HEIGHT = 10;
	public static final int SPEED = 7;
	
	private Random r;

	public CannonShot() {
		super(Cannon.CANNON_X, Cannon.CANNON_Y, WIDTH, HEIGHT);
		r = new Random();
		
		ImageIcon icon = new ImageIcon(CANNON_SHOT_IMG);
		setImage(icon.getImage());
		setRemoved(true);
	}
	
	public void move() {
		setY(getY() - SPEED);
	}
	
	public void checkCollisions(Game game, Graphics g, Invaders invaders, Bunker[] bunkers, Mystery mystery) {
		Invader[][][] invadersArray = invaders.getInvaders();
		for (int i = 0; i < Invaders.ROWS; i++) {
			for (int j = 0; j < Invaders.COLS; j++) {
				Invader[] invader = invadersArray[i][j];
				if (!invader[0].isRemoved() && 
					isIntersecting(getX(), getY(), getWidth(), getHeight(), invader[0].getX(), invader[0].getY(), invader[0].getWidth(), invader[0].getHeight())) {
					
					setRemoved(true);
					invader[0].setRemoved(true);
					invader[1].setRemoved(true);
					invader[2].setRemoved(true);
					
					game.setScore(game.getScore() + invader[0].getScore());
					game.deathEffect[i][j] = Invaders.DEATH_EFFECT_TICKS;
					invaders.setNumEnemies(invaders.getNumEnemies() - 1);
					Audio.play(3);
				}
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
		
		
		if (isIntersecting(getX(), getY(), getWidth(), getHeight(), mystery.getX(), mystery.getY(), mystery.getWidth(), mystery.getHeight())) {
			Audio.play(3);
			
			game.mysteryScoreTick = 50;
			game.mysteryHitX = mystery.getX();
			game.mysteryHitY = mystery.getY();
			
			game.mysteryPoints = (r.nextInt(3) + 1) * 100;
			game.setScore(game.getScore() + game.mysteryPoints);
			
			setRemoved(true);
			mystery.setRemoved(true);
			mystery.setX(mystery.getDirection() == 1 ? Game.WIDTH : -WIDTH);
		}
	}
	
	public static void hitBunker(Graphics g, Bunker bunker, int x, int y) {
		int ax = (x - bunker.getX()) / 3, ay = (y - bunker.getY()) / 3;
		bunker.setBunkerData(ax - 3, ay, 7, 2);
		bunker.setBunkerData(ax - 3, ay - 1, 6, 1);
		bunker.setBunkerData(ax - 5, ay + 1, 1, 1);
		bunker.setBunkerData(ax - 4, ay, 1, 1);
		bunker.setBunkerData(ax - 2, ay - 2, 1, 1);
		bunker.setBunkerData(ax, ay - 3, 1, 1);
		bunker.setBunkerData(ax + 2, ay - 2, 1, 1);
		bunker.setBunkerData(ax + 3, ay - 3, 1, 1);
	}
	
}
