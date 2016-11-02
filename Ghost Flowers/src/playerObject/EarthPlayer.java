package playerObject;

import gameObject.Animation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import enemyObject.EnemyHive;
import tileMap.TileMap;

public class EarthPlayer extends Player{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7588684056672616843L;

	protected final int[] NUMFRAMES = {
			8, 7, 1, 1, 1, 3, 1, 1
	};
	
	public EarthPlayer(TileMap tilemap, EnemyHive hive) {
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
		
		initializeSpritesheet();
		
		animation = new Animation();
		setIdle();
	}

	public void initializeSpritesheet() {
		try{
			width = height = 30;
			cwidth = cheight = 20;
			
			BufferedImage spritesheet;
			spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Earth_Player/earth_sprites.gif"));			
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
		animation.setDelay(200);		
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
	private void dig(){
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		calculateCorners(x, ydest);
		if (dy < 0){
			if (topLeft || topRight){
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else{
				ytemp += dy;
			}
		}
		if (dy > 0){
			if (bottomLeft || bottomRight){
				dy = 0;
				falling = false;
				grounded = true;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		if (!falling){
			calculateCorners(x, ydest + 1);
			if (!bottomLeft && !bottomRight)
				falling = true;
				grounded = false;
		}
		setPosition(x+dx,y+dy);
		System.out.println("Dig.");
	}

	public void update() {
		getNextPosition();
		status.update();

		if(dashing)
			dig();
		else{
			checkTileMapCollision();
			setPosition(xtemp, ytemp);
		}
		
		
		if (dy > 0){						
			if (currentAction != FALLING){
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
		else if (left || right){
			if(currentAction != WALKING){
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
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
