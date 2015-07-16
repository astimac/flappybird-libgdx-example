package com.astimac.flappybirdexample.screens;

import com.astimac.flappybirdexample.FlappyBirdExampleGame;
import com.astimac.flappybirdexample.framework.Assets;

public class LoadingScreen extends BaseScreen {
	
	private float mStateTime;

	public LoadingScreen(FlappyBirdExampleGame mGame) {
		super(mGame);
		
		this.mStateTime = 0;
		
		Assets.load();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float deltaTime) {
		mStateTime += deltaTime;

		if (Assets.assetManager.update()) {
			if (Assets.isLoaded()) {
				Assets.assign();
				mGame.setScreen(new MainMenuScreen(mGame));
			}
		}
	}

}
