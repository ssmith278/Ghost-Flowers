package enemyObject;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import gameObject.Animation;
import main.GamePanel;
import playerObject.Player;
import tileMap.TileMap;

public class Enemy extends Player{

	/**
	 * 
	 */
	private static final long serialVersionUID = 294111168096870018L;

	protected final int[] NUMFRAMES = {
			2, 8, 1, 2, 1, 3, 1, 1
	};
	
	public Enemy(TileMap tilemap, EnemyHive hive, double x, double y) {
		super(tilemap, hive);
		
		this.x = x;
		this.y = y;
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
		
		health = maxHealth = 100;
		
		initializeSpritesheet();
		animation = new Animation();
		setIdle();
	}

	public void initializeSpritesheet() {
		try{
			BufferedImage spritesheet;
			spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/testplayersprite.gif"));			
			sprites = new ArrayList<BufferedImage[]>();
			
			//4 sets of animations - increase if more animations are added
			for (int i = 0; i < 8; i++){
				BufferedImage[] images = new BufferedImage[NUMFRAMES[i]];
				if(i == 5)
					width = 40;
				else
					width = 30;
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
		animation.setDelay(400);				
	}

	public void disableMovementBooleans() {
		setUp(false);
		setDown(false);
		setLeft(false);
		setRight(false);
		setJumping(false);
		setDashing(false);			
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
		getNextPosition();
		status.update();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(nAttacking){
			if(currentAction != N_ATTACKING){
				currentAction = N_ATTACKING;
				animation.setFrames(sprites.get(N_ATTACKING));
				animation.setDelay(200);
				width = 40;
			}
		}
		else if (dy > 0){						
			if (currentAction != FALLING && !dashing){
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 30;
			}			
		}
		else if (dy < 0){
			if(currentAction != JUMPING){
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 30;
			}
		}
		else if ((left || right) && !dashing){
			if(currentAction != WALKING){
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 30;
			}
		}
		else if(dashing){
			if(currentAction != DASHING){
				currentAction = DASHING;
				animation.setFrames(sprites.get(DASHING));
				animation.setDelay(-1);
				width = 30;
			}
		}
		else if (currentAction != IDLE){
			setIdle();
			width = 30;
		}
		
		animation.update();
		
		if(right) facingRight = true;
		if(left) facingRight = false;
		
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
		
		if (facingRight) {
			g.drawImage(animation.getImage(), (int)(x + xmap - width/2), (int)(y + ymap - height/2), null);
		}
		else {
			g.drawImage(animation.getImage(), (int)(x + xmap - width/2) + width, (int)(y + ymap - height/2), -width, height, null);
		}
		
	}

}
