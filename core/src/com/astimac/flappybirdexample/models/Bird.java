package com.astimac.flappybirdexample.models;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Bird {

	public static final float WIDTH = 62.4f / 100f;
	public static final float HEIGHT = 58.792f / 100f;

	public static final int STATE_NORMAL = 0;
	public static final int STATE_HURT = 1;
	public static final int STATE_DEAD = 2;

	public static final float HURT_TIME = 0.5F;
	public static final float DEAD_TIME = 0.75F;

	public static float JUMP_VELOCITY = 5.5F;
	private final int MAX_ANGLE_DEGREES = 15;
	public float angleRad;
	public Vector2 position;
	public int state;
	public float stateTime;

	private float radians;

	public Bird(float x, float y) {
		this.position = new Vector2(x, y);
		this.state = STATE_NORMAL;
		this.stateTime = 0;
		this.angleRad = 0;
		this.radians = (float) Math.toRadians(MAX_ANGLE_DEGREES);
	}

	public void die() {
		this.state = STATE_DEAD;
		this.stateTime = 0;
	}

	public void getHurt() {
		this.state = STATE_HURT;
		this.stateTime = 0;
	}

	private void _clampRotation() {
		if (angleRad > radians) {
			angleRad = radians;
		} else if (angleRad < -radians) {
			angleRad = -radians;
		}
	}

	public void update(float delta, Body body) {
		if (body != null) {
			this.position.x = body.getPosition().x;
			this.position.y = body.getPosition().y;

			switch (state) {
			case STATE_NORMAL:
				if(body.getLinearVelocity().y > 0)
					this.angleRad += (1.125f + delta )* MathUtils.degreesToRadians;
				else
					this.angleRad -= (1.125f + delta)  * MathUtils.degreesToRadians;
				_clampRotation();
				break;
			case STATE_HURT:
				this.angleRad = body.getLinearVelocity().y + stateTime;
				break;
			default:
				break;
			}

			this.stateTime += delta;
		}

	}
}
