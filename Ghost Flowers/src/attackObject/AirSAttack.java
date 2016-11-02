package attackObject;

import java.awt.Graphics2D;

import gameObject.GameObject;
import playerObject.Player;
import tileMap.TileMap;

public class AirSAttack extends AttackObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9138974669863196315L;
	private final double FORCE = 1.5;
	
	public AirSAttack(TileMap tilemap, Player p, double cd, double ed) {
		super(tilemap, p, cd, ed);
		cwidth = width = cheight = height = 200;
	}

	public void initializeSpritesheet() {

	}
	public void vacuum(GameObject o){
		if(o.intersects(this))
			if(o.getX() > x && o.getY() > y)
				o.setVector(o.getDx() - FORCE, o.getDy() - FORCE);
			else if(o.getX() > x && o.getY() < y)
				o.setVector(o.getDx() - FORCE, o.getDy() + FORCE);
			else if(o.getX() < x && o.getY() > y)
				o.setVector(o.getDx() + FORCE, o.getDy() - FORCE);
			else if(o.getX() < x && o.getY() < y)
				o.setVector(o.getDx() + FORCE, o.getDy() + FORCE);
	}
	
	public void update(){
		setPosition(player.getX(),player.getY());
	}
	
	public void draw(Graphics2D g){
	}

}
