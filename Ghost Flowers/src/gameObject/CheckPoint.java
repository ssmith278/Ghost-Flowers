package gameObject;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

import tileMap.TileMap;

public class CheckPoint extends GameObject implements Serializable{
	
	/**
	 *  Serial ID
	 */
	private static final long serialVersionUID = -23644892839275049L;
	private boolean isExpended;

	public CheckPoint(TileMap tilemap) {
		super(tilemap);
		
		width = 30;
		height = 30;
		cwidth = 30;
		cheight = 30;
		
		isExpended = false;
	}
	
	public void draw(Graphics2D g){
		setMapPosition();
		g.setColor(Color.BLACK);
		g.fillRect((int)(x+xmap) - width/2, (int)(y+ymap) - height/2, width, height);
	}
	
	public void setExpended(boolean b){
		isExpended = b;
	}
	
	public boolean isExpended(){
		return isExpended;
	}
}
