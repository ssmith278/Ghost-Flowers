package gameState;

import java.awt.Font;
import java.awt.Graphics2D;

import main.GamePanel;

public abstract class GameState {
	
	protected GameStateManager gsm;
	protected int stringx, stringy;
	protected boolean initializing;
	protected String defaultSaveFolder = "Resources/Saves/";
	
	protected static Font titleFont = new Font("Cambria", Font.PLAIN, GamePanel.WIDTH/20);
	protected static Font font = new Font("Cambria", Font.PLAIN, GamePanel.WIDTH/30);
	
	public abstract void init();
	public abstract void update();
	public abstract void saveGame(String s);
	public abstract void loadGame(String s);
	public abstract void draw(Graphics2D g);
	public abstract void keyPressed(int key);
	public abstract void keyReleased(int key);
	public static int getStringCenteredX(String s, Graphics2D g){
		int stringLength = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		int stringOrigin = GamePanel.WIDTH/2 - stringLength/2;
		
		return stringOrigin;
	}
	public static void drawStringToCenter(String s, Graphics2D g){
		int stringLength = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		int stringOrigin = GamePanel.WIDTH/2 - stringLength/2;
		g.drawString(s, stringOrigin, GamePanel.HEIGHT/2);
	}
	public static void drawStringToCenter(String s, int height, Graphics2D g){
		int stringLength = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		int stringOrigin = GamePanel.WIDTH/2 - stringLength/2;
		g.drawString(s, stringOrigin, height);
	}
}
