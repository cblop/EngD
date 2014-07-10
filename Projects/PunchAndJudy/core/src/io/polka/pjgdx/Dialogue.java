package io.polka.pjgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Dialogue {
	Sound audio;
	String subtitle;
	Dialogue(String filename, String sub) {
		audio = Gdx.audio.newSound(Gdx.files.internal(filename));
		subtitle = sub;
		
	}
	
	public void trigger () {
		audio.play(1.0f);
	}

}
