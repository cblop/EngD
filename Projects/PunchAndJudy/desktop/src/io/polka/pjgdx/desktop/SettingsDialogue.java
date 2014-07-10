package io.polka.pjgdx.desktop;
import jason.JasonException;
import jason.infra.centralised.RunCentralisedMAS;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SettingsDialogue extends JFrame implements ActionListener, ChangeListener {
//public class SettingsDialogue extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private static SettingsDialogue settingsGui;
    public static HashMap<String, Integer> settingsMap = new HashMap<String, Integer>();
	JPanel mainPanel;
	JPanel settingsPanel;
	JPanel charPanel;
	JButton startbutton;
	JLabel sceneLabel;
	JLabel agentLabel;
	JLabel punchAngerLabel;
	JLabel judyHealthLabel;
	JLabel joeyHappyLabel;
	JCheckBox audienceCheck;
	JCheckBox punchCheck;
	JCheckBox judyCheck;
	JCheckBox joeyCheck;
	JSlider numScenes;
	JSlider punchAngerLevel;
	JSlider judyHealthLevel;
	JSlider joeyHappyLevel;
    //Env env = new Env();
	
	public SettingsDialogue() {
		mainPanel = new JPanel(new GridLayout(0,1));
		settingsPanel = new JPanel(new FlowLayout());
		charPanel = new JPanel(new FlowLayout());
		numScenes = new JSlider(JSlider.HORIZONTAL, 1, 6, 2);
		punchAngerLevel = new JSlider(JSlider.HORIZONTAL, 0, 10, 0);
		punchAngerLevel.setMajorTickSpacing(1);
		punchAngerLevel.setSnapToTicks(true);
		punchAngerLevel.setPaintTicks(true);
		punchAngerLevel.setPaintLabels(true);
		punchAngerLevel.addChangeListener(this);
		judyHealthLevel = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
		judyHealthLevel.setMajorTickSpacing(1);
		judyHealthLevel.setSnapToTicks(true);
		judyHealthLevel.setPaintTicks(true);
		judyHealthLevel.setPaintLabels(true);
		judyHealthLevel.addChangeListener(this);
		joeyHappyLevel = new JSlider(JSlider.HORIZONTAL, 0, 10, 6);
		joeyHappyLevel.setMajorTickSpacing(1);
		joeyHappyLevel.setSnapToTicks(true);
		joeyHappyLevel.setPaintTicks(true);
		joeyHappyLevel.setPaintLabels(true);
		joeyHappyLevel.addChangeListener(this);
		numScenes.setMajorTickSpacing(1);
		numScenes.setSnapToTicks(true);
		numScenes.setPaintTicks(true);
		numScenes.setPaintLabels(true);
		numScenes.addChangeListener(this);

		startbutton = new JButton("Start the show!");
		startbutton.addActionListener(this);
		sceneLabel = new JLabel("Number of scenes:");
		agentLabel = new JLabel("Characters:");
		punchAngerLabel = new JLabel("Punch's initial anger level:");
		judyHealthLabel = new JLabel("Judy's initial health level:");
		joeyHappyLabel = new JLabel("Joey's initial happiness level:");
		punchCheck = new JCheckBox("Punch");
		punchCheck.addActionListener(this);
		punchCheck.setSelected(true);
		judyCheck = new JCheckBox("Judy");
		judyCheck.setSelected(true);
		judyCheck.addActionListener(this);
		joeyCheck = new JCheckBox("Joey");
		joeyCheck.addActionListener(this);
		audienceCheck = new JCheckBox("Enable audience participation");
		audienceCheck.addActionListener(this);
		
		//settingsPanel.add(agentLabel);
		settingsPanel.add(sceneLabel);
		//settingsPanel.add(numScenes);
		settingsPanel.add(audienceCheck);
		settingsPanel.setBorder(BorderFactory.createTitledBorder("Scene"));
		charPanel.add(punchCheck);
		charPanel.add(judyCheck);
		charPanel.add(joeyCheck);
		charPanel.add(sceneLabel);
		charPanel.add(numScenes);
		charPanel.add(punchAngerLabel);
		charPanel.add(punchAngerLevel);
		charPanel.add(judyHealthLabel);
		charPanel.add(judyHealthLevel);
		charPanel.add(joeyHappyLabel);
		charPanel.add(joeyHappyLevel);
		charPanel.setBorder(BorderFactory.createTitledBorder("Characters"));
		//mainPanel.add(settingsPanel);
		mainPanel.add(charPanel);
		mainPanel.add(startbutton);
		this.add(mainPanel);
		
		settingsMap.put("scenes", 2);
		settingsMap.put("audience", 0);
		settingsMap.put("punch", 1);
		settingsMap.put("judy", 1);
		settingsMap.put("joey", 0);

		settingsMap.put("punchanger", 0);
		settingsMap.put("judyhealth", 5);
		settingsMap.put("joeyhappy", 6);
		//settingsMap.put("joey", 0);
	}
	
	public static void main(String[] args) {
		settingsGui = new SettingsDialogue();
		settingsGui.setTitle("Punch and Judy settings");
		settingsGui.setSize(300,700);
		settingsGui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		settingsGui.setVisible(true);
	}
	
	public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            int fps = (int)source.getValue();
                if(source == numScenes) {
                        settingsMap.put("scenes", fps);
                }
                if(source == punchAngerLevel) {
                        settingsMap.put("punchanger", fps);
                }
                if(source == judyHealthLevel) {
                        settingsMap.put("judyhealth", fps);
                }
                if(source == joeyHappyLevel) {
                        settingsMap.put("joeyhappy", fps);
                }

        }    
        System.out.println(settingsMap);
    }
	
	
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == punchCheck) {
			AbstractButton abstractButton = (AbstractButton) event.getSource();
	        boolean selected = abstractButton.getModel().isSelected();
			if (selected == true) {
				settingsMap.put("punch", 1);
			}
			else {
				settingsMap.put("punch", 0);
			}
		}

		if (event.getSource() == judyCheck) {
			AbstractButton abstractButton = (AbstractButton) event.getSource();
	        boolean selected = abstractButton.getModel().isSelected();
			if (selected == true) {
				settingsMap.put("judy", 1);
			}
			else {
				settingsMap.put("judy", 0);
			}
		}

		if (event.getSource() == joeyCheck) {
			AbstractButton abstractButton = (AbstractButton) event.getSource();
	        boolean selected = abstractButton.getModel().isSelected();
			if (selected == true) {
				settingsMap.put("joey", 1);
			}
			else {
				settingsMap.put("joey", 0);
			}
		}

		if (event.getSource() == audienceCheck) {
			AbstractButton abstractButton = (AbstractButton) event.getSource();
	        boolean selected = abstractButton.getModel().isSelected();
			if (selected == true) {
				settingsMap.put("audience", 1);
			}
			else {
				settingsMap.put("audience", 0);
			}
		}

		if(event.getSource() == startbutton) {
			//env.init(arguments);
			PrintWriter writer;
			try {
				writer = new PrintWriter("pjagents.mas2j", "UTF-8");
                writer.println("MAS pjagents {");
                writer.println("infrastructure: Centralised");
                writer.println("environment: io.polka.pjgdx.desktop.Env");
                writer.println("agents:");
                if (settingsMap.get("punch") == 1) {
                	writer.println("punch;");
                }
                if (settingsMap.get("judy") == 1) {
                	writer.println("judy;");
                }
                if (settingsMap.get("joey") == 1) {
                	writer.println("joey;");
                }
                //writer.println("narrative;");
                writer.println("aslSourcePath:");
                writer.println("\"asl\";");
                writer.println("}");
                writer.close();	
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
        new Thread(){
        public void run() {
			try {
				RunCentralisedMAS.main(new String[]{"pjagents.mas2j"});
			} catch (JasonException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        }.start();
		settingsGui.dispose();
		}
		System.out.println(settingsMap);
	}

}
