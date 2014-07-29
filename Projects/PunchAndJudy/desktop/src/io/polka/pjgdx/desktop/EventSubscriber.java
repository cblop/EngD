package io.polka.pjgdx.desktop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jivesoftware.smack.XMPPException;








//import edu.bath.sensorframework.DataReading;
//import edu.bath.sensorframework.DataReading.Value;
import edu.bath.sensorframework.JsonReading;
import edu.bath.sensorframework.JsonReading.Value;
import edu.bath.sensorframework.client.ReadingHandler;
import edu.bath.sensorframework.client.SensorClient;

// Why not just use SensorClient? Because I might have to add custom stuff
public class EventSubscriber extends SensorClient {
	private DesktopLauncher launcher;
	public EventSubscriber(String server, String jid, String password, DesktopLauncher launch) throws XMPPException {
		super(server, jid, password);
		launcher = launch;
	}
	
	public void subscribeTo(String nodeName) throws XMPPException 
	{
		// TODO Auto-generated method stub
		//SensorClient sc = new SensorClient("172.16.125.2", "user3", "bathstudent");
		System.out.println("Setting up subscriber...");
		
		addHandler(nodeName, new ReadingHandler() {
			public void handleIncomingReading (String node, String rdf) {
				//System.out.println("Handling reading...");
				try {
					JsonReading jr = new JsonReading();
					jr.fromJSON(rdf);
					
					//System.out.println(rdf);
					
					Value agname = jr.findValue("AGENT");
					Value functor = jr.findValue("FUNCTOR"); 
					Value terms = jr.findValue("TERMS"); 
					
					// How do I do this safely?
					@SuppressWarnings("unchecked")
					LinkedList<String> termList = (LinkedList<String>) terms.m_object;
					
					
					if (functor != null)
					{
						String temp = functor.m_object.toString();
						//System.out.println("Functor: " + temp);
					}

					if (terms != null)
					{
						//System.out.println("Terms: ");
						/*
						for (String str : termList) {
							System.out.println(str);
						}
						*/
					}

					if (agname != null)
					{
						String temp = agname.m_object.toString();
						//System.out.println("From: " + temp);
					}

                    if (functor.m_object.toString().equals("place")) {
                            launcher.pshow.addEvent("move", agname.m_object.toString(), 0, termList.getFirst());
                    }

                    if (functor.m_object.toString().equals("anim")) {
                            launcher.pshow.addEvent("anim", agname.m_object.toString(), 0, termList.getFirst());
                    }
                    
                    if (functor.m_object.toString().equals("move")) {
                            //System.out.println(values.get(0).toString());
                            launcher.pshow.addEvent("move", agname.m_object.toString(), 0, termList.getFirst());
                    }

                    if (functor.m_object.toString().equals("say")) {
                            //System.out.println(values.get(0).toString());
                            launcher.pshow.addEvent("speak", agname.m_object.toString(), 0, termList.getFirst());
                            // Something needs to be published that an agent is speaking
                    }
                    
					
					/*
					DataReading dr = DataReading.fromRDF(rdf);
					Value val = dr.findFirstValue(null,"http://127.0.0.1/sensors/type#requestString", null);
					
					if (val != null)
					{
						String temp = (String) val.object.toString();
						System.out.println(temp);
					}
					*/
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		subscribe(nodeName);
		System.out.println("Subscribed to " + nodeName);
		
		
	}

}
