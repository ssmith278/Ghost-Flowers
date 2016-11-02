package enemyObject;

import java.awt.Graphics2D;

import tileMap.TileMap;

public class Grunt extends Enemy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5881171496177505103L;

	public Grunt(TileMap tilemap, EnemyHive hive, double x, double y) {
		super(tilemap, hive, x, y);
		dx = moveSpeed;
		right = true;
	}
	
	protected void getNextPosition(){
		super.getNextPosition();
	}
	
	public void update(){
		super.update();
		if(dx == 0 && right){
			dx = -moveSpeed;
			right = false;
			left = true;
		}
		else if(dx == 0 && left){
			dx = moveSpeed;
			left = false;
			right = true;
		}
	}
	
	public void draw(Graphics2D g){
		super.draw(g);
	}

}
