package com.astimac.flappybirdexample.controller;

import com.astimac.flappybirdexample.framework.Assets;
import com.astimac.flappybirdexample.models.Bird;
import com.astimac.flappybirdexample.models.Tubes;
import com.astimac.flappybirdexample.screens.BaseScreen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameWorldRenderer {

	private final OrthographicCamera mOrthCamera = new OrthographicCamera(BaseScreen.WORLD_SCREEN_WIDTH, BaseScreen.WORLD_SCREEN_HEIGHT);

	private final SpriteBatch mBatcher;

	private final GameWorld mGameWorld;

	public GameWorldRenderer(SpriteBatch spriteBatch, GameWorld gameWorld) {
		this.mOrthCamera.position.set(BaseScreen.WORLD_SCREEN_WIDTH / 2, BaseScreen.WORLD_SCREEN_HEIGHT / 2, 0.0F);
		this.mBatcher = spriteBatch;
		this.mGameWorld = gameWorld;
	}

	private void drawBird(float delta) {
		Bird bird = this.mGameWorld.bird;
//		if (bird.state == Bird.STATE_NORMAL) {
		mBatcher.draw(Assets.flappyCatAnimation.getKeyFrame(bird.stateTime,
						true), bird.position.x - Bird.WIDTH / 2,
				bird.position.y - Bird.HEIGHT / 2, Bird.WIDTH / 2,
				Bird.HEIGHT / 2, Bird.WIDTH, Bird.HEIGHT, 1.0F,
				1.0F, (float) Math.toDegrees(bird.angleRad));
//		}
	}

	private void drawTubes(float delta) {
		for (Tubes tubes : mGameWorld.tubesArray) {
			if (tubes.type == Tubes.TYPE_UP) {
				mBatcher.draw(Assets.pipeUp, tubes.position.x - Tubes.WIDTH
						/ 2, tubes.position.y - Tubes.HEIGHT / 2, Tubes.WIDTH,
						Tubes.HEIGHT);
			} else {
				mBatcher.draw(Assets.pipeDown, tubes.position.x
						- Tubes.WIDTH / 2, tubes.position.y - Tubes.HEIGHT / 2,
						Tubes.WIDTH, Tubes.HEIGHT);
			}
		}
	}

	public void render(float delta) {
		this.mOrthCamera.update();
		this.mBatcher.setProjectionMatrix(this.mOrthCamera.combined);
		this.mBatcher.begin();
		this.mBatcher.disableBlending();
		this.mBatcher.enableBlending();
		drawTubes(delta);
		drawBird(delta);
		this.mBatcher.end();
	}
}