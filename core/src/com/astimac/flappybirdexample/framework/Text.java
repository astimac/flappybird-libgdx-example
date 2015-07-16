package com.astimac.flappybirdexample.framework;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Text extends Actor {
	
	private String text;
	private float x;
	private float y;
	private Color color;
	
	public Text(String text, float x, float y, Color color) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		FontManager.fontMarker.setColor(color);
		FontManager.fontMarker.draw(batch, text, x, y);
	}
	
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		return null;
	}

}
