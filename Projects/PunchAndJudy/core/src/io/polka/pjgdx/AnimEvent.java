package io.polka.pjgdx;

public class AnimEvent extends Event {
	String anim;

	public AnimEvent(Puppet act, float del, String anm) {
		super(act, del);
		anim = anm;
	}
	
	@Override
	protected void trigger() {
		if (anim.equals("turn")) {
			actor.turn();
		}
		else if (anim.equals("fast")) {
			actor.setSpeed("fast");
		}
		else if (anim.equals("medium")) {
			actor.setSpeed("medium");
		}
		else if (anim.equals("slow")) {
			actor.setSpeed("slow");
		}
		else {
      actor.setCurrentAnim(anim);
		}
	}

}
