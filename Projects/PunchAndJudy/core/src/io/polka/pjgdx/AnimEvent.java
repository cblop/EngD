package io.polka.pjgdx;

public class AnimEvent extends Event {
	String anim;

	public AnimEvent(Puppet act, float del, String anm) {
		super(act, del);
		anim = anm;
	}
	
	@Override
	protected void trigger() {
		actor.setCurrentAnim(anim);
	}

}
