package io.polka.pjgdx.desktop;

import io.polka.pjgdx.PuppetShow;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public PuppetShow pshow;
	public DesktopLauncher() {
		pshow = new PuppetShow();
	}
	
	public void run(){
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(pshow, config);
	}
	
}
