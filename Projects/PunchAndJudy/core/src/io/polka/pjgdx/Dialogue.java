package io.polka.pjgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Dialogue {
	private Sound audio;
	private String subtitle;

	Dialogue(String filename, String sub) {
		audio = Gdx.audio.newSound(Gdx.files.internal(filename));
		subtitle = sub;
		
	}
	
	public void trigger () {
		audio.play(1.0f);
		
	}

	

	public String getSubtitle() {
		return subtitle;
	}

}
