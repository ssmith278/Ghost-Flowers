package attackObject;

import gameObject.GameObject;
import playerObject.Player;
import tileMap.TileMap;

public class AirDash extends AttackObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2672233663758323157L;
	private final double DASH_SPEED = 9;
	private final double MAX_LENGTH = 90;
	
	public AirDash(TileMap tilemap, Player p, double cd, double ed) {
		super(tilemap, p, cd, ed);
		damage = 20;
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
			p.setHealth(p.getHealth()-damage);
			p.getStatus().setInitialKnockUp(-10);
			p.getStatus().setAilments(StatusAilments.KNOCKUP);
		}		
	}
}
