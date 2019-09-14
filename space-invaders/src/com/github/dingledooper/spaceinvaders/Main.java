package com.github.dingledooper.spaceinvaders;

import java.awt.EventQueue;

public class Main {

	public Main() {
		new Game();
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Main();
			}
		});
	}
	
}