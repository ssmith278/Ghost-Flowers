package attackObject;

import java.awt.Color;
import java.awt.Graphics2D;

import gameObject.GameObject;
import playerObject.Player;
import tileMap.TileMap;

public class AirHAttack extends AttackObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9138974669863196315L;
	
	public AirHAttack(TileMap tilemap, Player p, double cd, double ed) {
		super(tilemap, p, cd, ed);
		fallSpeed = 2;
		maxFallSpeed = 9;
		moveSpeed = 3;
		maxSpeed = 9;
		
		damage = 15;
		
		cwidth = width = 20;
		cheight = height = 40;
	}

	public void initializeSpritesheet() {}
	
	public void hit(GameObject o){
		Player p;
		if(o instanceof Player)
			p = (Player) o;
		else
			return;
		
		if(p.intersects(this) && !p.isFlinching()){
			p.setHealth(p.getHealth()-damage);
			if(facingRight)
				p.getStatus().setInitialKnockBack(5);
			else
				p.getStatus().setInitialKnockBack(-5);
			p.getStatus().setAilments(StatusAilments.KNOCKBACK);
		}
	}
	public void start(){
		cdStartTime = eStartTime = System.nanoTime();
		dy = -maxFallSpeed;
	}
	private void getNextPosition(){
		if(!durationComplete())
			if (falling){
				dy += fallSpeed;
				if(dy > maxFallSpeed) dy = maxFallSpeed;
				player.setPosition(x, y);
			}	
			
			if(dy == 0){
				if(facingRight){
					dx += moveSpeed;
					if(dx > maxSpeed) dx = maxSpeed;
				}
				else
					dx -= moveSpeed;
					if(dx < -maxSpeed) dx = -maxSpeed;
			}
	}
	
	public void update(){
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp,ytemp);
		
		if(right) facingRight = true;
		if(left) facingRight = false;
	}
	public void draw(Graphics2D g){
		setMapPosition();
	
		g.setColor(Color.red);
		g.drawRect((int)(x+xmap-cwidth/2), (int)(y+ymap-cheight/2), cwidth, cheight);
	}

}
