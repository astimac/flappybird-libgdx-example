package com.astimac.flappybirdexample.screens;

import com.astimac.flappybirdexample.FlappyBirdExampleGame;
import com.astimac.flappybirdexample.controller.GameWorld;
import com.astimac.flappybirdexample.controller.GameWorldRenderer;
import com.astimac.flappybirdexample.framework.Assets;
import com.astimac.flappybirdexample.framework.FontManager;
import com.astimac.flappybirdexample.framework.Text;
import com.astimac.flappybirdexample.models.Bird;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class GameScreen extends BaseScreen {

	private static final int STATE_READY = 1;
	private static final int STATE_RUNNING = 2;
	private static final int STATE_PAUSED = 3;
	private static final int STATE_GAME_OVER = 4;

	private int state;

	private GameWorld mGameWorld;
	private GameWorldRenderer mRenderer;

	private boolean canJump;

	private Image mFlash;

	public GameScreen(FlappyBirdExampleGame mGame) {
		super(mGame);
		this.state = STATE_READY;
		this.renderScore = false;

		mGameWorld = new GameWorld(new GameWorld.GameWorldListener() {

			@Override
			public void onHitObstacle() {
				Assets.playSound(Assets.punch);
				Assets.playSound(Assets.groan);
				int best = Assets.prefs.getInteger("best", 0);
				if (mGameWorld.counterScore >= best) {
					Assets.prefs.putInteger("best", mGameWorld.counterScore);
					Assets.prefs.flush();
				}
				Assets.prefs.putInteger("score", mGameWorld.counterScore);
				Assets.prefs.flush();
			}

			@Override
			public void onHitCounter() {
				Assets.playSound(Assets.coin);

			}
		});

		mRenderer = new GameWorldRenderer(mBatcher, mGameWorld);

		canJump = false;

		initGameOver();
		initGetReady();

		final Image blackImage = new Image(Assets.black);
		blackImage.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		blackImage.getColor().a = 1.0F;
		blackImage.addAction(Actions.sequence(Actions.fadeOut(0.4F),
				Actions.run(new Runnable() {
					public void run() {
						blackImage.remove();
					}
				})));
		mFlash = new Image(Assets.white);
		mFlash.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		mFlash.addAction(Actions.sequence(Actions.fadeOut(0.5F),
				Actions.run(new Runnable() {
					public void run() {
						mFlash.remove();
					}
				})));

		this.mStage.addActor(blackImage);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	private void updateRunning(float delta) {
		if (Gdx.input.justTouched()) {
			Assets.playSound(Assets.flap);
			this.canJump = true;
		}

		this.mGameWorld.update(delta, this.canJump);

		if (this.mGameWorld.bird.state == Bird.STATE_HURT) {
			mStage.addActor(mFlash);
			if (this.mGameWorld.bird.stateTime >= 1.8f) {
				this.mGameWorld.bird.die();
			}
		}

		if (this.mGameWorld.state == GameWorld.STATE_GAMEOVER) {
			setGameover();
		}

		this.canJump = false;
	}

	float x;
	boolean renderScore;

	@Override
	public void draw(float deltaTime) {
		if ((state == 3) || (state == 4))
			deltaTime = 0.0F;

		this.mBatcher.begin();
		this.mBatcher.draw(Assets.background, 0.0F, 0.0F, SCREEN_WIDTH, SCREEN_HEIGHT);
		this.mBatcher.end();

		this.mRenderer.render(deltaTime);

		Assets.parallaxBackground.render(deltaTime);

		this.mOrthoCamera.update();
		this.mBatcher.setProjectionMatrix(mOrthoCamera.combined);
		this.mBatcher.begin();

		switch (state) {
		case STATE_READY:
			drawReady(deltaTime);
			break;
		case STATE_GAME_OVER:
			drawReady(deltaTime);
			break;
		case STATE_RUNNING:
			x = SCREEN_WIDTH
					/ 2
					- 50
					/ 2;
			FontManager.fontMarker.setColor(Color.BLACK);
			FontManager.fontMarker.draw(mBatcher, mGameWorld.counterScore + "",
					x, 736);
			FontManager.fontMarker.setColor(Color.WHITE);
			FontManager.fontMarker.draw(mBatcher, mGameWorld.counterScore + "",
					x, 740);
			break;
		default:
			break;
		}

		this.mBatcher.end();
	}

	@Override
	public void update(float deltaTime) {
		switch (state) {
		case STATE_READY:
			updateGetReady(deltaTime);
			break;
		case STATE_RUNNING:
			updateRunning(deltaTime);
			break;
		case STATE_PAUSED:

			break;
		case STATE_GAME_OVER:

			break;
		default:
			break;
		}

	}

	private Image mGetReadyImage;
	private Image mTapStartImage;

	private void initGetReady() {
		this.mGetReadyImage = new Image(Assets.get_ready);
		this.mGetReadyImage.setSize(Assets.get_ready.getRegionWidth(),
				Assets.get_ready.getRegionHeight());
		this.mGetReadyImage.setPosition(
				SCREEN_WIDTH / 2 - mGetReadyImage.getWidth() / 2, SCREEN_HEIGHT
						/ 2 - mGetReadyImage.getHeight() / 2 + 180);
		this.mGetReadyImage.getColor().a = 0.0F;
		this.mGetReadyImage.addAction(Actions.fadeIn(0.8F));
		this.mTapStartImage = new Image(Assets.tap_start);
		this.mTapStartImage.setSize(Assets.tap_start.getRegionWidth(),
				Assets.tap_start.getRegionHeight());
		this.mTapStartImage.setPosition(
				SCREEN_WIDTH / 2 - mTapStartImage.getWidth() / 2, SCREEN_HEIGHT
						/ 2 - mTapStartImage.getHeight() / 2 - 130);
		this.mTapStartImage.getColor().a = 0.0F;
		this.mTapStartImage.addAction(Actions.fadeIn(0.8F));

		this.mStage.addActor(this.mGetReadyImage);
		this.mStage.addActor(this.mTapStartImage);
	}

	private void updateGetReady(float delta) {
		if (Gdx.input.justTouched()) {
			this.mGetReadyImage.remove();
			this.mTapStartImage.remove();
			state = STATE_RUNNING;
			this.canJump = true;
			Assets.playSound(Assets.flap);
		}
	}

	private void drawReady(float delta) {
		this.mGameWorld.bird.update(delta, null);
	}

	private Image mBlackImage;
	private Image mGameOverImage;
	private Image mDialog;
	private Button mPlayBtn;
	private Button mScoreBtn;

	private void initGameOver() {
		this.mBlackImage = new Image(Assets.black);
		this.mBlackImage.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		this.mBlackImage.getColor().a = 0.0F;
		this.mBlackImage.addAction(Actions.sequence(Actions.fadeIn(0.4F),
				Actions.run(new Runnable() {
					public void run() {
						mGame.setScreen(new GameScreen(mGame));
//						flipGame.resolver.loadAds();
					}
				})));

		this.mDialog = new Image(Assets.dialog);
		this.mDialog.setSize(Assets.dialog.getRegionWidth(),
				Assets.dialog.getRegionHeight());
		this.mDialog.setPosition(SCREEN_WIDTH / 2 - mDialog.getWidth() / 2,
				SCREEN_HEIGHT / 2 - mDialog.getHeight() / 2);

		this.mPlayBtn = new Button(new TextureRegionDrawable(
				Assets.game_over_play_btn));
		this.mPlayBtn.setSize(Assets.game_over_play_btn.getRegionWidth(),
				Assets.game_over_play_btn.getRegionHeight());
		this.mPlayBtn.setPosition(75.0F, 125.0F);
		this.mPlayBtn.addListener(new InputListener() {
			public boolean touchDown(InputEvent paramInputEvent,
					float paramFloat1, float paramFloat2, int paramInt1,
					int paramInt2) {
				mPlayBtn.setPosition(75.0F, 122.0F);
				return true;
			}

			public void touchUp(InputEvent paramInputEvent, float paramFloat1,
					float paramFloat2, int paramInt1, int paramInt2) {
				mPlayBtn.setPosition(75.0F, 125.0F);
				mScoreBtn.addAction(Actions.fadeOut(0.2F));
				mPlayBtn.addAction(Actions.fadeOut(0.2F));
				mGameOverImage.addAction(Actions.fadeOut(0.2F));
				state = STATE_READY;
				mStage.addActor(mBlackImage);
			}
		});

		this.mScoreBtn = new Button(new TextureRegionDrawable(
				Assets.game_over_score_btn));
		this.mScoreBtn.setSize(Assets.game_over_score_btn.getRegionWidth(),
				Assets.game_over_score_btn.getRegionHeight());
		this.mScoreBtn.addListener(new InputListener() {
			public boolean touchDown(InputEvent paramInputEvent,
					float paramFloat1, float paramFloat2, int paramInt1,
					int paramInt2) {
				mScoreBtn.setPosition(250.0F, 122.0F);
				return true;
			}

			public void touchUp(InputEvent paramInputEvent, float paramFloat1,
					float paramFloat2, int paramInt1, int paramInt2) {
				mScoreBtn.setPosition(250.0F, 125.0F);
			}
		});
		this.mScoreBtn.setPosition(250.0F, 125.0F);
		this.mGameOverImage = new Image(Assets.game_over);
		this.mGameOverImage.setSize(Assets.game_over.getRegionWidth(),
				Assets.game_over.getRegionHeight());
		this.mGameOverImage.setPosition(
				SCREEN_WIDTH / 2 - Assets.game_over.getRegionWidth() / 2, 620);
	}

	private void setGameover() {
		state = STATE_GAME_OVER;

		this.mGameOverImage.getColor().a = 0f;
		this.mDialog.getColor().a = 0f;
		this.mPlayBtn.getColor().a = 0f;
		this.mScoreBtn.getColor().a = 0f;
		mGameOverImage.addAction(Actions.fadeIn(0.8f));
		mDialog.addAction(Actions.sequence(Actions.fadeIn(0.86F),
				Actions.run(new Runnable() {
					public void run() {
						mDialog.getColor().a = 0.90f;
						renderScore = true;
//						Color red = new Color(0.835f, 0.09f, 0.125f, 1);
						mStage.addActor(new Text("SCORE: ", 65, 517,
								Color.BLACK));
						mStage.addActor(new Text("BEST: ", 65, 397,
								Color.BLACK));
						mStage.addActor(new Text("SCORE: ", 65, 520, Color.WHITE));
						mStage.addActor(new Text("BEST: ", 65, 400, Color.WHITE));

						int best = Assets.prefs.getInteger("best", 0);

						mStage.addActor(new Text(mGameWorld.counterScore
								+ "", 365, 517, Color.BLACK));
						mStage.addActor(new Text(best + "", 365, 397,
								Color.BLACK));
						mStage.addActor(new Text(mGameWorld.counterScore
								+ "", 365, 520, Color.WHITE));
						mStage.addActor(new Text(best + "", 365, 400, Color.WHITE));

						if(mGame.resolver != null) {
							mGame.resolver.loadAd();
						}
					}
				})));

		mPlayBtn.addAction(Actions.fadeIn(0.8f));
		mScoreBtn.addAction(Actions.fadeIn(0.8f));

		this.mStage.addActor(this.mDialog);
		this.mStage.addActor(this.mPlayBtn);
		this.mStage.addActor(this.mScoreBtn);
		this.mStage.addActor(this.mGameOverImage);
	}

	@Override
	public boolean keyUp(int keycode) {
		if (Input.Keys.R == keycode || Input.Keys.BACK == keycode) {
			Image black = new Image(Assets.black);
			black.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
			black.getColor().a = 0.0F;
			black.addAction(Actions.sequence(Actions.fadeIn(0.4f),
					Actions.run(new Runnable() {

						@Override
						public void run() {
							mGame.setScreen(new MainMenuScreen(mGame));
						}
					})));

			mStage.addActor(black);
		}

		return false;
	}
}
