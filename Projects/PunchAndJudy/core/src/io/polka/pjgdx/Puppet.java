package io.polka.pjgdx;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Puppet {
	private String name;
	private Vector2 pos = new Vector2();
	private Vector2 target = new Vector2();
	private HashMap<String, Animation> anims;
	private HashMap<String, List<Dialogue>> lines;
	private String currentAnim = "rest";
  private Dialogue currentLine;
	private String direction = "right";
  private String emotion = "happy";
	private float speed = 10.0f;
	//private SpriteBatch batch;
	private int frame = 0;
	private boolean loop = true;
  private boolean speaking = false;
  private BitmapFont font;

	
	public Puppet (String nom, float x, float y, HashMap<String, Animation> animations,
			HashMap<String, List<Dialogue>> dialogue) {
		name = nom;
		pos.x = x;
		pos.y = y;
		target.x = x;
		target.y = y;
		anims = animations;
		lines = dialogue;
        font = new BitmapFont();
		//batch = new SpriteBatch(5460);

		// consider implementing bounds (if needed)

	}
	
	protected void move() {
		if (Math.abs(pos.x - target.x) <= speed) {
			pos.x = target.x;
		}
		else {
                if (pos.x < target.x) {
                	pos.x += speed;
                        
                }
                else if (pos.x > target.x) {
                	pos.x -= speed;
                }
		}
	}
	
	
	public void sayLine(String mood) {
		int rnd = (int) (Math.random() * lines.get(mood).size());
    currentLine = lines.get(mood).get(rnd);
    currentLine.trigger();
    speaking = true;
    
    new Thread() {
    	public void run() {
    		try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		speaking = false;
    	}
    }.start();

	}
	
	public void drawEmotion(SpriteBatch batch) {
        if (emotion != null) {
                font.setColor(1f, 1f, 0f, 1f);
                font.draw(batch, emotion, getPos().x + 250, getPos().y + 200);
        }
		
	}
	
	public void drawSubs(SpriteBatch batch) {
		if (speaking) {
                if (currentLine != null) {
                		font.setColor(1f, 1f, 1f, 1f);
                        font.draw(batch, currentLine.getSubtitle(), getPos().x + 200, getPos().y + 400);
                }
		}
		
	}
	
	public Animation getCurrentAnim() {
		return anims.get(currentAnim);
	}
	
	public void setCurrentAnim(String animName) {
		for (String anm : anims.keySet()) {
			if (anm.equals(animName)) {
            currentAnim = animName;
			}
		}
	}
	
	
	public void turn() {
		if (direction.equals("right")) {
			setDirection("left");
		}
		else {
			setDirection("right");
		}
	}
	
	public String getDirection() {
		return direction;
	}
	
	public void setDirection(String dir) {
		direction = dir;
		//currentAnim = dir;
	}
	
	public void setSpeed(String spd) {
		if (spd.equals("fast")) {
			speed = 30f;
		}
		else if (spd.equals("medium")) {
			speed = 20f;
		}
		else if (spd.equals("slow")) {
			speed = 10f;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public Vector2 getPos() {
		return pos;
	}

  public void setEmotion(String emo) {
    emotion = emo;
    System.out.println(name + "'s new emotion: " + emotion);
  }
	
	public void update () {
		move();

	}

	public void moveTo(Vector2 trg) {
		target = trg;
	}

}
