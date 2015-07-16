package com.astimac.flappybirdexample.framework;

import com.astimac.flappybirdexample.parallax.ParallaxBackground;
import com.astimac.flappybirdexample.parallax.ParallaxLayer;
import com.astimac.flappybirdexample.screens.BaseScreen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Assets {

	public static final String PREFS = "flip_flap_prefs";
	public static AssetManager assetManager = null;
	public static final String ASSETS_DIR = "data/flipflop.pack";

	public static Music theme;
	public static final float SOUND_VOLUME = 0.9f;

	/** PAPER BACKGROUND */
	public static TextureRegion background;

	public static boolean MUSIC = true;
	public static boolean VIBRATE = true;
	public static int TOUCHPAD = 0;
	public static TextureRegion pipeUp;
	public static TextureRegion pipeDown;
	public static TextureRegion platform_bottom;
	public static TextureRegion logo;
	public static TextureRegion pause;
	public static Sprite play_btn;
	public static TextureRegion scores_btn;
	public static TextureRegion dialog;
	public static Animation flappyCatAnimation;
	public static TextureRegion game_over;
	public static TextureRegion black;
	public static TextureRegion white;
	public static TextureRegion game_over_play_btn;
	public static TextureRegion game_over_score_btn;
	public static TextureRegion rate;
	public static TextureRegion get_ready;

	public static TextureRegion tap_start;
	public static Sound groan;
	public static Sound flap;
	public static Sound punch;
	public static Sound coin;
	public static Preferences prefs;
	public static TextureRegion bird1;
	public static ParallaxBackground parallaxBackground;

	private static AssetErrorListener errorListener = new AssetErrorListener() {

		@Override
		public void error(AssetDescriptor arg0, Throwable arg1) {
			// TODO Auto-generated method stub

		}
	};

	public static void createAssetManager() {
		InternalFileHandleResolver resolver = new InternalFileHandleResolver();
		prefs = Gdx.app.getPreferences(Assets.PREFS);
		TOUCHPAD = prefs.getInteger("TP", 0);

		int tmp = prefs.getInteger("SOUND", 0);
		if (tmp == 0) {
			MUSIC = true;
		} else {
			MUSIC = false;
		}
		tmp = prefs.getInteger("VIBRATE", 0);
		if (tmp == 0) {
			VIBRATE = true;
		} else {
			VIBRATE = false;
		}
		assetManager = new AssetManager();

		assetManager.setLoader(Texture.class, new TextureLoader(resolver));
		assetManager.setLoader(Music.class, new MusicLoader(resolver));
		assetManager.setLoader(Sound.class, new SoundLoader(resolver));
		assetManager
				.setLoader(BitmapFont.class, new BitmapFontLoader(resolver));

		assetManager.setErrorListener(errorListener);

		loadAssets();
	}

	private static void loadAssets() {
		assetManager.load(ASSETS_DIR, TextureAtlas.class);
		assetManager.load("data/coin.mp3", Sound.class);
		assetManager.load("data/groan.mp3", Sound.class);
		assetManager.load("data/flap.mp3", Sound.class);
		assetManager.load("data/punch.mp3", Sound.class);
		FontManager.loadAssets();
	}

	public static Texture loadTexture(String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static TextureAtlas fetchTextureAtlas(String name) {
		if (assetManager.isLoaded(name))
			return (TextureAtlas) assetManager.get(name, TextureAtlas.class);
		return null;
	}

	public static boolean isLoaded;

	public static boolean isLoaded() {
		if (null == assetManager) {
			return false;
		}

		isLoaded = true;

		if (!assetManager.isLoaded(ASSETS_DIR)) {
			isLoaded = false;
		}
		isLoaded = FontManager.isLoaded();
		return isLoaded;
	}

	public static void assign() {
		background = assetManager.get(ASSETS_DIR, TextureAtlas.class)
				.findRegion("background");
		platform_bottom = assetManager.get(ASSETS_DIR, TextureAtlas.class)
				.findRegion("front_bg");
		pipeDown = assetManager.get(ASSETS_DIR, TextureAtlas.class).findRegion(
				"tube");
		pipeUp = assetManager.get(ASSETS_DIR, TextureAtlas.class).findRegion(
				"tube");
		parallaxBackground = new ParallaxBackground(
				new ParallaxLayer[] {
						new ParallaxLayer(Assets.platform_bottom, new Vector2(
								1f, 0), new Vector2(0, 0), new Vector2(0, 0)),
						new ParallaxLayer(Assets.platform_bottom, new Vector2(
								1f, 0), new Vector2(0, 0), new Vector2(
								Assets.platform_bottom.getRegionWidth(), 0)), },
				BaseScreen.SCREEN_WIDTH, BaseScreen.SCREEN_HEIGHT,
				new Vector2(300, 0));

		logo = assetManager.get(ASSETS_DIR, TextureAtlas.class).findRegion(
				"logo");

		black = assetManager.get(ASSETS_DIR, TextureAtlas.class).findRegion(
				"black");
		bird1 = assetManager.get(ASSETS_DIR, TextureAtlas.class).findRegion(
				"bird1");
		flappyCatAnimation = new Animation(0.15f, bird1, assetManager.get(
				ASSETS_DIR, TextureAtlas.class).findRegion("bird2"));
		flappyCatAnimation.setPlayMode(Animation.PlayMode.LOOP);
		play_btn = assetManager.get(ASSETS_DIR, TextureAtlas.class)
				.createSprite("btn_play");
		scores_btn = assetManager.get(ASSETS_DIR, TextureAtlas.class)
				.findRegion("btn_score");

		dialog = assetManager.get(ASSETS_DIR, TextureAtlas.class).findRegion(
				"dialog");

		game_over_play_btn = assetManager.get(ASSETS_DIR, TextureAtlas.class)
				.findRegion("go_play_btn");
		game_over_score_btn = assetManager.get(ASSETS_DIR, TextureAtlas.class)
				.findRegion("go_scores_btn");
		game_over = assetManager.get(ASSETS_DIR, TextureAtlas.class)
				.findRegion("game_over");
		get_ready = assetManager.get(ASSETS_DIR, TextureAtlas.class)
				.findRegion("get_ready");
		tap_start = assetManager.get(ASSETS_DIR, TextureAtlas.class)
				.findRegion("tap_start");
		white = assetManager.get(ASSETS_DIR, TextureAtlas.class).findRegion(
				"white");

		FontManager.load();

		groan = assetManager.get("data/groan.mp3", Sound.class);
		punch = assetManager.get("data/punch.mp3", Sound.class);
		flap = assetManager.get("data/flap.mp3", Sound.class);
		coin = assetManager.get("data/coin.mp3", Sound.class);
	}

	public static Texture fetchTexture(String name) {
		if (assetManager.isLoaded(name))
			return (Texture) assetManager.get(name, Texture.class);
		return null;
	}

	public static void playSound(Sound sound) {
		if (MUSIC)
			sound.play(SOUND_VOLUME);
	}

	public static void vibrate() {
		if (VIBRATE && Gdx.app.getType() == ApplicationType.Android)
			Gdx.input.vibrate(200);
	}

	private static void unloadAssetManager() {
		// assetManager.unload(ASSETS_DIR);
		// FontManager.unloadAssets();
		assetManager.dispose();
		assetManager = null;
	}

	public static Texture setFilter(Texture texture) {
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		return texture;
	}

	public static void load() {
		createAssetManager();
	}

	public static void dispose() {
		unloadAssetManager();
		// FontManager.dispose();
	}
}
