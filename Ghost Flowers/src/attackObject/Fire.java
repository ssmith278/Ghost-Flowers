package attackObject;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import playerObject.Player;
import tileMap.TileMap;

public class Fire extends AttackObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4158202074519402667L;
	
	private final String FILE_LOCATION = "/Sprites/Fire_Player/fire.png";
	private final int NUM_FRAMES = 4;
	private boolean remove;
	
	public Fire(TileMap tilemap, Player p, double cd, double ed) {
		super(tilemap, p, cd, ed);
		cwidth = width = 20;
		cheight = height = 25;
		fallSpeed = 0.15;
		maxFallSpeed = 5.0;
		remove = false;
		initializeSpritesheet();
		start();
	}
	
	public void initializeSpritesheet() {
		try{
			BufferedImage spritesheet;
			spritesheet = ImageIO.read(getClass().getResourceAsStream(FILE_LOCATION));			
			sprites = new BufferedImage[NUM_FRAMES];
			
			for (int i = 0; i < sprites.length; i++){
				BufferedImage images = spritesheet.getSubimage(i * width, 0, width, height);					
				sprites[i] = images;
			}
			
			animation.setFrames(sprites);
			animation.setDelay(100);
		}
		catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	public void flagForRemoval(){
		remove = true;
	}
	public boolean toBeRemoved(){
		return remove;
	}
	private void getNextPosition(){
		if (falling){
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}		
	}
	
	public void update(){
		if(durationComplete())
			flagForRemoval();
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		animation.update();
	}
	public void draw(Graphics2D g){
		setMapPosition();
		
		if (facingRight) {
			g.drawImage(animation.getImage(), (int)(x + xmap - width/2), (int)(y + ymap - height/2), null);
		}
		else {
			g.drawImage(animation.getImage(), (int)(x + xmap - width/2) + width, (int)(y + ymap - height/2), -width, height, null);
		}
	}

}
