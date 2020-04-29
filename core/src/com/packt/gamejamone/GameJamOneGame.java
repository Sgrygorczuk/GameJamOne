package com.packt.gamejamone;


import com.badlogic.gdx.Game;

public class GameJamOneGame extends Game {
	@Override
	public void create () {
		//Calls game screen
		setScreen(new StartScreen());
	}
}
