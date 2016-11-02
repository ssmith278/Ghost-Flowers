package playerObject;

import gameObject.GameObject;
import gameObject.Personality;

import java.util.ArrayList;

import attackObject.StatusAilments;
import enemyObject.EnemyHive;
import tileMap.*;

import java.awt.Graphics2D;
import java.io.Serializable;

public class PlayerManager implements Serializable{

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 6763662607494453656L;
	
	public static final int FIRE_PLAYER = 0;
	public static final int AIR_PLAYER = 1;
	public static final int WATER_PLAYER = 2;
	public static final int EARTH_PLAYER = 3;
	
	public static final int NUM_PLAYERS = 4;
	
	private Personality personality;
	
	private ArrayList<Player> players;
	private int currentPlayer;
	
	private boolean isFire, isAir, isWater, isEarth;
	
	public PlayerManager(TileMap tilemap, EnemyHive hive) {		
		players = new ArrayList<Player>();
		
		players.add(new FirePlayer(tilemap, hive));
		players.add(new AirPlayer(tilemap, hive));
		players.add(new WaterPlayer(tilemap, hive));
		players.add(new EarthPlayer(tilemap, hive));
		
		currentPlayer = 0;
		
		personality = new Personality(this);
	}
	
	private void setPlayer(int player){
		players.get(player).setPosition(getX(), getY());
		players.get(player).setVector(getDX(), getDY());
		copyMovementVariables(players.get(player));
		players.get(player).setMapPosition();
		currentPlayer = player;
	}
	
	public Player getPlayer(int player){
		if(player < players.size() && player >= 0)
			return players.get(player);
		
		return null;
	}

	public void addAggression(int aggression) { personality.addAggression(aggression); }
	public void addSwiftness(int swiftness) { personality.addSwiftness(swiftness); }
	public void addMalevolence(int malevolence) { personality.addMalevolence(malevolence); }
	public void addSubtlety(int subtlety) { personality.addSubtlety(subtlety); }
	public void addCommanding(int commanding) { personality.addCommanding(commanding); }
	
	public void setStatus(int... status){players.get(currentPlayer).getStatus().setAilments(status);}
	public StatusAilments getStatus(){ return players.get(currentPlayer).getStatus(); }
	/**
	 * Sets the player's element to the given element
	 * @param element Element to set player to
	 */
	public void setElement(int element){
		
		isFire = isAir = isEarth = isWater = false;
		
		switch(element){
		
			case Personality.FIRE:	setPlayer(FIRE_PLAYER);
									isFire = true;
									break;
			case Personality.AIR:	setPlayer(AIR_PLAYER);
									isAir = true;
									break;
			case Personality.EARTH:	setPlayer(EARTH_PLAYER);
									isEarth = true;
									break;
			case Personality.WATER:	setPlayer(WATER_PLAYER);
									isWater = true;
									break;
			default:				setPlayer(FIRE_PLAYER);
									isFire = true;
									System.out.println("Invalid Element. Element reset.");
		}
		
		initializeSpritesheet();
	}
	
	/**
	 * Gets the player element
	 * @return Player's current element
	 */
	public int getElement(){
		if(isFire)
			return Personality.FIRE;
		else if(isAir)
			return Personality.AIR;
		else if(isEarth)
			return Personality.EARTH;
		else if(isWater)
			return Personality.WATER;
		else
			return -1;
	}
	
	public void update(){ players.get(currentPlayer).update(); }
	public void initializeSpritesheet(){ players.get(currentPlayer).initializeSpritesheet(); }
	public void draw(Graphics2D g){ players.get(currentPlayer).draw(g); }
	
	public void setPosition(double x, double y){ players.get(currentPlayer).setPosition(x, y); }
	public double getX(){ return players.get(currentPlayer).getX();	}
	public double getY(){ return players.get(currentPlayer).getY();	}
	public double getDX(){ return players.get(currentPlayer).getDx(); }
	public double getDY(){ return players.get(currentPlayer).getDy(); }
	public boolean intersects(GameObject o){ return players.get(currentPlayer).intersects(o); }
	public boolean notOnScreen(){ return players.get(currentPlayer).notOnScreen(); }
	
	public void setUp(boolean b){ players.get(currentPlayer).setUp(b); }
	public void setDown(boolean b){ players.get(currentPlayer).setDown(b); }
	public void setLeft(boolean b){ players.get(currentPlayer).setLeft(b); }
	public void setRight(boolean b){ players.get(currentPlayer).setRight(b); }
	public void setJumping(boolean b){ players.get(currentPlayer).setJumping(b); }
	public void setDashing(boolean b){ players.get(currentPlayer).setDashing(b); }
	public void setNAttacking(boolean b){ players.get(currentPlayer).setNAttacking(b); }
	public void setHAttacking(boolean b){ players.get(currentPlayer).setHAttacking(b); }
	public void setSAttacking(boolean b){ players.get(currentPlayer).setSAttacking(b); }
	
	public void setIdle(){ players.get(currentPlayer).setIdle(); }
	public void copyMovementVariables(Player p){
		p.setLeft(players.get(currentPlayer).isLeft());
		p.setRight(players.get(currentPlayer).isRight());
		p.setUp(players.get(currentPlayer).isUp());
		p.setDown(players.get(currentPlayer).isDown());
		p.setDashing(players.get(currentPlayer).isDashing());
		p.setJumping(players.get(currentPlayer).isJumping());
	}
	public void disableMovementBooleans(){ players.get(currentPlayer).disableMovementBooleans(); }
	
	public boolean isFlinching(){ return players.get(currentPlayer).isFlinching(); }
	public void startFlinching(){ players.get(currentPlayer).startFlinching(); }
	
	public void decrementHealth(){ 
		for(int i = 0; i < players.size(); i++)
		players.get(i).decrementHealth(); 
	}
	public int getHealth(){	return players.get(currentPlayer).getHealth(); }
	public int getMaxHealth(){ return players.get(currentPlayer).getMaxHealth(); }
	public boolean isDead(){ return players.get(currentPlayer).isDead(); }
	public void revive(){ players.get(currentPlayer).revive(); }

}
