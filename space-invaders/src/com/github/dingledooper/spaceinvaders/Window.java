package com.github.dingledooper.spaceinvaders;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends JFrame {

	private static final long serialVersionUID = -1766954078348840209L;

	public Window(Game game, String title, int width, int height) {
		super(title);

		add(game);
		pack();
		
		int x = getInsets().left + getInsets().right, y = getInsets().top + getInsets().bottom;
		setPreferredSize(new Dimension(width + x, height + y));
		setMaximumSize(new Dimension(width + x, height + y));
		setMinimumSize(new Dimension(width + x, height + y));

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
}