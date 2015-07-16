package com.astimac.flappybirdexample.screens;

import com.astimac.flappybirdexample.FlappyBirdExampleGame;
import com.astimac.flappybirdexample.framework.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MainMenuScreen extends BaseScreen {
	
	/**
	 * LOGO IMAGE
	 */
	private Image mLogoImage;

	/**
	 * BUTTONS
	 */
	private Button mPlayBtn;
	private Button mScoresBtn;
	
	/**
	 * FADE OUT BLACK
	 */
	private Image mBlackImage;

	public MainMenuScreen(final FlappyBirdExampleGame mGame) {
		super(mGame);

		mLogoImage = new Image(Assets.logo);
		mLogoImage.setSize(Assets.logo.getRegionWidth(), Assets.logo.getRegionHeight());
		mLogoImage.setPosition(SCREEN_WIDTH/2 - mLogoImage.getWidth()/2, SCREEN_HEIGHT - mLogoImage.getHeight() - 65);
		
		mPlayBtn = new Button(new TextureRegionDrawable(Assets.play_btn));
		mPlayBtn.setSize(Assets.play_btn.getRegionWidth(), Assets.play_btn.getRegionHeight());
		mPlayBtn.setPosition(SCREEN_WIDTH/2 - mPlayBtn.getWidth()/2, SCREEN_HEIGHT - mLogoImage.getHeight() - mPlayBtn.getHeight() - 90);
		
		mScoresBtn = new Button(new TextureRegionDrawable(Assets.scores_btn));
		mScoresBtn.setSize(Assets.scores_btn.getRegionWidth(), Assets.scores_btn.getRegionHeight());
		mScoresBtn.setPosition(SCREEN_WIDTH/2 - mScoresBtn.getWidth()/2, SCREEN_HEIGHT - mLogoImage.getHeight() - mPlayBtn.getHeight() - mScoresBtn.getHeight() - 150);
		
		
		mBlackImage = new Image(Assets.black);
		mBlackImage.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		mBlackImage.getColor().a = 0.0F;
		mBlackImage.addAction(Actions.sequence(Actions.fadeIn(0.4f), Actions.run(new Runnable() {
			
			@Override
			public void run() {
				mGame.setScreen(new GameScreen(mGame));
			}
		})));
		
		mPlayBtn.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				super.touchUp(event, x, y, pointer, button);
				mPlayBtn.setPosition(SCREEN_WIDTH/2 - mPlayBtn.getWidth()/2, SCREEN_HEIGHT - mLogoImage.getHeight() - mPlayBtn.getHeight() - 90);
				mStage.addActor(mBlackImage);
			}
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				mPlayBtn.setPosition(SCREEN_WIDTH/2 - mPlayBtn.getWidth()/2, SCREEN_HEIGHT - mLogoImage.getHeight() - mPlayBtn.getHeight() - 95);
				return true;
			}
		});
		
		
		mScoresBtn.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				super.touchUp(event, x, y, pointer, button);
				mScoresBtn.setPosition(SCREEN_WIDTH/2 - mScoresBtn.getWidth()/2, SCREEN_HEIGHT - mLogoImage.getHeight() - mPlayBtn.getHeight() - mScoresBtn.getHeight() - 150);
			}
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				mScoresBtn.setPosition(SCREEN_WIDTH/2 - mScoresBtn.getWidth()/2, SCREEN_HEIGHT - mLogoImage.getHeight() - mPlayBtn.getHeight() - mScoresBtn.getHeight() - 155);
				
				return true;
			}
		});
		
		
		this.mStage.addActor(mLogoImage);
		this.mStage.addActor(mPlayBtn);
		this.mStage.addActor(mScoresBtn);
		
		final Image blackImage = new Image(Assets.black);
		blackImage.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		blackImage.getColor().a = 1.0F;
		blackImage.addAction(Actions.sequence(Actions.fadeOut(0.4F),
				Actions.run(new Runnable() {
					public void run() {
						blackImage.remove();
					}
				})));

		this.mStage.addActor(blackImage);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(float deltaTime) {
		this.mBatcher.begin();
		this.mBatcher.draw(Assets.background, 0.0F, 0.0F, SCREEN_WIDTH, SCREEN_HEIGHT);
		this.mBatcher.end();
		
		Assets.parallaxBackground.render(deltaTime);
		
		this.mOrthoCamera.update();
		this.mBatcher.setProjectionMatrix(mOrthoCamera.combined);
		this.mBatcher.enableBlending();
		this.mBatcher.begin();

		//DRAWING GOES HERE
		
		this.mBatcher.end();
	}

	@Override
	public void update(float deltaTime) {

	}
	
	@Override
	public boolean keyUp(int keycode) {
		if (Input.Keys.R == keycode || Input.Keys.BACK == keycode) {
			if(mGame.resolver != null) {
				mGame.resolver.loadAd();
			}

			Gdx.app.exit();
		}
		return false;
	}

}
