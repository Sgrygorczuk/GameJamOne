package com.packt.gamejamone.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.packt.gamejamone.GameJamOneGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new GameJamOneGame(), config);
		config.height = 320;
		config.width = 480;
		config.resizable = true;
	}
}
