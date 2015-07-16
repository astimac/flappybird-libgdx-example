package com.astimac.flappybirdexample.models;

import com.astimac.flappybirdexample.framework.Assets;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Counter {
	
	  public static float HEIGHT = 1.5f;
	  public static float WIDTH = Assets.pipeDown.getRegionWidth()/100f;
	  
	  public static int STATE_NORMAL = 0;
	  public static int STATE_DESTROY = 1;
	  
	  public static float VELOCITY_X = Tubes.VELOCITY_X;

	  public Vector2 position = new Vector2();
	  
	  public int state = STATE_NORMAL;
	  
	  public float stateTime = 0;

	  public void update(float delta, Body body) {
	    this.position.x = body.getPosition().x;
	    this.position.y = body.getPosition().y;
	    this.stateTime += delta;
	  }
}
