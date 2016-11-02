package playState;

import gameObject.*;
import gameState.GameStateManager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import attackObject.StatusAilments;
import enemyObject.Enemy;
import enemyObject.EnemyHive;
import enemyObject.Grunt;
import playerObject.PlayerManager;
import tileMap.Background;
import tileMap.TileMap;
import main.GamePanel;

public class Level1State extends PlayState {
	
	private final String TRAINER = "Trainer";
	private final String INSTRUCTIONS = "Instructions";
	
	private final double SPAWN_TIME = 5.0;
	private long spawnStartTime;
	
	public Level1State(GameStateManager gsm){
		this.gsm = gsm;
		init();
	}
	
	public Level1State(GameStateManager gsm, String s){
		this.gsm = gsm;		
		loadGame(s);
		tilemap.loadTiles("/Tilesets/grasstileset.gif");
		tilemap.loadMap("/Maps/maptexture.map");
	}
	
	public void init() {
		
		tilemap = new TileMap(30);
		tilemap.loadTiles("/Tilesets/grasstileset.gif");
		tilemap.loadMap("/Maps/maptexture.map");
		tilemap.setPosition(0,0);
		
		bg = new Background("/Backgrounds/level1BG.png", .15, tilemap.getWidth(), tilemap.getHeight());
		bg.setPosition(0,0);
		
		checkpoints = new CheckPointList(tilemap, 3);
		checkpoints.setCheckPointPosition(0, 100, 100);
		checkpoints.setCheckPointPosition(1, 490, 560);
		checkpoints.setCheckPointPosition(2, 350, 170);
		
		hive = new EnemyHive(tilemap);
		
		playermngr = new PlayerManager(tilemap, hive);
		playermngr.setElement(PlayerManager.WATER_PLAYER);
		playermngr.setPosition(checkpoints.getStartingCheckPointX(),checkpoints.getStartingCheckPointY());
		
		//personality = new Personality(player);
		
		npcs = new NPCList();
		npcs.addNPC(new NPC(tilemap, TRAINER, 100, 550, 5000));
			npcs.getNPC(TRAINER).populateDialogue("Resources/Dialogue/npc1.txt");
		npcs.addNPC(new NPC(tilemap, INSTRUCTIONS, 5000));
			npcs.getNPC(INSTRUCTIONS).populateDialogue("Resources/Dialogue/instructions.txt");
			
		//spawnStartTime = System.nanoTime();
	}
	
	public void update() {
		
		if(!saving && !loading){			
		
			//Updates background
			bg.setPosition(tilemap.getX(),tilemap.getY());
			
			//Updates player position and animation
			playermngr.update();
			
			//Updates enemy position and animation
			hive.update();
			if(SPAWN_TIME < (System.nanoTime() - spawnStartTime)/1E9){
				spawnStartTime = System.nanoTime();
				Grunt e = new Grunt(tilemap, hive, checkpoints.getStartingCheckPointX(), checkpoints.getStartingCheckPointY());
				hive.add(e);
			}
			
			//Checks if player has collected a checkpoint
			checkpoints.checkPlayerCheckPointCollision(playermngr);
			
			if(checkpoints.getCurrentCheckPoint() == 1 && !tilemap.isShaking()){
				tilemap.setShaking(true, 3);
				npcs.stopAllDialogue();
				npcs.getNPC(TRAINER).populateDialogue("Resources/Dialogue/npc1_warning.txt");
				if(!npcs.getNPC(TRAINER).playedOnce()){
					npcs.getNPC(TRAINER).triggerDialogue();
				}
			}
			
			//Populates dialogue with end game lines if player has collected all checkpoints
			if(checkpoints.getCurrentCheckPoint() == checkpoints.size()-1
					&& !npcs.getNPC(TRAINER).isTriggered()
					&& npcs.getNPC(TRAINER).getCurrentFile() != "Resources/Dialogue/npc1_end.txt"){
				tilemap.setShaking(false, 0);
				npcs.getNPC(TRAINER).populateDialogue("Resources/Dialogue/npc1_end.txt");
			}
			
			if(checkpoints.getCurrentCheckPoint() == checkpoints.size()-1 &&
					npcs.getNPC(TRAINER).playedOnce() && !npcs.getNPC(TRAINER).isTriggered())
					gsm.setState(GameStateManager.LEVEL2STATE);
			
			//Trigger initial instruction dialogue
			if(playermngr.getY() < GamePanel.HEIGHT/2 && !npcs.getNPC(INSTRUCTIONS).playedOnce())
				npcs.getNPC(INSTRUCTIONS).triggerDialogue();
			
			//Check if player fell off map and display player fell messages if so
			if(playermngr.notOnScreen() && !playermngr.isFlinching()){
				playermngr.setPosition(checkpoints.getCurrentCheckPointX(),checkpoints.getCurrentCheckPointY());
				playermngr.decrementHealth();
				playermngr.startFlinching();
				npcs.stopAllDialogue();
				if(playermngr.getHealth() == 2){
					npcs.getNPC(INSTRUCTIONS).populateDialogue("Resources/Dialogue/player_fell.txt");
					npcs.getNPC(INSTRUCTIONS).triggerDialogue();
				}
				else if(playermngr.getHealth() == 1){
					npcs.getNPC(INSTRUCTIONS).populateDialogue("Resources/Dialogue/player_fell2.txt");
					npcs.getNPC(INSTRUCTIONS).triggerDialogue();
				}
				
			}
			
			//Check for flinching and dashing invincibility			
			
			//Checks player health for death state
			if(playermngr.isDead()){
				playermngr.revive();
				gsm.setNewState(GameStateManager.ENDGAMESTATE);
			}
			
			tilemap.setPosition(GamePanel.WIDTH/2 - playermngr.getX(), GamePanel.HEIGHT/2 - playermngr.getY());
			tilemap.update();
			tilemap.fixBounds();
		
		}
		
	}	
	
