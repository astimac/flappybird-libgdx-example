package com.astimac.flappybirdexample.framework;

import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FontManager {

	public static final String FONT_DIR_PATH = "data/font/";

	public static BitmapFont fontMarker;
	
	static BitmapFontParameter bitmapFontParam = new BitmapFontParameter();

    public static boolean areFontsLoaded;
	
	public static void loadAssets() {
		bitmapFontParam.magFilter = TextureFilter.Linear;
		bitmapFontParam.minFilter = TextureFilter.Linear;
		
		Assets.assetManager.load(
				FONT_DIR_PATH + "oij.fnt", 
				BitmapFont.class,
				bitmapFontParam
		);

		Assets.assetManager.load(FONT_DIR_PATH + "oij.png", Texture.class);
	}
	


	public static boolean isLoaded() {
		if (null == Assets.assetManager)  {
			return false;
		}
		
		areFontsLoaded = true;

		if (!Assets.assetManager.isLoaded(FONT_DIR_PATH + "oij.fnt")) {
			areFontsLoaded = false;
		}
		if (!Assets.assetManager.isLoaded(FONT_DIR_PATH + "oij.png")) {
			areFontsLoaded = false;
		}
		
		return areFontsLoaded;
	}
	
	public static void unloadAssets() {
		Assets.assetManager.unload(FONT_DIR_PATH + "oij.fnt");
		Assets.assetManager.unload(FONT_DIR_PATH + "oij.png");
	}

	public static void load () {
		fontMarker = Assets.assetManager.get(FONT_DIR_PATH + "oij.fnt", BitmapFont.class);
		fontMarker.setColor(Color.WHITE);
	}
	
	public static void dispose() {
		fontMarker.dispose();
	}
}