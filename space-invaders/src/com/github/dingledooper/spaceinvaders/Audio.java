package com.github.dingledooper.spaceinvaders;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

@SuppressWarnings("deprecation")

public class Audio {
	
	private static AudioClip[] clips = new AudioClip[10];

	public static void load() {
		for (int i = 0; i < 10; i++) {
			URL url = Audio.class.getResource("/../res/" + i + ".wav");
			clips[i] = Applet.newAudioClip(url);
		}
	}
	
	public static void play(int clip) {
		clips[clip].play();
	}
	
}