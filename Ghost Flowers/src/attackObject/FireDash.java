package attackObject;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

import gameObject.GameObject;
import playerObject.Player;
import tileMap.TileMap;

public class FireDash extends AttackObject implements Serializable{

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -3678709606757291938L;
	
	private final double DASH_SPEED = 9;
	private final double MAX_LENGTH = 120;
	
	private boolean done;
	
	private ArrayList<Fire> firetrail;
	
	public FireDash(TileMap tilemap, Player p, int damage, double cd, double ed) {
		super(tilemap, p, cd, ed);
		this.damage = damage;
		
		cwidth = cheight = width = height = 30;
		firetrail = new ArrayList<Fire>();
	}
	public void setCooldown(double cd){
		cooldown = cd;
	}
	public void initializeSpritesheet(){
		for(Fire f : firetrail)
			f.initializeSpritesheet();
	}
	public void start(){
		eStartTime = cdStartTime = System.nanoTime();
		done = false;
	}
	//Makes player invulnerable during the dash
	public void update(){
		if(!durationComplete() && !done){
			if(player.getDy() == 0){
				Fire tempf = new Fire(tilemap, player, 0, 7);
				tempf.setPosition(player.getX(), player.getY());
				firetrail.add(tempf);
			}
			if(facingRight){
				if(player.isDashing() && player.isRight() && (player.getX() + player.getDx() < startingX + MAX_LENGTH))
					player.setVector(DASH_SPEED, player.getDy());
				else{
					player.setVector(player.getSpeedPercent(100), player.getDy());
					done = true;
				}
			}
			else{
				if(player.isDashing() && player.isLeft() && (player.getX() + player.getDx() > startingX - MAX_LENGTH))
					player.setVector(-DASH_SPEED, player.getDy());
				else{
					player.setVector(-player.getSpeedPercent(100), player.getDy());
					done = true;
				}

			}
			x = player.getX();
			y = player.getY();
			player.setInvulnerable(true);
		}
		else
			player.setInvulnerable(false);
		
		firetrail.trimToSize();
		for(int i = 0; i < firetrail.size(); i++){
			firetrail.get(i).update();
			if(firetrail.get(i).toBeRemoved()){
				firetrail.remove(i);
			}
		}
	}
	
	public void burn(GameObject o){
		Player p;
		if(o instanceof Player)
			p = (Player) o;
		else
			return;
		
		for(Fire element : firetrail)
			if(element.intersects(p) && !p.getStatus().isBurned()){
				p.setHealth(p.getHealth()-damage);
				p.getStatus().setBurnTick(10);
				p.getStatus().setAilments(StatusAilments.BURN, StatusAilments.COMBO_MARK);
				System.out.println("Player burned.");
			}
	}
	
	public void draw(Graphics2D g){
		setMapPosition();
		for(Fire element : firetrail)
			element.draw(g);
		
	}
	
	

}
