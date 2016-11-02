package playState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import enemyObject.Enemy;
import enemyObject.EnemyHive;
import playerObject.PlayerManager;
import tileMap.Background;
import tileMap.TileMap;
import fileObject.Properties;
import gameObject.CheckPointList;
import gameObject.NPCList;
import gameState.GameState;

public abstract class PlayState extends GameState{
	
	protected Properties properties;
	protected PlayerManager playermngr;
	protected EnemyHive hive;
	protected TileMap tilemap;
	protected CheckPointList checkpoints;
	protected NPCList npcs;
	protected Background bg;
	
	protected boolean saving = false;
	protected boolean loading = false;
	
	public void saveGame(String s){
		
		if(!saving){
			saving = true;
			
			Properties prop = new Properties(s, this);
			File file = new File(System.getProperty("user.dir"), s);
			try (FileOutputStream fop = new FileOutputStream(s)){
				
				if (!file.exists()){
					file.createNewFile();
				}
				
				ObjectOutputStream op = new ObjectOutputStream(fop);
				op.writeObject(prop);
				op.close();
			}
			catch (IOException e){
				e.printStackTrace();
			}
			System.out.println("Game Saved");
			
			saving = false;
		}
		else
			System.out.println("Saving Game In Progress");
				
	}
	
	public void loadGame(String s){
		
		if(!loading){
			loading = true;
			
			try (FileInputStream fip = new FileInputStream(s)){
				
				ObjectInputStream in = new ObjectInputStream(fip);
				properties = (Properties) in.readObject();
				in.close();
				
				playermngr = properties.getPlayerManager();
				int tempElement = playermngr.getElement();
				for(int i = 0; i < PlayerManager.NUM_PLAYERS; i++){
					playermngr.setElement(i);
					playermngr.setIdle();
					playermngr.disableMovementBooleans();
				}
				playermngr.setElement(tempElement);
				
				for(int i = 0; i < properties.getEnemyHive().size(); i++){
					properties.getEnemyHive().get(i).initializeSpritesheet();
					properties.getEnemyHive().get(i).setIdle();
				}
				
				tilemap = properties.getTilemap();
				checkpoints = properties.getCheckpoints();
				npcs = properties.getNpcs();
				hive = properties.getEnemyHive();
				bg = properties.getBackground();
				bg.importBackground();
				
			}
			catch(IOException e){
				e.printStackTrace();
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("Game Loaded");
			
			loading = false;
		}
		else
			System.out.println("Loading Game In Progress");
		
	}

	/**
	 * @return the player
	 */
	public PlayerManager getPlayerManager() {
		return playermngr;
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
	public Background getBackground(){
		return bg;
	}
	
	

}
