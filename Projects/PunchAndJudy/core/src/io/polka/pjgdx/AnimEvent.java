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
			System.out.println("FAST");
		}
		else if (anim.equals("medium")) {
			actor.setSpeed("medium");
			System.out.println("MEDIUM");
		}
		else if (anim.equals("slow")) {
			actor.setSpeed("slow");
			System.out.println("SLOW");
		}
		else {
                actor.setCurrentAnim(anim);
		}
	}

}
