package attackObject;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import playerObject.Player;
import tileMap.TileMap;

public class FireSAttack extends AttackObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4935941832677493071L;
	
	private final String FILE_LOCATION = "/Sprites/Fire_Player/fire_buff.png";
	private final int NUM_FRAMES = 10;
	private double originalAS;
	
	public FireSAttack(TileMap tilemap, Player p, double cd, double ed) {
		super(tilemap, p, cd, ed);
		
		cwidth = width = 30;
		cheight = height = 30;		
		originalAS = player.getAttackSpeed();
		initializeSpritesheet();	
	}
	
	public void initializeSpritesheet(){
		try{
			BufferedImage spritesheet;
			spritesheet = ImageIO.read(getClass().getResourceAsStream(FILE_LOCATION));			
			sprites = new BufferedImage[NUM_FRAMES];
			
			for (int i = 0; i < sprites.length; i++){
				BufferedImage images = spritesheet.getSubimage(i * width, 0, width, height);					
				sprites[i] = images;
			}	
			
			animation.setFrames(sprites);
			animation.setDelay(200);	
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void update(){
		if(!durationComplete()){
			animation.update();
			setPosition(player.getX(), player.getY());
			width = player.getWidth();
			height = player.getHeight();
			player.setAttackSpeed(originalAS*0.5);
		}
		else{
			player.setAttackSpeed(originalAS);
		}
	}
	
	public void draw(Graphics2D g){
		if(!durationComplete()){
			setMapPosition();
			g.drawImage(animation.getImage(),(int)(x+xmap)-width/2,(int)(y+ymap)-height/2,width,height,null);
		}
	}

}
