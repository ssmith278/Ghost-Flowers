package gameObject;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

import playerObject.PlayerManager;
import tileMap.TileMap;

public class CheckPointList implements Serializable{

	/**
	 *  Serial ID
	 */
	private static final long serialVersionUID = 5025659625664767016L;
	private ArrayList<CheckPoint> checkpoints;
	private int currentCheckPoint, nextCheckPoint, startingCheckPoint;
	
	public CheckPointList(TileMap tilemap, int numpoints){
		
		startingCheckPoint = 0;
		currentCheckPoint = 0;
		nextCheckPoint = currentCheckPoint+1;
		
		checkpoints = new ArrayList<CheckPoint>();
		
		for(int i = 0; i < numpoints; i++){
			checkpoints.add(new CheckPoint(tilemap));
			checkpoints.get(i).setPosition(0,0);
		}
		
		checkpoints.get(startingCheckPoint).setExpended(true);
		
	}
	
	public CheckPoint getCheckPoint(int i){ return checkpoints.get(i); }
	public int getCurrentCheckPoint(){ return currentCheckPoint; }
	public double getCurrentCheckPointX(){ return checkpoints.get(currentCheckPoint).getX(); }
	public double getCurrentCheckPointY(){ return checkpoints.get(currentCheckPoint).getY(); }
	public double getNextCheckPointX(){ return checkpoints.get(nextCheckPoint).getX(); }
	public double getNextCheckPointY(){ return checkpoints.get(nextCheckPoint).getY(); }
	public double getStartingCheckPointX(){ return checkpoints.get(startingCheckPoint).getX(); }
	public double getStartingCheckPointY(){ return checkpoints.get(startingCheckPoint).getY(); }
	
	public void setCurrentCheckPoint(int currentpoint){
		if(currentpoint >= checkpoints.size())
			currentCheckPoint = startingCheckPoint+1;
		else
			currentCheckPoint = currentpoint;
		
		nextCheckPoint = (currentCheckPoint+1) % (checkpoints.size());		
	}
	
	public void setCheckPointPosition(int index, int x, int y){
		checkpoints.get(index).setPosition(x, y);
	}
	
	public String getExpendedStates(){
		String s = "";
		
		for(int i = 0; i < checkpoints.size(); i++)
			if(i != checkpoints.size()-1)
				s += checkpoints.get(i).isExpended() + System.lineSeparator();
			else
				s += checkpoints.get(i).isExpended();
		
		System.out.println(s);
		
		return s;
	}
	
	public void checkPlayerCheckPointCollision(PlayerManager player){
		for(int i = 0; i < checkpoints.size(); i++)
			if(player.intersects(checkpoints.get(i))){
				if(!checkpoints.get(i).isExpended()){
					checkpoints.get(i).setExpended(true);
					setCurrentCheckPoint(i);
					break;
				}
				
			}
		
	}
	
	public void draw(Graphics2D g){
		if(nextCheckPoint != 0 && !checkpoints.get(nextCheckPoint).isExpended())
			checkpoints.get(nextCheckPoint).draw(g);
	}
	
	public int size(){
		return checkpoints.size();
	}
	
}
