package io.polka.pjgdx;

public class SpeakEvent extends Event {
	String mood;


	public SpeakEvent (Puppet act, float del, String md) {
		super(act, del);
		mood = md;
	}
	
	@Override
	protected void trigger() {
		actor.sayLine(mood);
	}

}
