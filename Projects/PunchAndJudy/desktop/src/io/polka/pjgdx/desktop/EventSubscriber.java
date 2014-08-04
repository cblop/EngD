package io.polka.pjgdx.desktop;

import java.io.Serializable;
import java.net.UnknownHostException;
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
	private boolean debug;
	private EventLogger logger;
	public EventSubscriber(String server, String jid, String password, DesktopLauncher launch, boolean dbg) throws XMPPException {
		super(server, jid, password);
		launcher = launch;
		debug = dbg;
		
		if (debug) {
			startLogger();
		}
	}
	
	private void startLogger() {
		try {
			logger = new EventLogger();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void subscribeTo(String nodeName) throws XMPPException 
	{
		// TODO Auto-generated method stub
		//SensorClient sc = new SensorClient("172.16.125.2", "user3", "bathstudent");
		System.out.println("Setting up subscriber...");
		
		addHandler(nodeName, new ReadingHandler() {
			public void handleIncomingReading (String node, String rdf) {
				//System.out.println("Handling reading...");

        //System.out.println(rdf);

				try {
					JsonReading jr = new JsonReading();
					jr.fromJSON(rdf);
					
					if (debug) {
						logger.logJson(rdf);
					}
					
					//System.out.println(rdf);
					
					Value agname = jr.findValue("AGENT");
					Value functor = jr.findValue("FUNCTOR"); 
					Value value = jr.findValue("VALUE"); 
					
					
					if (functor != null && value != null && agname != null) {
                            // How do I do this safely?
					        //@SuppressWarnings("unchecked")
					        //LinkedList<String> termList = (LinkedList<String>) terms.m_object;
					
                            String ftemp = functor.m_object.toString();
                            //System.out.println("Functor: " + ftemp);
                                

                            /*
                            System.out.println("Terms: ");
                            for (String str : termList) {
                                    System.out.println(str);
                            }
                            */
                            String vtemp = value.m_object.toString();
                            //System.out.println("Value: " + vtemp);

                            String atemp = agname.m_object.toString();
                            //System.out.println("From: " + atemp);

                            if (functor.m_object.toString().equals("place")) {
                                    launcher.pshow.addEvent("move", agname.m_object.toString(), 0, value.m_object.toString());
                            }

                            if (functor.m_object.toString().equals("anim")) {
                                    launcher.pshow.addEvent("anim", agname.m_object.toString(), 0, value.m_object.toString());
                            }

                            if (functor.m_object.toString().equals("nextScene")) {
                            	RunShow.nextScene();
                            }

                            if (functor.m_object.toString().equals("emotion")) {
                                    launcher.pshow.addEvent("emotion", agname.m_object.toString(), 0, value.m_object.toString());
                            }
                            
                            if (functor.m_object.toString().equals("move")) {
                                    //System.out.println(values.get(0).toString());
                                    launcher.pshow.addEvent("move", agname.m_object.toString(), 0, value.m_object.toString());
                            }

                            if (functor.m_object.toString().equals("say")) {
                                    //System.out.println(values.get(0).toString());
                                    launcher.pshow.addEvent("speak", agname.m_object.toString(), 0, value.m_object.toString());
                                    // Something needs to be published that an agent is speaking
                            }
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
