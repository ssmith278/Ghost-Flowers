package playerObject;

import gameObject.Animation;
import gameObject.GameObject;
import gameObject.Skillbar;
import main.GamePanel;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import tileMap.TileMap;
import attackObject.FireDash;
import attackObject.FireHAttack;
import attackObject.FireSAttack;
import attackObject.StatusAilments;
import attackObject.FireNAttack;
import attackObject.FireNAttack;
import enemyObject.Enemy;
import enemyObject.EnemyHive;

public class FirePlayer extends Player{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5063021525845959618L;
	private FireDash fdash;
	private FireNAttack fna;
	private FireHAttack fha;
	private FireSAttack fsa;
	
	private Skillbar skillbar;
	
	protected final int[] NUMFRAMES = {
			9, 4, 4, 4, 6, 3, 1
	};
	
	public FirePlayer(TileMap tilemap, EnemyHive hive) {
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
		attackSpeed = 0.15;
		
		flinchTime = 1000;
		
		facingRight = true;
		
		health = maxHealth = 100;
		
		this.hive = hive;
		fdash = new FireDash(tilemap, this, 15, 2, 0.5);
		fna = new FireNAttack(tilemap, this, 1.5, attackSpeed);
		fha = new FireHAttack(tilemap, this, 0, attackSpeed*12);
		fsa = new FireSAttack(tilemap, this, 30, 10);
		
		skillbar = new Skillbar(tilemap, GamePanel.WIDTH/10, GamePanel.HEIGHT/10, fdash, fna, fha, fsa);
		animation = new Animation();
		initializeSpritesheet();
		setIdle();
	}

	public void initializeSpritesheet() {
		try{
			width = height = 30;
			cwidth = cheight = 20;
			
			fdash.initializeSpritesheet();
			fna.initializeSpritesheet();
			fha.initializeSpritesheet();
			fsa.initializeSpritesheet();
			
			BufferedImage spritesheet;
			spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Fire_Player/fire_sprites.gif"));			
			sprites = new ArrayList<BufferedImage[]>();
			
			//4 sets of animations - increase if more animations are added
			for (int i = 0; i < NUMFRAMES.length; i++){
				BufferedImage[] images = new BufferedImage[NUMFRAMES[i]];
				if(i == 5)
					width = 45;
				else
					width = 30;
				for (int j = 0; j < NUMFRAMES[i]; j++){
					//change the width/height if sprites are not default size
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
		animation.setDelay(150);		
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
		if ((left || right) && !fdash.onCooldown()){
			fdash.setPosition(getX(), getY());
			fdash.setFacingRight(facingRight);
			fdash.start();
		}	
	};
	
	public void setAttackSpeed(double as){
		attackSpeed = as;
		fna.updateAS();
		if(!fsa.durationComplete())
			fdash.setCooldown(.5);
		else
			fdash.setCooldown(2);
	}
	
	private void nAttack(){
		if(!fna.onCooldown() && !hAttacking)
			fna.start();			
	}
	
	private void sAttack(){
		if(!fsa.onCooldown())
			fsa.start();
	}
	
	public void setHAttacking(boolean b){
		if(!hAttacking && b){
			fha.start();
			fha.setCharge(true);
			hAttacking = true;
			System.out.println("Charging...");
		}
		else if(!b){
			if(fha.fullyCharged()){
				for(Enemy e : hive)
					synchronized(e){
						fha.detonate(e);
					}
				synchronized(this){
					if(facingRight)
						status.setInitialKnockBack(-5);
					else
						status.setInitialKnockBack(5);
					status.setAilments(StatusAilments.KNOCKBACK);
				}
				hAttacking = false;
			
			}
			else{
				fha.setCharge(false);
				hAttacking = false;
				System.out.println("Ceased charging.");
			}
		}
	}

	protected void getNextPosition() {
		if(!status.isKnockedBack())
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
		
		fdash.update();
		
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
		synchronized(this){
			super.update();
		}
		skillbar.update();
		
		if(hAttacking){
			if(currentAction != H_ATTACKING){
				currentAction = H_ATTACKING;
				animation.setFrames(sprites.get(H_ATTACKING));
				animation.setDelay(-1);
				animation.playOneTime(true);
				width = height = 30;
			}
		}
		else if(nAttacking){
			if(currentAction != N_ATTACKING){
				currentAction = N_ATTACKING;
				animation.setFrames(sprites.get(N_ATTACKING));
				animation.setDelay((long) (attackSpeed*10));
				animation.playOneTime(true);
				width = 45;
			}
		}
		else if(!fdash.durationComplete()){
			if(currentAction != DASHING){
				currentAction = DASHING;
				animation.setFrames(sprites.get(DASHING));
				animation.setDelay(75);
				animation.playOneTime(false);
				width = 30;
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
				animation.setDelay(100);
				animation.playOneTime(true);
				width = 30;
			}
		}
		else if ((left || right)){
			if(currentAction != WALKING){
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(70);
				animation.playOneTime(false);
				width = 30;
			}
		}
		else if (currentAction != IDLE){
			setIdle();
			animation.playOneTime(false);
			width = 30;
		}
		
		animation.update();
		
		if(right) facingRight = true;
		if(left) facingRight = false;
		
		if(dashing)
			dash();
		
		for(Enemy e : hive)
			fdash.burn(e);
		
		fsa.update();
		if(sAttacking)
			sAttack();
		
		fha.update();
		if(hAttacking)
			for(Enemy e : hive)
				fha.pull(e);
		
		fna.update();
		if(nAttacking){
			nAttack();
			for(Enemy e : hive)
				fna.hit(e);
		}
		
		flinchElapsed = (System.nanoTime() - flinchStart) / 1000000;
		
		if(flinchElapsed >= flinchTime)
			flinching = false;
		
		if(health <= 0) dead = true;		
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		
		if(!fsa.durationComplete())
			fsa.draw(g);
		
		fdash.draw(g);
		
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTime) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		

		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
		if (facingRight) {
			g.drawImage(animation.getImage(), (int)(x + xmap - width/2), (int)(y + ymap - height/2), null);
		}
		else {
			g.drawImage(animation.getImage(), (int)(x + xmap - width/2) + width, (int)(y + ymap - height/2), -width, height, null);
		}
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		
		
		fha.draw(g);
		
		fna.draw(g);
		
		//skillbar.draw(g);
		
	}

}
