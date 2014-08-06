package io.polka.pjgdx;

public class StateEvent extends Event {
	String state;
	
	public StateEvent (Puppet act, float del, String st) {
		super(act, del);
		state = st;
	}
	
	@Override
	protected void trigger() {
		actor.setEmotion(state);
	}

}
