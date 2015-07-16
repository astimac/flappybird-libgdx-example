package com.astimac.flappybirdexample.android;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.astimac.flappybirdexample.framework.ActionResolver;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.astimac.flappybirdexample.FlappyBirdExampleGame;

public class AndroidLauncher extends AndroidApplication implements ActionResolver {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			hideVirtualButtons();
		}

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		initialize(new FlappyBirdExampleGame(this), config);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			// In KITKAT (4.4) and next releases, hide the virtual buttons
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				hideVirtualButtons();
			}
		}
	}

	@TargetApi(19)
	private void hideVirtualButtons() {
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	}

	@Override
	public void loadAd() {
		//we can load Ads here.
	}
}