	public void draw(Graphics2D g) {
		//Draw background
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, GamePanel.WIDTH*GamePanel.SCALE, GamePanel.HEIGHT*GamePanel.SCALE);
		bg.draw(g);
		
		//Draw tilemap
		tilemap.draw(g);
		
		//Draw NPC
		npcs.getNPC(TRAINER).draw(g);
		
		//Draw checkpoints
		checkpoints.draw(g);
		
		//Draw enemies
		hive.draw(g);
		
		//Draw HUD
		
		g.setColor(Color.BLACK);
		g.setFont(font);
		g.drawString("HP: " + playermngr.getHealth() + "/" + playermngr.getMaxHealth(), GamePanel.WIDTH/15, GamePanel.HEIGHT/15);
		
		
		//Draw NPC dialogue
		if(npcs.getNPC(TRAINER).isTriggered())
			npcs.getNPC(TRAINER).drawDialogue(g);
		
		//Draw starting instruction dialogue
		if(npcs.getNPC(INSTRUCTIONS).isTriggered())
			npcs.getNPC(INSTRUCTIONS).drawDialogue(g);
		
		//Draw player
		playermngr.draw(g);
				
	}
	
	public void keyPressed(int key) {		
		switch(key){
			case KeyEvent.VK_ESCAPE:	gsm.setNewState(GameStateManager.PAUSESTATE);
										break;
			case KeyEvent.VK_LEFT:		playermngr.setLeft(true);
										break;
			case KeyEvent.VK_RIGHT:		playermngr.setRight(true);
										break;
			case KeyEvent.VK_UP:		playermngr.setUp(true);
										break;
			case KeyEvent.VK_DOWN:		playermngr.setDown(true);
										if(playermngr.intersects(npcs.getNPC(TRAINER))){
											npcs.stopAllDialogue();
											npcs.getNPC(TRAINER).triggerDialogue();
										}
										break;
			case KeyEvent.VK_SPACE:		playermngr.setJumping(true);
										break;
			case KeyEvent.VK_SHIFT:		playermngr.setDashing(true);
										break;
			case KeyEvent.VK_D:			playermngr.setNAttacking(true);
										break;
			case KeyEvent.VK_S:			playermngr.setHAttacking(true);
										break;
			case KeyEvent.VK_A:			playermngr.setSAttacking(true);
										break;
			case KeyEvent.VK_ENTER:		playermngr.setElement(playermngr.getElement()+1);
		}
	}	
	public void keyReleased(int key) {
		switch(key){
			case KeyEvent.VK_LEFT:		playermngr.setLeft(false);
										break;
			case KeyEvent.VK_RIGHT:		playermngr.setRight(false);
										break;
			case KeyEvent.VK_UP:		playermngr.setUp(false);
										break;
			case KeyEvent.VK_DOWN:		playermngr.setDown(false);
										break;
			case KeyEvent.VK_SPACE:		playermngr.setJumping(false);
										break;
			case KeyEvent.VK_SHIFT:		playermngr.setDashing(false);
										break;
			case KeyEvent.VK_D:			playermngr.setNAttacking(false);
										break;
			case KeyEvent.VK_S:			playermngr.setHAttacking(false);
										break;
			case KeyEvent.VK_A:			playermngr.setSAttacking(false);
		}
	}

}
