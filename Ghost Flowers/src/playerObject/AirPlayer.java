package playerObject;

import gameObject.Animation;
import gameObject.GameObject;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import attackObject.AirDash;
import attackObject.AirHAttack;
import attackObject.AirNAttack;
import attackObject.AirSAttack;
import enemyObject.Enemy;
import enemyObject.EnemyHive;
import tileMap.TileMap;

public class AirPlayer extends Player{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4166989419480425495L;
	private AirDash adash;
	private AirNAttack ana;
	private AirHAttack aha;
	private AirSAttack asa;
	
	protected final int[] NUMFRAMES = {
			7, 10, 7, 7, 0, 5
	};
	
	private boolean jumped, djumping, djumped;
	
	public AirPlayer(TileMap tilemap, EnemyHive hive) {
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
		attackSpeed = .10;
		
		flinchTime = 1000;
		
		facingRight = true;
		
		health = maxHealth = 3;
		
		this.hive = hive;
		adash = new AirDash(tilemap, this, 2, 0.5);
		ana = new AirNAttack(tilemap, this, 0, attackSpeed);
		aha = new AirHAttack(tilemap, this, 3, 1);
		asa = new AirSAttack(tilemap, this, 7.5, 5);
				
		animation = new Animation();
		initializeSpritesheet();
		setIdle();
	}

	public void initializeSpritesheet() {
		try{
			width = height = 30;
			cwidth = cheight = 20;
			BufferedImage spritesheet;
			spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Air_Player/air_sprites.gif"));			
			sprites = new ArrayList<BufferedImage[]>();
			
			//4 sets of animations - increase if more animations are added
			for (int i = 0; i < NUMFRAMES.length; i++){
				BufferedImage[] images = new BufferedImage[NUMFRAMES[i]];
				for (int j = 0; j < NUMFRAMES[i]; j++){
					//change the following if sprites are not default size
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
		animation.setDelay(75);		
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
		if ((left || right) && !adash.onCooldown()){
			adash.setPosition(getX(), getY());
			adash.setFacingRight(facingRight);
			adash.start();
		}	
	}
	
	private void nAttack(){
		if(!ana.onCooldown())
			ana.start();
	}
	private void hAttack(){
		if(facingRight){
			aha.setRight(true);
			aha.setLeft(false);
			System.out.println("Set Right");
		}
		else{
			aha.setLeft(true);
			aha.setRight(false);
			System.out.println("Set Left");
		}
		if(!aha.onCooldown() && aha.durationComplete()){
			aha.start();
			aha.setPosition(x, y);
		}			
	}
	public void setDJumping(boolean b){
		djumping = b;
	}
	public void setJumping(boolean b){
		if(jumped){
			setDJumping(b);
			jumping = false;
		}
		else
			jumping = b;
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
		
		adash.update();
		
		if (jumping && !jumped) {
			dy = jumpStart;
			falling = true;
			jumped = true;
		}
		else if(djumping && !djumped){
			dy = jumpStart;
			djumped = true;
		}
		
		if (falling){
			dy += fallSpeed;
			
			if(dy > 0){ 
				jumping = false;
				djumping = false;
			}
			if(dy < 0 && !jumping && !djumping){
				dy += stopJumpSpeed;
			}
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		if(dy == 0)
			jumped = djumped =  false;
	}

	public void update() {
		super.update();
		
		if(nAttacking){
			if(currentAction != N_ATTACKING){
				currentAction = N_ATTACKING;
				animation.setFrames(sprites.get(N_ATTACKING));
				animation.playOneTime(false);
				animation.setDelay((long) (attackSpeed));
			}
		}
		else if (dy > 0){						
			if (currentAction != FALLING){
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.playOneTime(false);
				animation.setDelay(75);
			}			
		}
		else if (dy < 0){
			if(currentAction != JUMPING){
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.playOneTime(false);
				if(!djumped)
					animation.setDelay(30);
				else
					animation.setDelay(15);
			}
		}
		else if (left || right){
			if(currentAction != WALKING){
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.playOneTime(false);
				animation.setDelay(50);
			}
		}
		else if (currentAction != IDLE){
			setIdle();
		}
		
		animation.update();
		
		if(right) facingRight = true;
		if(left) facingRight = false;
		
		if(dashing)
			dash();
		
		if(!adash.durationComplete())
			for(Enemy e : hive)
				adash.hit(e);
		
		ana.update();
		if(nAttacking){
			nAttack();
		}
		if(nAttacking && !ana.durationComplete())
			for(Enemy e : hive)
				ana.hit(e);
		
		aha.update();
		if(hAttacking)
			hAttack();
		
		if(!aha.durationComplete())
			for(Enemy e : hive)
				aha.hit(e);
		
		asa.update();
		if(sAttacking && !asa.onCooldown()){
			asa.start();
			setPosition(x, y-30);
		}
		
		if(!asa.durationComplete())
			for(Enemy e : hive)
				asa.vacuum(e);

		
		if(!asa.durationComplete()){
			width  = height = 60;
			cwidth = cheight = 50;
		}
		else{
			width  = height = 30;
			cwidth = cheight = 20;
		}
		
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
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
		if (facingRight) {
			g.drawImage(animation.getImage(), (int)(x + xmap - width/2), (int)(y + ymap - height/2), width, height, null);
		}
		else {
			g.drawImage(animation.getImage(), (int)(x + xmap - width/2) + width, (int)(y + ymap - height/2), -width, height, null);
		}
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		
		if(!aha.durationComplete())
			aha.draw(g);
		
		if(!asa.durationComplete())
			asa.draw(g);
		
	}
}
