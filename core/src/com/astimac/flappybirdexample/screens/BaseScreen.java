package com.astimac.flappybirdexample.screens;

import com.astimac.flappybirdexample.FlappyBirdExampleGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public abstract class BaseScreen extends InputAdapter implements Screen {
	
	public static final int SCREEN_HEIGHT = 853;
	public static final int SCREEN_WIDTH = 480;
	
	public static final float WORLD_SCREEN_HEIGHT = 853f/100f;
	public static final float WORLD_SCREEN_WIDTH = 480f/100f;

	public SpriteBatch mBatcher;
	
	public FlappyBirdExampleGame mGame;
	
	public OrthographicCamera mOrthoCamera;
	
	public Stage mStage;

	public BaseScreen(FlappyBirdExampleGame game) {
		this.mStage = game.mStage;
		this.mStage.clear();
		
		this.mBatcher = game.mSpriteBatch;
		
		this.mGame = game;
		
		this.mOrthoCamera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
		this.mOrthoCamera.position.set(SCREEN_WIDTH/2f, SCREEN_HEIGHT/2f, 0.0F);
		
		InputProcessor[] arrayOfInputProcessor = new InputProcessor[2];
		arrayOfInputProcessor[0] = this;
		arrayOfInputProcessor[1] = this.mStage;
		InputMultiplexer mInputMultiplexer = new InputMultiplexer(arrayOfInputProcessor);

		Gdx.input.setInputProcessor(mInputMultiplexer);
		Gdx.input.setCatchBackKey(true);

        this.mStage.setViewport(new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, mOrthoCamera));
	}

	public void dispose() {
		this.mStage.dispose();
		this.mBatcher.dispose();
        this.mGame.dispose();
	}

	public abstract void draw(float deltaTime);
	
	public abstract void update(float deltaTime);
	
	@Override
	public void render(float delta) {
/*		if(delta > 0.1f)
			delta = 0.1f;*/
		
		update(delta);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		
		draw(delta);
		
		this.mStage.act(delta);
		this.mStage.draw();
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
        this.mStage.getViewport().update(width, height, true);
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}


}