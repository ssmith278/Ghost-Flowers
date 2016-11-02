package fileObject;

import java.io.Serializable;

import enemyObject.EnemyHive;
import playState.PlayState;
import playerObject.PlayerManager;
import gameObject.CheckPointList;
import gameObject.NPCList;
import tileMap.Background;
import tileMap.TileMap;

public class Properties implements Serializable{

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -4192286531664480819L;
	
	private TileMap tilemap;
	private PlayerManager playermngr;
	private CheckPointList checkpoints;
	private NPCList npcs;
	private EnemyHive hive;
	private Background bg;
	
	private String filepath;
	
	public Properties(String s, PlayState ps){
		filepath = s;
		tilemap = ps.getTilemap();
		playermngr = ps.getPlayerManager();
		checkpoints = ps.getCheckpoints();
		npcs = ps.getNpcs();
		hive = ps.getEnemyHive();
		bg = ps.getBackground();
		
	}

	public PlayerManager getPlayerManager(){
		return playermngr;
	}
	public Background getBackground(){
		return bg;
	}
	/**
	 * @return the tilemap
	 */
	public TileMap getTilemap() {
		return tilemap;
	}
	/**
	 * @return the checkpoints
	 */
	public CheckPointList getCheckpoints() {
		return checkpoints;
	}

	/**
	 * @return the npcs
	 */
	public NPCList getNpcs() {
		return npcs;
	}
	
	public EnemyHive getEnemyHive(){
		return hive;
	}
	
	
}
