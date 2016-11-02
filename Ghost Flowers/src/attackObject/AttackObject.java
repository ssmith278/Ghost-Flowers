package attackObject;

import tileMap.TileMap;

import java.awt.image.BufferedImage;

import gameObject.Animation;
import gameObject.GameObject;
import playerObject.Player;

public abstract class AttackObject extends GameObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5662199987009216796L;
	
	protected Animation animation;
	transient protected BufferedImage[] sprites;
	protected int damage;
	protected double startingX, startingY, cooldown, effectDuration;
	protected long cdStartTime, eStartTime;
	protected Player player;

	public AttackObject(TileMap tilemap, Player p, double cd, double ed) {
		super(tilemap);
		cooldown = cd;
		effectDuration = ed;
		player = p;
		animation = new Animation();
	}
	
	public abstract void initializeSpritesheet();	
	
	public boolean onCooldown(){
		long elapsed = System.nanoTime() - cdStartTime;
		
		if(cooldown > elapsed / 1E9)
			return true;
		
		return false;
	}
	
	public boolean durationComplete(){
		long elapsed = System.nanoTime() - eStartTime;
		
		if(effectDuration < elapsed / 1E9)
			return true;
		
		return false;
	}
	public long getEDTimer(){
		long elapsed =  (long) ((System.nanoTime() - eStartTime)/1E9);
		
		return elapsed;
	}
	
	public long getCDTimer(){
		long elapsed =  (long) ((System.nanoTime() - cdStartTime)/1E9);
		if(cooldown < elapsed)
			return (long) cooldown;
		else
			return elapsed;
	}
	
	public double getCooldown(){
		return cooldown;
	}
	
	public void setPosition(double x, double y){
		this.x = x;
		this.y = y;
		startingX = x;
		startingY = y;
	}
	
	public double getStartingX(){
		return startingX;
	}
	public double getStartingY(){
		return startingY;
	}
	
	public void start(){
		cdStartTime = eStartTime = System.nanoTime();
	}
	
	public boolean facingRight(){
		return facingRight;
	}
	
	public void setFacingRight(boolean b){
		facingRight = b;
	}
	
	public int getDamage(){
		return damage;
	}

}
