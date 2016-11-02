package enemyObject;

import java.awt.Graphics2D;
import java.util.ArrayList;

import tileMap.TileMap;

public class EnemyHive extends ArrayList<Enemy>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7235090378539179212L;
	private TileMap tilemap;
	
	public EnemyHive(TileMap tilemap){
		this.tilemap = tilemap;
	}
	public void update(){
		if(!isEmpty()){
			trimToSize();
			for(int i = 0; i < size(); i++){
				get(i).update();
				
				if(get(i).isDead())
					remove(i);
			}
		}
		
	}
	public void draw(Graphics2D g){
		if(!isEmpty()){
			trimToSize();
			for(int i = 0; i < size(); i++)
				get(i).draw(g);
		}
	}

}
