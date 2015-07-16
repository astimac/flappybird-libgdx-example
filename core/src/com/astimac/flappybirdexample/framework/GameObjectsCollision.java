package com.astimac.flappybirdexample.framework;

import com.astimac.flappybirdexample.controller.GameWorld;
import com.astimac.flappybirdexample.models.Bird;
import com.astimac.flappybirdexample.models.Counter;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameObjectsCollision implements ContactListener {

	private GameWorld gameWorld;

	public GameObjectsCollision(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	private void beginContactWithFlappyBird(Fixture fixture1, Fixture fixture2) {
		Bird bird = (Bird) fixture1.getBody().getUserData();
		Object object = fixture2.getBody().getUserData();
		if (object instanceof Counter) {
			Counter counter = (Counter) object;
			if (counter.state == Counter.STATE_NORMAL) {
				counter.state = Counter.STATE_DESTROY;
				gameWorld.listener.onHitCounter();
				gameWorld.counterScore += 1;
			}
		} else {
			if (bird.state == Bird.STATE_NORMAL) {
				gameWorld.listener.onHitObstacle();
				bird.getHurt();
			}
		}

	}

	@Override
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();

		if (fixtureA.getBody().getUserData() instanceof Bird) {
			beginContactWithFlappyBird(fixtureA, fixtureB);
		} else if (fixtureB.getBody().getUserData() instanceof Bird) {
			beginContactWithFlappyBird(fixtureB, fixtureA);
		} else {
			// no op
		}

	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
