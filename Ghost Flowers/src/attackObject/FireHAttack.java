package attackObject;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import gameObject.GameObject;
import playerObject.Player;
import tileMap.TileMap;

public class FireHAttack extends AttackObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1563924845268381237L;
	private final String FILE_LOCATION = "/Sprites/Fire_Player/fire_heavy_charge.gif";
	private final int[] NUM_FRAMES = {
			6, 6, 6, 3
	};
	private final int CHARGE_1 = 0;
	private final int CHARGE_2 = 1;
	private final int CHARGE_3 = 2;
	private final int DETONATE = 3;
	
	private final double FORCE = 1.5;
	
	transient private ArrayList<BufferedImage[]> sprites;
	
	private int chargeLVL;
	private long chargeDelay;
	private double ch1Time, ch2Time;
	private boolean charging;
	
	public FireHAttack(TileMap tilemap, Player p, double cd, double ed) {
		super(tilemap, p, cd, ed);
		chargeLVL = CHARGE_1;
		chargeDelay = (long) (ed*100);
		charging = false;
		cwidth = cheight = 30;
		width = height = 30;
		
		damage = 70;
		
		ch1Time = effectDuration/3;
		ch2Time = effectDuration/3 + ch1Time;
		
		initializeSpritesheet();
	}

	public void initializeSpritesheet() {
		try{			
			BufferedImage spritesheet;
			spritesheet = ImageIO.read(getClass().getResourceAsStream(FILE_LOCATION));			
			sprites = new ArrayList<BufferedImage[]>();
			
			//4 sets of animations - increase if more animations are added
			for (int i = 0; i < NUM_FRAMES.length; i++){
				BufferedImage[] images = new BufferedImage[NUM_FRAMES[i]];
				for (int j = 0; j < NUM_FRAMES[i]; j++){
					//change the width/height if sprites are not default size
					images[j] = spritesheet.getSubimage(j * width, i * height, width, height);
					
				}
				sprites.add(images);
			}
			
			animation.setFrames(sprites.get(CHARGE_1));
			animation.setDelay(chargeDelay);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}	
	public boolean fullyCharged(){
		return chargeLVL == CHARGE_3;
	}
	public void setCharge(boolean b){
		if(charging != b)
			charging = b;
		
		if(!b)
			player.getStatus().removeSlow();
	}
	public void detonate(GameObject o){
		Player p;
		if(o instanceof Player)
			p = (Player) o;
		else
			return;
		
		if(p.intersects(this)){
			p.setHealth(p.getHealth() - damage);
			do{
				p.getStatus().setCMDMG(30);
				p.getStatus().setAilments(StatusAilments.COMBO_MARK);
			}while(p.getStatus().isMarked());
		}
		
		setCharge(false);
		if(currentAction != DETONATE){
			currentAction = DETONATE;
			cwidth = cheight = 60;
			animation.setFrames(sprites.get(DETONATE));
			animation.playOneTime(true);
			animation.setDelay(25);
			
			chargeLVL = DETONATE;			
			System.out.println("Boom.");
		}		
	}
	public void pull(GameObject o){
		if(o.intersects(this))
			if(o.getX() > x && o.getY() > y)
				o.setVector(o.getDx() - FORCE, o.getDy() - FORCE);
			else if(o.getX() > x && o.getY() < y)
				o.setVector(o.getDx() - FORCE, o.getDy() + FORCE);
			else if(o.getX() < x && o.getY() > y)
				o.setVector(o.getDx() + FORCE, o.getDy() - FORCE);
			else if(o.getX() < x && o.getY() < y)
				o.setVector(o.getDx() + FORCE, o.getDy() + FORCE);
			else
				o.setVector(0, -FORCE);
	}
	public void update(){
		setPosition(player.getX(), player.getY());
		
		if(charging){
			cwidth = cheight = 60;
			player.getStatus().setMaximumSpeed(player.getSpeedPercent(80));
			player.getStatus().setAilments(StatusAilments.SLOW);
			if(getEDTimer() < ch1Time)
				chargeLVL = CHARGE_1;
			else if(getEDTimer() < ch2Time)
				chargeLVL = CHARGE_2;
			else
				chargeLVL = CHARGE_3;
		}
		else if(currentAction == DETONATE && animation.hasPlayedOnce()){
				chargeLVL = CHARGE_1;
		}

				
				
			
		switch(chargeLVL){
			case CHARGE_1:	if(currentAction != CHARGE_1){
								currentAction = CHARGE_1;
								cwidth = cheight = 60;
								animation.setFrames(sprites.get(CHARGE_1));
								animation.setDelay(chargeDelay);
								animation.playOneTime(false);
							}
							break;
			case CHARGE_2:	if(currentAction != CHARGE_2){
								currentAction = CHARGE_2;
								cwidth = cheight = 60;
								animation.setFrames(sprites.get(CHARGE_2));
								animation.setDelay(chargeDelay);
							}
							break;
			case CHARGE_3:	if(currentAction != CHARGE_3){
								currentAction = CHARGE_3;
								cwidth = cheight = 60;
								animation.setFrames(sprites.get(CHARGE_3));
								animation.setDelay(chargeDelay);
							}
							break;
		}
		animation.update();
	}
	
	public void draw(Graphics2D g){
		setMapPosition();
		
		if(charging || currentAction == DETONATE)
			if (facingRight) {
				g.drawImage(animation.getImage(), (int)(x + xmap - cwidth/2), (int)(y + ymap - cheight/2), cwidth, cheight, null);
			}
			else {
				g.drawImage(animation.getImage(), (int)(x + xmap - cwidth/2) + cwidth, (int)(y + ymap - cheight/2), -cwidth, cheight, null);
			}
	}

}
