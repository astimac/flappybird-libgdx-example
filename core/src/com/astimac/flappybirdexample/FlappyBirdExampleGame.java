package com.astimac.flappybirdexample;

import com.astimac.flappybirdexample.framework.ActionResolver;
import com.astimac.flappybirdexample.screens.LoadingScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class FlappyBirdExampleGame extends Game {

	public SpriteBatch mSpriteBatch;
	public Stage mStage;
	public ActionResolver resolver;

	public FlappyBirdExampleGame(ActionResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public void create() {
		this.mStage = new Stage();
		this.mSpriteBatch = new SpriteBatch();

		setScreen(new LoadingScreen(this));
	}
}
