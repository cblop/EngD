package io.polka.pjgdx;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Puppet {
	private String name;
	private Vector2 pos = new Vector2();
	private Vector2 target = new Vector2();
	private HashMap<String, Animation> anims;
	private HashMap<String, List<Dialogue>> lines;
	private String currentAnim = "right";
	private float speed = 10.0f;
	//private SpriteBatch batch;
	private int frame = 0;
	private boolean loop = true;

	
	public Puppet (String nom, float x, float y, HashMap<String, Animation> animations,
			HashMap<String, List<Dialogue>> dialogue) {
		name = nom;
		pos.x = x;
		pos.y = y;
		target.x = x;
		target.y = y;
		anims = animations;
		lines = dialogue;
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
	
	public void face(String direction) {
		if (direction == "left") {
			currentAnim = "left";
		}
		else if (direction == "right") {
			currentAnim = "right";
		}
		
	}
	
	public void sayLine(String mood) {
		int rnd = (int) (Math.random() * lines.get(mood).size());
		lines.get(mood).get(rnd).trigger();
		
	}
	
	public Animation getCurrentAnim() {
		return anims.get(currentAnim);
	}
	
	public void setCurrentAnim(String animName) {
		currentAnim = animName;
	}
	
	public String getName() {
		return name;
	}
	
	public Vector2 getPos() {
		return pos;
	}
	
	public void update () {
		move();

	}

	public void moveTo(Vector2 trg) {
		target = trg;
	}

}
