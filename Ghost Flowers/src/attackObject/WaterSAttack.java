package attackObject;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import playerObject.Player;
import tileMap.TileMap;

public class WaterSAttack extends AttackObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3725510921333687701L;
	private final String FILE_LOCATION = "/Sprites/Water_Player/water_prison.png";
	private final int NUM_FRAMES = 10;
	
	private boolean occupied;
	private Player imprisonedPlayer;
	
	private double bounceSpeed;
	
	public WaterSAttack(TileMap tilemap, Player p, double cd, double ed) {
		super(tilemap, p, cd, ed);
		fallSpeed = 0.15;
		maxFallSpeed = 5.0;
		bounceSpeed = 1.5;
		moveSpeed = 2.5;
		stopSpeed = 0.15;
		
		occupied = false;
		
		cwidth = width = cheight = height = 30;
		initializeSpritesheet();
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
			animation.setDelay(125);
		}
		catch(Exception e){
			e.printStackTrace();
		}	
	}
	public void imprison(Player p){
		if(p.intersects(this) && !occupied){
			imprisonedPlayer = p;
			imprisonedPlayer.getStatus().setAilments(StatusAilments.FREEZE);
			
			cwidth = width = cheight = height = (int) (imprisonedPlayer.getWidth()*1.5);
			occupied = true;
		}
	}
	
	public void start(){
		cdStartTime = eStartTime = System.nanoTime();
		occupied = false;
		cwidth = width = cheight = height = 30;
		facingRight = player.isFacingRight();
		if(facingRight)
			dx = moveSpeed;
		else
			dx = -moveSpeed;
		setPosition(player.getX(), player.getY()-cwidth/2);
		falling = true;
	}
	
	public void getNextPosition(){
		if(falling)
			dy += fallSpeed;
		else{
			dy = -bounceSpeed;
			
			if(facingRight){
				dx -= stopSpeed;
				if(dx < 0) facingRight = false;
			}
			else{
				dx += stopSpeed;
				if(dx > 0) facingRight = true;
			}
		}
		if(dy >= maxFallSpeed) dy = maxFallSpeed;		
	}
	
	public void update(){
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		animation.update();
		
		if(durationComplete())
			occupied = false;
		
		if(occupied)
			imprisonedPlayer.setPosition(x, y);
		
	}
	public void draw(Graphics2D g){
		if(!durationComplete()){
			setMapPosition();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
			g.drawImage(animation.getImage(), (int)(x+xmap-width/2), (int)(y+ymap-height/2), width, height, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
	}

}
