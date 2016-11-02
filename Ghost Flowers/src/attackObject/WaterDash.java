package attackObject;

import gameObject.GameObject;
import playerObject.Player;
import tileMap.TileMap;

public class WaterDash extends AttackObject {


	/**
	 * 
	 */
	private static final long serialVersionUID = -603320597331366608L;
	private final double DASH_SPEED = 9;
	private final double MAX_LENGTH = 90;
	
	public WaterDash(TileMap tilemap, Player p, double cd, double ed) {
		super(tilemap, p, cd, ed);
		damage = 0;
	}

	public void initializeSpritesheet() {}
	
	public void update(){
		if(!durationComplete()){
			if(facingRight){
				if(player.isDashing() && player.isRight())
					player.setVector(player.getDx() + DASH_SPEED, player.getDy());
				if (player.getX() + player.getDx() >= startingX + MAX_LENGTH){
					player.setVector(player.getDx()/5,player.getDy());
				}
			}
			else{
				if(player.isDashing() && player.isLeft())
					player.setVector(player.getDx() - DASH_SPEED, player.getDy());
				if (player.getX() + player.getDx() <= startingX - MAX_LENGTH){
					player.setVector(player.getDx()/5,player.getDy());
				}
			}
			
			player.setInvulnerable(true);
		}
		else
			player.setInvulnerable(false);
	}
	
	public void hit(GameObject o){
		Player p;
		if(o instanceof Player)
			p = (Player) o;
		else
			return;
		
		if(p.intersects(player) && !p.isFlinching()){			
			if(player.isFacingRight())
				p.getStatus().setInitialKnockBack(-10);
			else
				p.getStatus().setInitialKnockBack(10);
			
			p.getStatus().setAilments(StatusAilments.KNOCKBACK);
			
			p.setHealth(p.getHealth()-damage);
		}		
	}

}
