package com.astimac.flappybirdexample.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Tubes {
	
	public static float WIDTH = 88f/100f;
	public static float HEIGHT = 526f/100f;
	
	public static int STATE_NORMAL = 0;	
	public static int STATE_DESTROY = 1;

	public static int TYPE_UP = 1;
	public static int TYPE_DOWN = 0;
	
	public static float VELOCITY_X = -3.0f;

	public Vector2 position;
	public int state;
	public float stateTime;
	public int type;

	public Tubes(float x, float y, int type) {
		this.position = new Vector2(x, y);
		this.stateTime = 0.0F;
		this.state = STATE_NORMAL;
		this.type = type;
	}

	public void update(float delta, Body body) {
		this.position.x = body.getPosition().x;
		this.position.y = body.getPosition().y;
		this.stateTime += delta;
	}
}
