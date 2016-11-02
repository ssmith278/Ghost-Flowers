package playerObject;

import gameObject.Animation;
import gameObject.GameObject;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import attackObject.WaterDash;
import attackObject.WaterHAttack;
import attackObject.WaterNAttack;
import attackObject.WaterSAttack;
import enemyObject.Enemy;
import enemyObject.EnemyHive;
import tileMap.TileMap;

public class WaterPlayer extends Player{

	/**
	 * 
	 */
	private static final long serialVersionUID = -554290290212480154L;
	private WaterDash wdash;
	private WaterNAttack wna;
	private WaterHAttack wha;
	private WaterSAttack wsa;
	
	protected final int[] NUMFRAMES = {
			10, 5, 5, 4, 4
	};
	
	public WaterPlayer(TileMap tilemap, EnemyHive hive) {
		super(tilemap, hive);
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 5.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		flinchTime = 1000;
		
		facingRight = true;
		
		health = maxHealth = 3;
		
		this.hive = hive;
		wdash = new WaterDash(tilemap, this, 2, 0.5);
		wna = new WaterNAttack(tilemap, this, 0, .25);
		wha = new WaterHAttack(tilemap, this, 3.5, 2.5);
		wsa = new WaterSAttack(tilemap, this, 0, 5);
		
		initializeSpritesheet();		
		animation = new Animation();
		setIdle();
	}

	public void initializeSpritesheet() {
		try{
			width = height = 30;
			cwidth = cheight = 20;
			
			wdash.initializeSpritesheet();
			wna.initializeSpritesheet();
			wha.initializeSpritesheet();
			wsa.initializeSpritesheet();
			
			BufferedImage spritesheet;
			spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Water_Player/water_sprites.gif"));			
			sprites = new ArrayList<BufferedImage[]>();
			
			//4 sets of animations - increase if more animations are added
			for (int i = 0; i < NUMFRAMES.length; i++){
				BufferedImage[] images = new BufferedImage[NUMFRAMES[i]];
				for (int j = 0; j < NUMFRAMES[i]; j++){
					//change the following if sprites are not default size
					if(i == DASHING)
						width = 45;
					else
						width = 30;
					
					images[j] = spritesheet.getSubimage(j * width, i * height, width, height);
				}
				
				sprites.add(images);
			}				
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public void setIdle() {
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(100);	
		animation.playOneTime(false);
	}

	public void disableMovementBooleans() {
		setUp(false);
		setDown(false);
		setLeft(false);
		setRight(false);
		setJumping(false);
		setDashing(false);		
	}
	private void dash(){
		if ((left || right) && !wdash.onCooldown()){
			wdash.setPosition(getX(), getY());
			wdash.setFacingRight(facingRight);
			wdash.start();
		}	
	}
	
	private void hAttack(){
		if(!wha.onCooldown()){
			wha.setPosition(x, y-wha.getHeight()/2);
			wha.start();
		}
	}
	
	private void sAttack(){
		if(!wsa.onCooldown())
			wsa.start();
	}

	protected void getNextPosition() {
		if (left){
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if (right){
			dx += moveSpeed;
			if(dx > maxSpeed){
				dx = maxSpeed;
			}
		}
		else{
			if(dx > 0){
				dx -= stopSpeed;
				if(dx < 0)
					dx = 0;
			}
			else if(dx < 0){
				dx += stopSpeed;
				if(dx > 0)
					dx = 0;
			}
		}
		
		wdash.update();
		
		if (jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}
		
		if (falling){
			dy += fallSpeed;
			
			if(dy > 0) jumping = false;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}		
	}

	public void update() {
		super.update();
		
		if(!wdash.durationComplete()){
			if(currentAction != DASHING){
				currentAction = DASHING;
				animation.setFrames(sprites.get(DASHING));
				animation.setDelay(50);
				animation.playOneTime(true);
				width = 45;
			}
		}
		else if (dy > 0){						
			if (currentAction != FALLING){
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				animation.playOneTime(true);
				width = 30;
			}			
		}
		else if (dy < 0){
			if(currentAction != JUMPING){
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(50);
				animation.playOneTime(true);
				width = 30;
			}
		}
		else if (left || right){
			if(currentAction != WALKING){
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(100);
				animation.playOneTime(false);
				width = 30;
			}
		}
		else if (currentAction != IDLE){
			setIdle();
			width = 30;
		}
		
		animation.update();
		
		if(!nAttacking){
			if(right) facingRight = true;
			if(left) facingRight = false;
			
			wna.start();
		}		
		
		if(dashing)
			dash();
		
		if(!wdash.durationComplete())
			for(Enemy e : hive)
				wdash.hit(e);
			
		
		wna.update();
		for(Enemy e : hive)
			wna.hit(e);
		
		wha.update();
		
		if(hAttacking)
			hAttack();
		
		if(!wha.durationComplete())
			for(Enemy e : hive)
				wha.hit(e);
		
		if(sAttacking)
			sAttack();
		
		if(!wsa.durationComplete())
			for(Enemy e : hive)
				wsa.imprison(e);
		
		wsa.update();
		
		flinchElapsed = (System.nanoTime() - flinchStart) / 1000000;
		
		if(flinchElapsed >= flinchTime)
			flinching = false;
		
		if(health <= 0) dead = true;		
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTime) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		
		wna.draw(g);
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
		if (facingRight) {
			g.drawImage(animation.getImage(), (int)(x + xmap - width/2), (int)(y + ymap - height/2), null);
		}
		else {
			g.drawImage(animation.getImage(), (int)(x + xmap - width/2) + width, (int)(y + ymap - height/2), -width, height, null);
		}
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		
		wha.draw(g);
		
		wsa.draw(g);
		
	}

}
