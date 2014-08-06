package io.polka.pjgdx;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class EventHandler {
Timer tim;
	EventHandler() {
		tim = new Timer();
		
	}
	
	void addEvent(final Event ev) {
		Task eventTask = new Task(){
		    @Override
		    public void run() {
		    	//System.out.println("event triggered!");
		    	ev.trigger();
		    }
		};
		tim.scheduleTask(eventTask, ev.delay);
		
	}

}
