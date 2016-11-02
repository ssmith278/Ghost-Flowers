package attackObject;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import gameObject.GameObject;
import playerObject.Player;
import tileMap.TileMap;

public class FireNAttack extends AttackObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3318953996945788567L;
	private final String FILE_LOCATION = "/Sprites/Fire_Player/fire_punch.png";
	private final int NUM_FRAMES = 4;
	private final int DAMAGE = 15;
	private final double RECOIL = 2;
	
	public FireNAttack(TileMap tilemap, Player p, double cd, double ed) {
		super(tilemap, p, cd, ed);
		cwidth = width = 45;
		cheight = height = 30;
		cdStartTime = (long) (cd*1E9);
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
			animation.setDelay((long) (effectDuration*10));
			animation.playOneTime(true);
		}
		catch(Exception e){
			e.printStackTrace();
		}		
	}	
	public void updateAS(){
		cooldown = 2*player.getAttackSpeed();
		effectDuration = player.getAttackSpeed();
		animation.setDelay((long) (effectDuration*10));
	}
	
	public void start(){
		eStartTime = cdStartTime = System.nanoTime();
		animation.setCurrentFrame(0);
		
		cwidth = width = 90;
		cheight = height = 60;
		
		if(player.isFacingRight())
			player.setVector(player.getDx()-RECOIL, player.getDy());
		else
			player.setVector(player.getDx()+RECOIL, player.getDy());
	}
	
	public void hit(GameObject o){
		if(!durationComplete()){
			Player p;
			if(o instanceof Player){
				p = (Player) o;
			}
			else
				return;
			
			if(p.intersects(this) && !p.isFlinching()){					
				p.setHealth(p.getHealth()-DAMAGE);
				p.getStatus().setCMDMG(15);
				if(player.isFacingRight())
					p.getStatus().setInitialKnockBack(5);
				else
					p.getStatus().setInitialKnockBack(-5);
				p.getStatus().setAilments(StatusAilments.COMBO_MARK, StatusAilments.KNOCKBACK);
			}
		
		}
	
	}
	
	public void update(){		
		setPosition(player.getX(), player.getY()-height/4);
		animation.update();			
	}
	public void draw(Graphics2D g){
		setMapPosition();
		
		if(!durationComplete())
			if (player.isFacingRight()) {
				g.drawImage(animation.getImage(), (int)(x + xmap - width/2), (int)(y + ymap - height/2), width, height, null);
			}
			else {
				g.drawImage(animation.getImage(), (int)(x + xmap - width/2) + width, (int)(y + ymap - height/2), -width, height, null);
			}
	}

}
