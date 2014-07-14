package io.polka.pjgdx.desktop;

import jason.JasonException;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;

public class RunShow {
	private static DesktopLauncher launcher;

	public static void main(String[] args) throws XMPPException, JasonException {
		launcher = new DesktopLauncher();
		
		new Thread() {
			public void run() {
				launcher.run();
			}
		}.start();
		
		String eventNode = "xnode";
		
        EventSubscriber esub;
		//esub = new EventSubscriber("localhost", "punch", "punchuser");
		//esub = new EventSubscriber("localhost", "judy", "judyuser");
		esub = new EventSubscriber("localhost", "anim", "animuser", launcher);
		Connection conn = esub.getConnection();
		esub.subscribeTo(eventNode);
		
		BsfAgent punchAgent = new BsfAgent("localhost", "punch", "punchuser", "punch-bsf.asl", eventNode);
		//BsfAgent punchAgent = new BsfAgent(conn, "judy", "judyuser", "punch-bsf.asl", eventNote);
		punchAgent.run();
		
	}

}
