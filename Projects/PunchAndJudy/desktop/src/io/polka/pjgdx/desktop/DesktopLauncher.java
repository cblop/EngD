package io.polka.pjgdx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.polka.pjgdx.PuppetShow;

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
