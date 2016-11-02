package playerObject;

import gameObject.GameObject;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import attackObject.StatusAilments;
import enemyObject.EnemyHive;
import tileMap.TileMap;

public abstract class Player extends GameObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7520368994128895197L;
	protected EnemyHive hive;
	protected StatusAilments status;
	protected int health, maxHealth;
	protected double attackSpeed;
	protected long flinchTime, flinchStart, flinchElapsed; 
	protected boolean dead, flinching, nAttacking, hAttacking, sAttacking, invul;
	
	protected transient ArrayList<BufferedImage[]> sprites;
	
	protected static final int IDLE = 0;
	protected static final int WALKING = 1;
	protected static final int JUMPING = 2;
	protected static final int FALLING = 3;
	protected static final int DASHING = 4;
	protected static final int N_ATTACKING = 5;
	protected static final int H_ATTACKING = 6;
	protected static final int S_ATTACKING = 7;
	
	public Player(TileMap tilemap, EnemyHive hive) { super(tilemap); status = new StatusAilments(this); this.hive = hive; }
	public abstract void initializeSpritesheet();
	public abstract void setIdle();
	public abstract void disableMovementBooleans();
	
	public StatusAilments getStatus(){ return status;}
	public void setHealth(int hp){ 
		if(!invul){
			if(hp < health)
				startFlinching(); 
			health = hp; 
			System.out.println("Player health: " + health);
		}
	}
	public int getHealth(){ return health; }
	public int getMaxHealth(){ return maxHealth; }
	public void decrementHealth(){ if(!invul){ health--; startFlinching(); } }
	public void setAttackSpeed(double as){ attackSpeed = as; }
	public double getAttackSpeed(){ return attackSpeed; }
	public void setNAttacking(boolean b){ nAttacking = b; }
		public boolean isNAttacking(){ return nAttacking; }
	public void setHAttacking(boolean b){ hAttacking = b; }
		public boolean isHAttacking(){ return hAttacking; }
	public void setSAttacking(boolean b){ sAttacking = b; }
		public boolean isSAttacking(){ return sAttacking; }
	public void startFlinching(){ if(!flinching) { flinching = true; flinchStart = System.nanoTime(); } }
	public boolean isFlinching(){ return flinching; }
	public void setInvulnerable(boolean b){ invul = b; }
	public boolean isInvulnerable(){ return invul; }
	public boolean isDead(){ return dead; }
	public void revive(){ health = maxHealth; dead = false; }
	
	protected abstract void getNextPosition();
	public void update(){
		getNextPosition();
		status.update();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
	}
	public abstract void draw(Graphics2D g);
	
}
