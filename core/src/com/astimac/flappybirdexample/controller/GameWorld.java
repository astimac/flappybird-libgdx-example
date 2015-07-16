package com.astimac.flappybirdexample.controller;

import java.util.Random;
import com.astimac.flappybirdexample.framework.Assets;
import com.astimac.flappybirdexample.framework.GameObjectsCollision;
import com.astimac.flappybirdexample.models.Bird;
import com.astimac.flappybirdexample.models.Counter;
import com.astimac.flappybirdexample.models.Tubes;
import com.astimac.flappybirdexample.screens.BaseScreen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class GameWorld {

	/**
	 * STATES
	 */
	public static final int STATE_GAMEOVER = 1;
	public static final int STATE_RUNNING = 2;

	private static final float PIPE_SPAWN = 0.99f;
	
	public interface GameWorldListener {
		void onHitObstacle();
		void onHitCounter();
	}
	
	public GameWorldListener listener;
	
	/**
	 * GAME STATE
	 */
	public int state;

	/**
	 * BOX2D WORLD WITH GRAVITY
	 */
	public World box2dWorld = new World(new Vector2(0.0F, -18f), true);

	/**
	 * TIME TO SPAWN TUBE
	 */
	private float pipeSpawnTime;

	/**
	 * PLAYER BIRD
	 */
	public Bird bird;

	/**
	 * PLAYER SCORE
	 */
	public int counterScore;

	/**
	 * ARRAY OF BOX2D BODIES
	 */
	private Array<Body> bodyArray;

	/**
	 * ARRAY OF TUBES
	 */
	public Array<Tubes> tubesArray;

	private Random mRandom;

	public GameWorld(GameWorldListener listener) {
		
		this.counterScore = 0;
		
		this.listener = listener;
		
		this.box2dWorld.setContactListener(new GameObjectsCollision(this));

		this.tubesArray = new Array<Tubes>();
		this.bodyArray = new Array<Body>();

		//1.5f
		this.pipeSpawnTime = 0f;

		this.mRandom = new Random();

		doCreateBird();
		doCreateBottomMargin();
		doCreateTopMargin();
	}

	private void doGenerateTubes() {
		float f = 0.5F + 2.5F * mRandom.nextFloat();
		Tubes mTubes = new Tubes(7.0F, f, Tubes.TYPE_DOWN);
		BodyDef mBodyDef1 = new BodyDef();
		mBodyDef1.position.x = mTubes.position.x;
		mBodyDef1.position.y = mTubes.position.y;
		mBodyDef1.type = BodyDef.BodyType.KinematicBody;
		Body mBody = box2dWorld.createBody(mBodyDef1);
		PolygonShape mShape = new PolygonShape();
		mShape.setAsBox(Tubes.WIDTH / 2.0F, Tubes.HEIGHT / 2.0F);
		FixtureDef mFixture = new FixtureDef();
		mFixture.shape = mShape;
		mFixture.density = 0.0F;
		mFixture.restitution = 0.0F;
		mFixture.friction = 0.0F;
		mBody.createFixture(mFixture);
		mBody.setFixedRotation(true);
		mBody.setUserData(mTubes);
		this.tubesArray.add(mTubes);

		Tubes mTubes2 = new Tubes(7.0F, 1.7F + f + Tubes.HEIGHT,
				Tubes.TYPE_UP);
		mBodyDef1.position.x = mTubes2.position.x;
		mBodyDef1.position.y = mTubes2.position.y;
		Body mBody2 = box2dWorld.createBody(mBodyDef1);
		mBody2.createFixture(mFixture);
		mBody2.setUserData(mTubes2);
		mBody2.setFixedRotation(true);
		this.tubesArray.add(mTubes2);

		BodyDef mBodyDef2 = new BodyDef();
		mBodyDef2.position.x = mTubes2.position.x;
		mBodyDef2.position.y = (mTubes2.position.y - Counter.HEIGHT
				/ 2.0F - Tubes.HEIGHT / 2.0F - 0.035F);
		mBodyDef2.type = BodyDef.BodyType.KinematicBody;
		Body mBody3 = box2dWorld.createBody(mBodyDef2);
		PolygonShape mShape2 = new PolygonShape();
		mShape2.setAsBox(Counter.WIDTH / 2.0F, Counter.HEIGHT / 2.0F);
		mFixture.isSensor = true;
		mFixture.shape = mShape2;
		mBody3.createFixture(mFixture);
		mBody3.setUserData(new Counter());
		mBody3.setFixedRotation(true);

		mShape.dispose();
		mShape2.dispose();
	}

	private void doDestroyObject() {
		this.box2dWorld.getBodies(this.bodyArray);
		for (Body b : bodyArray) {
			if (b.getUserData() instanceof Bird) {
				Bird bird = (Bird) b.getUserData();
				if (bird.state == Bird.STATE_DEAD) {
					this.box2dWorld.destroyBody(b);
					this.state = STATE_GAMEOVER;
				}
			}

			if (b.getUserData() instanceof Tubes) {
				Tubes tubes = (Tubes) b.getUserData();
				if (tubes.state == Tubes.STATE_DESTROY) {
					this.tubesArray.removeValue(tubes, true);
					this.box2dWorld.destroyBody(b);
				}
			}

			if (b.getUserData() instanceof Counter) {
				Counter counter = (Counter) b.getUserData();
				if (counter.state == Counter.STATE_DESTROY) {
					this.box2dWorld.destroyBody(b);
				}
			}
		}
	}

	private void doCreateBird() {

		this.bird = new Bird(1.25F, BaseScreen.WORLD_SCREEN_HEIGHT/2 - Bird.HEIGHT/2);

		/**
		 * BIRD BODY DEF
		 */
		BodyDef birdBodyDef = new BodyDef();
		birdBodyDef.position.x = this.bird.position.x;
		birdBodyDef.position.y = this.bird.position.y;
		birdBodyDef.type = BodyDef.BodyType.DynamicBody;

		/**
		 * PHYSICS BODY
		 */
		Body birdBody = box2dWorld.createBody(birdBodyDef);

		/**
		 * BODY SHAPE
		 */
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(Bird.WIDTH/3.8f);

		/**
		 * FIXTURE
		 */
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;
		fixtureDef.density = 8.0F;
		fixtureDef.restitution = 0.11F;
		fixtureDef.friction = 0.25F;

		/**
		 * CREATE BODY
		 */
		birdBody.createFixture(fixtureDef);
		birdBody.setFixedRotation(true);
		birdBody.setUserData(this.bird);
		birdBody.setBullet(true);

		circleShape.dispose();
	}

	private void doCreateTopMargin() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.x = 0.0F;
		bodyDef.position.y = BaseScreen.WORLD_SCREEN_HEIGHT;
		bodyDef.type = BodyDef.BodyType.StaticBody;

		Body body = box2dWorld.createBody(bodyDef);
		EdgeShape edge = new EdgeShape();
		edge.set(0.0F, 0.0F, BaseScreen.WORLD_SCREEN_WIDTH, 0.0F);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = edge;
		fixtureDef.density = 0.0F;
		fixtureDef.restitution = 0.0F;
		fixtureDef.friction = 0.0F;

		body.createFixture(fixtureDef);
		body.setFixedRotation(true);

		edge.dispose();
	}

	private void doCreateBottomMargin() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.x = 0.0F;
		bodyDef.position.y = Assets.platform_bottom.getRegionHeight() / 100f;
		bodyDef.type = BodyDef.BodyType.StaticBody;

		Body body = box2dWorld.createBody(bodyDef);

		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(0.0F, 0.0F, BaseScreen.WORLD_SCREEN_WIDTH, 0.0F);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = edgeShape;
		fixtureDef.density = 0.0F;
		fixtureDef.restitution = 0.0F;
		fixtureDef.friction = 0.0F;

		body.createFixture(fixtureDef);
		body.setFixedRotation(true);

		edgeShape.dispose();
	}

	private void updateCounter(Body body, float delta) {
		if (this.bird.state == Bird.STATE_NORMAL) {
			Counter counter = (Counter) body.getUserData();
			if (counter.position.x <= -5.0F)
				counter.state = Counter.STATE_DESTROY;
			body.setLinearVelocity(Counter.VELOCITY_X, 0.0F);
			return;
		}
		body.setLinearVelocity(0.0F, 0.0F);
	}

	private void updateFlappyBird(Body body, float delta, boolean jump) {
		Bird bird = (Bird) body.getUserData();
		bird.update(delta, body);

		if ((jump) && (bird.state == Bird.STATE_NORMAL)) {
			body.setLinearVelocity(0.0F, Bird.JUMP_VELOCITY);
			return;
		}

		body.setLinearVelocity(0.0F, body.getLinearVelocity().y);
	}

	private void updateTubes(Body body, float delta) {
		if (this.bird.state == Bird.STATE_NORMAL) {
			Tubes tubes = (Tubes) body.getUserData();

			if (tubes != null) {
				tubes.update(delta, body);
				if (tubes.position.x <= -5.0F)
					tubes.state = Tubes.STATE_DESTROY;
				body.setLinearVelocity(Tubes.VELOCITY_X, 0.0F);
			}

			return;
		}

		body.setLinearVelocity(0.0F, 0.0F);
	}

	public void update(float delta, boolean canJump) {
		this.box2dWorld.step(delta, 8, 3);
		doDestroyObject();

		this.pipeSpawnTime += delta;

		if (this.pipeSpawnTime >= PIPE_SPAWN) {
			this.pipeSpawnTime = 0;
			doGenerateTubes();
		}

		this.box2dWorld.getBodies(this.bodyArray);

		for (Body b : bodyArray) {
			if (b.getUserData() instanceof Bird) {
				updateFlappyBird(b, delta, canJump);
			}

			if (b.getUserData() instanceof Tubes) {
				updateTubes(b, delta);
			}

			if (b.getUserData() instanceof Counter) {
				updateCounter(b, delta);
			}
		}

	}

}
