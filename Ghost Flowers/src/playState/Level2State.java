package playState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import enemyObject.EnemyHive;
import gameState.GameStateManager;
import main.GamePanel;
import playerObject.PlayerManager;
import tileMap.TileMap;

public class Level2State extends PlayState {
	
	public Level2State(GameStateManager gsm){
		this.gsm = gsm;
		init();
	}

	public void init() {
		tilemap = new TileMap(30);
		tilemap.loadTiles("/Tilesets/grasstileset.gif");
		tilemap.loadMap("/Maps/2_maptexture.map");
		tilemap.setPosition(0,0);
		
		hive = new EnemyHive(tilemap);
		
		playermngr = new PlayerManager(tilemap, hive);
		playermngr.setElement(PlayerManager.WATER_PLAYER);
		playermngr.setPosition(GamePanel.WIDTH/2, GamePanel.HEIGHT/10);
	}

	public void update() {
		if(!saving && !loading){
			
			playermngr.update();
			
			if(playermngr.notOnScreen() && !playermngr.isFlinching()){
				playermngr.setPosition(GamePanel.WIDTH/2, GamePanel.HEIGHT/10);
				playermngr.startFlinching();
				
			}	
			
			tilemap.setPosition(GamePanel.WIDTH/2 - playermngr.getX(), GamePanel.HEIGHT/2 - playermngr.getY());
			tilemap.update();
			tilemap.fixBounds();
		}
	}

	public void draw(Graphics2D g) {
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, GamePanel.WIDTH*GamePanel.SCALE, GamePanel.HEIGHT*GamePanel.SCALE);
		
		tilemap.draw(g);
		
		g.setColor(Color.BLACK);
		g.setFont(font);
		g.drawString("HP: " + playermngr.getHealth() + "/" + playermngr.getMaxHealth(), GamePanel.WIDTH/15, GamePanel.HEIGHT/15);
		
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
		case KeyEvent.VK_DOWN:		
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
