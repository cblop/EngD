package io.polka.pjgdx.desktop;

import jason.JasonException;
import jason.architecture.AgArch;
import jason.asSemantics.ActionExec;
import jason.asSemantics.Agent;
import jason.asSemantics.Circumstance;
import jason.asSemantics.TransitionSystem;
import jason.asSyntax.Literal;
import jason.infra.centralised.RunCentralisedMAS;
import jason.runtime.Settings;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;

import edu.bath.sensorframework.client.SensorClient;
import edu.bath.sensorframework.JsonReading;
import edu.bath.sensorframework.JsonReading.Value;
import edu.bath.sensorframework.client.ReadingHandler;
import edu.bath.sensorframework.sensor.Sensor;

public class BsfAgent extends AgArch {
	
	// Make sure to implement JeeHang's NormReadingHandler
	private static Logger m_logger = Logger.getLogger(BsfAgent.class.getName());

	// initial information for xmpp connection
	private String m_name;
	private String m_server;
	private String m_pwd;
	private String m_aslpath;

	// Percept buffer
	//private ArrayList<Literal> m_percept;
	public String m_percept;
	
	// sensorclients
	private SensorClient m_sc;
	private EventPublisher m_pub;

	/*
	public static void main(String[] args) {
		RunCentralisedMAS.setupLogger();
		
		
		String[] testArgs = {"localhost", "user1", "testuser1", "punch-bsf.asl"};
				
		BsfAgent bsfa = new BsfAgent(testArgs);
		
		bsfa.run();
	}
*/

	
	public BsfAgent(String server, String jid, String password, String aslfile, String nodeName) throws JasonException {
		String path = Paths.get("").toAbsolutePath().toString();
		m_aslpath = path + "\\asl\\" + aslfile;
		m_name = jid;
        try {
			m_sc = new SensorClient(server, jid, password);
            //m_pub = new EventPublisher(m_sc.getConnection(), "user4", "testuser4", nodeName);
            m_pub = new EventPublisher(m_sc.getConnection(), jid, password, nodeName);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initSensors();
		makeAgent();

	}

	public BsfAgent(Connection conn, String jid, String password, String aslfile, String nodeName) throws JasonException {
		String path = Paths.get("").toAbsolutePath().toString();
		m_aslpath = path + "\\asl\\" + aslfile;
		m_name = jid;
        try {
			m_sc = new SensorClient(conn, jid, password);
            //m_pub = new EventPublisher(m_sc.getConnection(), "user1", "testuser1", nodeName);
            m_pub = new EventPublisher(m_sc.getConnection(), jid, password, nodeName);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initSensors();
		makeAgent();

	}
	
	private void makeAgent() throws JasonException {
			// Create agent for reasoning
			Agent ag = new Agent();
			new TransitionSystem(ag, new Circumstance(), new Settings(), this);
			ag.initAg(m_aslpath);
	}

	
	private void initSensors()
	{
		try {
			//m_pub = new EventPublisher(m_server, "user2", "testuser2", "EVENTS");
			
			
			// m_sc.addHandler("NODE_NORM", new ReadingHandler() {
			m_sc.addHandler("perceptnode", new ReadingHandler() {
				public void handleIncomingReading(String node, String rdf) {
					try {
						JsonReading jr = new JsonReading();
						jr.fromJSON(rdf);
						Value val = jr.findValue("EVENT");
						if (val != null) {
							/*
							System.out.println(val.m_object);		
							String lit = "obl(at("+ jr.findValue("posX").m_object+","+jr.findValue("posY").m_object +"),"+ jr.findValue("deadline").m_object + ",violation)";
							m_percept = lit;
							*/
							System.out.println(val);
							//m_percept = val.m_object.toString();
						}
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			// SensorClient for percepts
			m_sc.subscribe("perceptnode");
		} catch (XMPPException xe) {
			System.out.println("failed to subscribe: " + "perceptnode");
		}
	}

	public void run() {
		try	{
			while (isRunning())	{
				//if (m_percept != null) {
					// calls the jason engine to perform one reasoning cycle
					m_logger.fine("in reasoning");
					getTS().reasoningCycle();
				//}
			}
		}
		catch (Exception e)	{
			m_logger.log(Level.SEVERE, "Run error", e);
		}
	}
	
	public String getAgName() {
		return m_name;
	}
	
	// this method just add some perception for the agent
    @Override
    public List<Literal> perceive() {
        List<Literal> l = new ArrayList<Literal>();
        if (m_percept != null) {
	    	l.add(Literal.parseLiteral(m_percept));
        }
        return l;
    }

    // this method get the agent actions
    @Override
    public void act(ActionExec action, List<ActionExec> feedback) {
    	getTS().getLogger().info("Agent " + getAgName() + " is doing: " + action.getActionTerm());
        String act = action.getActionTerm().toString();

        if (act.isEmpty() == false)	{
			System.out.println(this.getAgName() + ": " + "action string: " + act);
			m_percept = null;

			try {
				m_pub.publishEvent(getAgName(), action.getActionTerm().getFunctor(), action.getActionTerm().getTerms());
			} catch (UnsupportedEncodingException e) {
				System.out.println("Failed to publish action: " + act);
			}
		}
        
        // set that the execution was ok
        action.setResult(true);
        feedback.add(action);
    }

    @Override
    public boolean canSleep() {
        return true;
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    // a very simple implementation of sleep
    @Override
    public void sleep() {
        try {
        	Thread.sleep(2000);
        } catch (InterruptedException e) {
        	// no-op
        }
    }
    
    // Not used methods
    // This simple agent does not need messages/control/...
    @Override
    public void sendMsg(jason.asSemantics.Message m) throws Exception {
    	
    }

    @Override
    public void broadcast(jason.asSemantics.Message m) throws Exception {
    	
    }

    @Override
    public void checkMail() {
    	
    }	

}
