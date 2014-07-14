package io.polka.pjgdx.desktop;

import jason.asSyntax.Term;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;

import edu.bath.sensorframework.JsonReading;
import edu.bath.sensorframework.sensor.Sensor;

public class EventPublisher extends Sensor {
	public EventPublisher(String server, String username, String password, String node) throws XMPPException {
		super(server, username, password, node);
	}
	public EventPublisher(Connection connection, String username, String password, String node) throws XMPPException {
		super(connection, username, password, node);
	}
	public void publishEvent(String agname, String functor, List<Term> values) throws UnsupportedEncodingException {
		//System.out.println("Publishing event: " + evnt);
        JsonReading js = new JsonReading();
        
        ArrayList<String> valList = new ArrayList<String>();
        
        for (Term t : values) {
        	valList.add(t.toString());
        }
        js.addValue("AGENT", agname);
        js.addValue("FUNCTOR", functor);
        js.addValue("TERMS", valList);
		
		publish(js);
	}

}
