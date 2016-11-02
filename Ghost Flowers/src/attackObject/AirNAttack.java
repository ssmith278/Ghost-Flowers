package attackObject;

import gameObject.GameObject;
import playerObject.Player;
import tileMap.TileMap;

public class AirNAttack extends AttackObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7063289482325655245L;
	private final int DAMAGE = 2;
	public AirNAttack(TileMap tilemap, Player p, double cd, double ed) {
		super(tilemap, p, cd, ed);
		cwidth = width = cheight = height = 30;
	}
	public void initializeSpritesheet(){}
	
	public void hit(GameObject o){
		start();
		animation.setCurrentFrame(0);
		if(o instanceof Player){
			o = (Player) o;
			if(player.intersects(o)){					
				((Player) o).setHealth(((Player) o).getHealth()-DAMAGE);
				((Player) o).getStatus().setInitialKnockUp(-2);
				((Player) o).getStatus().setAilments(StatusAilments.KNOCKUP);
			}
		}
	
	}
	public void update(){
		setPosition(player.getX(), player.getY());
	}

}
