package attackObject;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import gameObject.GameObject;
import playerObject.Player;
import tileMap.TileMap;

public class WaterNAttack extends AttackObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8877737844555272222L;
	private ArrayList<WaterBeam> beam;
	
	//Effect Duration acts as cooldown for this skill
	//Start() to reset it and begin the cooldown.
	public WaterNAttack(TileMap tilemap, Player p, double cd, double ed) {
		super(tilemap, p, cd, ed);
		
		damage = 0;
		
		beam = new ArrayList<WaterBeam>();
	}

	public void initializeSpritesheet() {

	}
	
	public void hit(GameObject o){
		Player p;
		if(o instanceof Player){
			p = (Player) o;
			for(WaterBeam wb : beam)
			if(p.intersects(wb)){
				p.setHealth(p.getHealth()-damage);
				if(p.isFacingRight() == wb.isFacingRight())
					p.getStatus().setMaximumSpeed(p.getSpeedPercent(70));
				else
					p.getStatus().setMaximumSpeed(p.getSpeedPercent(50));
				p.getStatus().setAilments(StatusAilments.SLOW);		
				wb.flagForRemoval();
			}
		}			
	}
	public void update(){		
		if(durationComplete()){	
			WaterBeam tempf = new WaterBeam(tilemap, player, 0, 0.7);
			tempf.setPosition(player.getX(), player.getY());
			tempf.setVector(tempf.getDx(), new Random().nextDouble()*3);
			tempf.setFacingRight(player.isFacingRight());
			beam.add(tempf);			
		}
		for(int j = 0; j < beam.size(); j++){
			beam.get(j).update();
			if(beam.get(j).toBeRemoved())
				beam.remove(j);					
		}
		beam.trimToSize();
			
	}
	public void draw(Graphics2D g){
		setMapPosition();
		
		for(int i = 0; i < beam.size(); i++)
			beam.get(i).draw(g);

	}

}
