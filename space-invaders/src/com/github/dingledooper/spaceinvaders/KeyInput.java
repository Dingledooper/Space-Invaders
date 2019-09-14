package com.github.dingledooper.spaceinvaders;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
		
	private boolean leftDown = false, rightDown = false;
	private Cannon cannon;
	private CannonShot cannonShot;
	
	public KeyInput(Cannon cannon, CannonShot cannonShot) {
		this.cannon = cannon;
		this.cannonShot = cannonShot;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_LEFT)  { cannon.setVelX(-Cannon.SPEED); leftDown = true; }
		if (keyCode == KeyEvent.VK_RIGHT) { cannon.setVelX(Cannon.SPEED); rightDown = true; }
		
		if (keyCode == KeyEvent.VK_SPACE) {
			if (cannonShot.isRemoved()) {
				Audio.play(1);
				cannonShot.setRemoved(false);
				cannonShot.setX(cannon.getX() + Cannon.WIDTH / 2);
				cannonShot.setY(cannon.getY());
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_LEFT)  leftDown = false;
		if (keyCode == KeyEvent.VK_RIGHT) rightDown = false;
		
		if (!leftDown && !rightDown) cannon.setVelX(0);
	}
	
}