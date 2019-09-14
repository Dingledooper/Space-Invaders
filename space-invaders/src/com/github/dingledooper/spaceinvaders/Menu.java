package com.github.dingledooper.spaceinvaders;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu extends MouseAdapter {
	
	public static final int OVER_WIDTH = 424, OVER_HEIGHT = 35, OVER_X = (Game.WIDTH - OVER_WIDTH) / 2, OVER_Y = 400;
	public static final int MENU_HEIGHT = 50;
	public static final int PLAY_WIDTH = 141, PLAY_X = (Game.WIDTH - PLAY_WIDTH) / 2, PLAY_Y = 300;
	public static final int QUIT_WIDTH = 141, QUIT_X = (Game.WIDTH - QUIT_WIDTH) / 2, QUIT_Y = 500;
	
	private Game game;
	private Cannon cannon;

	public Menu(Game game, Cannon cannon) {
		this.game = game;
		this.cannon = cannon;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mouseX = e.getX(), mouseY = e.getY();
		
		if (game.getStatus() == Status.MENU) {
			if (mouseOver(mouseX, mouseY, PLAY_X, PLAY_Y, PLAY_WIDTH, MENU_HEIGHT)) {
				game.setStatus(Status.PLAY);
				cannon.setRemoved(false);
			} else if (mouseOver(mouseX, mouseY, QUIT_X, QUIT_Y, QUIT_WIDTH, MENU_HEIGHT)) {
				System.exit(0);
			}
		} else if (game.getStatus() == Status.GAME_OVER) {
			if (mouseOver(mouseX, mouseY, OVER_X, OVER_Y, OVER_WIDTH, OVER_HEIGHT)) {
				game.setStatus(Status.MENU);
			}
		}
	}
	
	public void render(Graphics g) {
		if (game.getStatus() == Status.GAME_OVER) {
			g.setColor(Color.WHITE);
			g.setFont(game.getSpaceInvadersFont().deriveFont(40F));
			g.drawString("Final Score:", (Game.WIDTH - g.getFontMetrics().stringWidth("Final Score:")) / 2, 250);
			g.drawString(game.getScoreString(), (Game.WIDTH - g.getFontMetrics().stringWidth(game.getScoreString())) / 2, 300);
			
			g.setColor(Color.RED);
			g.setFont(game.getSpaceInvadersFont().deriveFont(35F));
			g.drawString("Back to Menu", OVER_X, OVER_Y + 35);
		} else if (game.getStatus() == Status.MENU) {
			g.drawImage(Game.LOGO_ICON.getImage(), (Game.WIDTH - 360) / 2, 70, 360, 150, null);
			g.setColor(Color.GREEN);
			g.setFont(game.getSpaceInvadersFont().deriveFont(35F));
			g.drawString("Play", PLAY_X, PLAY_Y + 35);
			g.drawString("Quit", QUIT_X, QUIT_Y + 35);
		}
	}
	
	private boolean mouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
	
}