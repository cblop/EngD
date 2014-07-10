package io.polka.pjgdx;

import com.badlogic.gdx.math.Vector2;

public class MoveEvent extends Event {
	Vector2 target;
	public MoveEvent(Puppet act, float delay, Vector2 targ) {
		super(act, delay);
		target = targ;
	}
	
	@Override
	protected void trigger() {
		actor.moveTo(target);
	}

}
