package io.polka.pjgdx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class PuppetShow extends ApplicationAdapter {
	//SpriteBatch batch;
	Texture punchSide, judySide;
	private Texture stageTop, stageUnder;
	public static final Vector2 OFFSTAGELEFT = new Vector2(-300, -50);
	public static final Vector2 STAGELEFT = new Vector2(-75, -50);
	public static final Vector2 STAGECENTRE = new Vector2(75, -50);
	public static final Vector2 STAGERIGHT = new Vector2(200, -50);
	public static final Vector2 OFFSTAGERIGHT = new Vector2(400, -50);
	//private SpriteBatch batch = new SpriteBatch(5460);
	private SpriteBatch batch; 
	private static EventHandler ehandler;
	private List<Puppet> puppets = new ArrayList<Puppet>();
	
	
	public HashMap<String, Animation> processSprites(String name) {
        HashMap<String, Animation> anims = new HashMap<String, Animation>();
		if (name == "punch") {
                Texture punchTexture = new Texture(Gdx.files.internal("pics/Punch.png"));
                TextureRegion[] split = new TextureRegion(punchTexture).split(512, 512)[0];
                TextureRegion[] mirror = new TextureRegion(punchTexture).split(512, 512)[0];
                for (TextureRegion region : mirror)
                        region.flip(true, false);
                Animation punchRight = new Animation(0.1f, split[0]);
                Animation punchLeft = new Animation(0.1f, mirror[0]);
                anims.put("right", punchRight);
                anims.put("left", punchLeft);
		}
		else if (name == "judy") {
                Texture judyTexture = new Texture(Gdx.files.internal("pics/Judy.png"));
                TextureRegion[] split = new TextureRegion(judyTexture).split(512, 512)[0];
                TextureRegion[] mirror = new TextureRegion(judyTexture).split(512, 512)[0];
                for (TextureRegion region : mirror)
                        region.flip(true, false);
                Animation judyRight = new Animation(0.1f, split[0]);
                Animation judyLeft = new Animation(0.1f, mirror[0]);
                anims.put("right", judyRight);
                anims.put("left", judyLeft);
		}
		
		return anims;
	}
	
	 public String[] readLines(String filename) throws IOException {
		 FileHandle handle = Gdx.files.internal(filename);
         String rawlines = handle.readString();
         String[] lines = rawlines.split("\n");
         return lines;
	    }
	
	public HashMap<String, List<Dialogue>> readDialogueFile (String filename) throws IOException {
		String[] lines = readLines(filename);
		List<Dialogue> dList = new ArrayList<Dialogue>();
		HashMap<String, List<Dialogue>> dialogue = new HashMap<String, List<Dialogue>>();
		String hmKey = null;

		for (String line : lines) {
			if (line.startsWith(":")) {
				dList.clear();
				hmKey = line.split(":")[1];
			}
			else {
				String[] pieces = line.split(";");
				System.out.println(hmKey);
				System.out.println(pieces[1]);
				Dialogue aline = new Dialogue("sounds/" + pieces[1] + ".mp3", pieces[0]);
				//Dialogue aline = new Dialogue(this, pieces[0]);
				dList.add(aline);
				dialogue.put(hmKey, new ArrayList<Dialogue>(dList));
			}
		}
		
		//dialogue.get("happy").get(0).audio.play(0);
		
		return dialogue;
		
	}
	
	public HashMap<String, List<Dialogue>> processDialogue(String name) throws IOException {
		HashMap<String, List<Dialogue>> lines = new HashMap<String, List<Dialogue>>();
		String filename = "";
		if (name == "punch") {
			filename = "sounds/punch/punch.csv";
		}
		else if (name == "judy") {
			filename = "sounds/judy/judy.csv";
		}
		lines = readDialogueFile(filename);
		return lines;
		
	}
	
	@Override
	public void create () {
		//batch = new SpriteBatch(5460);
		batch = new SpriteBatch();
        stageTop = new Texture(Gdx.files.internal("pics/Stage-top.png"));
        stageUnder = new Texture(Gdx.files.internal("pics/Stage2.png"));
        HashMap<String, Animation> punchAnims = processSprites("punch");
        HashMap<String, Animation> judyAnims = processSprites("judy");
        ehandler = new EventHandler();
        
        // nooo, don't do this
        HashMap<String, List<Dialogue>> punchDialogue = null;
        HashMap<String, List<Dialogue>> judyDialogue = null;
        
        try {
			punchDialogue = processDialogue("punch");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			judyDialogue = processDialogue("judy");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Puppet punch = new Puppet("punch", OFFSTAGELEFT.x, OFFSTAGELEFT.y, punchAnims, punchDialogue);
		Puppet judy = new Puppet("judy", OFFSTAGERIGHT.x, OFFSTAGERIGHT.y, judyAnims, judyDialogue);
		puppets.add(punch);
		puppets.add(judy);
		judy.setDirection("left");
		
		/*
		MoveEvent moveJudy = new MoveEvent(judy, 2, STAGELEFT);
		SpeakEvent speakJudy = new SpeakEvent(judy, 3, "worried");
		AnimEvent turnJudy = new AnimEvent(judy, 4, "right");
		ehandler.addEvent(moveJudy);
		ehandler.addEvent(speakJudy);
		ehandler.addEvent(turnJudy);
		*/
		
		// don't forget to dispose of dialogue, etc, when done
		
	}
	
	private Puppet lookupActor(String aname) {
		for (Puppet pup : puppets) {
			if (pup.getName().equals(aname)) {
				return pup;
			}
		}
		return null;
	}
	
	private Vector2 lookupPos(String posname) {
		if (posname.equals("offstageLeft")) return OFFSTAGELEFT;
		if (posname.equals("stageLeft")) return STAGELEFT;
		if (posname.equals("stageCentre")) return STAGECENTRE;
		if (posname.equals("stageRight")) return STAGERIGHT;
		if (posname.equals("offstageRight")) return OFFSTAGERIGHT;
		else return null;
	}
	
	public void addEvent(String type, String act, float del, String arg) {
		Puppet actor = lookupActor(act);
		//System.out.println("addEvent called");

		if (actor != null) {
                //System.out.println("Inner loop reached");
                if (type == "move") {
                	//System.out.println("Move event processed");
                	Vector2 targ = lookupPos(arg);
                	if (targ != null) {
                        MoveEvent mvEv = new MoveEvent(actor, del, targ);
                        ehandler.addEvent(mvEv);
                	}
                        
                }
                else if (type == "speak") {
                	//System.out.println("Speak event processed");
                	SpeakEvent spkEv = new SpeakEvent(actor, del, arg);
                	ehandler.addEvent(spkEv);
                        
                }
                else if (type == "anim") {
                	//System.out.println("Anim event processed");
                	AnimEvent anEv = new AnimEvent(actor, del, arg);
                	ehandler.addEvent(anEv);
                }
		}
		
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(stageUnder, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Texture punchTexture = new Texture(Gdx.files.internal("pics/Punch.png"));
		TextureRegion[] split = new TextureRegion(punchTexture).split(512, 512)[0];
		for (Puppet puppet : puppets) {
			puppet.update();
			batch.draw(puppet.getCurrentAnim().getKeyFrame(0), puppet.getPos().x, puppet.getPos().y);
		}
		batch.draw(stageTop, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
	}
}
