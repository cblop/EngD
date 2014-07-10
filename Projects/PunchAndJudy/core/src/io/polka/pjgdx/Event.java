package io.polka.pjgdx;

public class Event {
	protected Puppet actor;
	protected float delay;

	public Event(Puppet act, float dly) {
		actor = act;
		delay = dly;
		
	}
	
	protected void trigger() {
		
	}

}
