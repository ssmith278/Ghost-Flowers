package attackObject;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import gameObject.Animation;
import playerObject.Player;
import tileMap.TileMap;

public class WaterBeam extends AttackObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1562210553116708015L;
	private final String FILE_LOCATION = "/Sprites/Water_Player/waterbeam.png";
	private final int NUM_FRAMES = 2;
	private final double SPEED = 6.6;
	
	private boolean remove;
	public WaterBeam(TileMap tilemap, Player p, double cd, double ed) {
		super(tilemap, p, cd, ed);
		fallSpeed = 0.15;
		maxFallSpeed = 5.0;
		moveSpeed = SPEED;
		cwidth = cheight = width = height = 10;
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
			animation.setDelay(350);
			animation.playOneTime(true);
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
		if(falling)
			dy += fallSpeed;
		if(dy >= maxFallSpeed) dy = maxFallSpeed;
		if(facingRight)
			dx = moveSpeed;
		else
			dx = -moveSpeed;	
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
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
		
		if(player.isFacingRight())
			g.drawImage(animation.getImage(),(int)(x+xmap-cwidth/2), (int)(y+ymap-cheight/2), cwidth, cheight, null);
		else
			g.drawImage(animation.getImage(),(int)(x+xmap+cwidth/2), (int)(y+ymap-cheight/2), -cwidth, cheight, null);
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}

}
