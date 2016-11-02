package attackObject;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import gameObject.Animation;
import gameObject.GameObject;
import playerObject.Player;
import tileMap.TileMap;

public class WaterHAttack extends AttackObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -608896302516859988L;
	private final String FILE_LOCATION = "/Sprites/Water_Player/hailstorm.png";
	private final int NUM_FRAMES = 6;

	public WaterHAttack(TileMap tilemap, Player p, double cd, double ed) {
		super(tilemap, p, cd, ed);
		
		fallSpeed = 0.1;
		maxFallSpeed = 0.5;
		cwidth = width = cheight = height = 90;
		damage = 0;
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
			animation.setDelay(75);
			cwidth = cheight = 135;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void hit(GameObject o){
		Player p;
		
		if(o instanceof Player){
			p = (Player) o;
			
			if(p.intersects(this) && !p.isFlinching()){
				p.setHealth(p.getHealth()-damage);
				p.getStatus().setMaximumSpeed(p.getSpeedPercent(50));
				p.getStatus().setAilments(StatusAilments.SLOW);
			}
		}
		
	}
	private void getNextPosition(){
		dy += fallSpeed;
		if(dy >= maxFallSpeed)
			dy = maxFallSpeed;
		
		y += dy;
		
	}
	public void update(){
		getNextPosition();
		animation.update();
	}
	public void draw(Graphics2D g){
		setMapPosition();
		
		if(!durationComplete())
			g.drawImage(animation.getImage(), (int) (x+xmap - cwidth/2), (int) (y+ymap - cheight/2), cwidth, cheight, null);
	}
}
