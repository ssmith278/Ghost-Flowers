package gameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import main.GamePanel;

public class EndGameState extends GameState{
	
	private static final String[] CHOICES = 
		{	"- RESTART -",
			"- LOAD LAST SAVE",
			"- MAIN MENU -"
		};
	private int currentChoice = 0;
	
	public EndGameState(GameStateManager gsm){
		this.gsm = gsm;
		init();
	}
	public void init() {}	
	public void update() {}
	public void draw(Graphics2D g) {
		
		int count = 0;
		
		while(count < 10){
			g.setColor(new Color(0,0,0,1));
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			count++;
		}
		
		g.setColor(Color.white);
		g.setFont(titleFont);
		drawStringToCenter("Game Over", GamePanel.HEIGHT/3, g);
		
		g.setFont(font);
		for (int i = 0; i < CHOICES.length; i++){
			if (i == currentChoice){
				g.setColor(Color.RED);
			}
			else{
				g.setColor(Color.WHITE);
			}
			drawStringToCenter(CHOICES[i], GamePanel.HEIGHT* 2/3 + i * 25, g);
		}
	}
	
	private void select(){
		
		switch(currentChoice){
		
			case 0: gsm.setNewState(GameStateManager.LEVEL1STATE);
					break;
			case 1:	loadGame("Resources/Saves/save.ser");
					break;
			case 2:	gsm.setState(GameStateManager.MENUSTATE);
					break;
		}
	}
	
	public void keyPressed(int key) {
		
		switch(key){
		case KeyEvent.VK_ESCAPE:	gsm.setState(GameStateManager.LEVEL1STATE);
									break;
		case KeyEvent.VK_ENTER:		select();
									break;
		case KeyEvent.VK_UP:		currentChoice--;
									if(currentChoice == -1)
										currentChoice = CHOICES.length-1;
									break;
		case KeyEvent.VK_DOWN:		currentChoice++;
									if(currentChoice == CHOICES.length)
										currentChoice = 0;
									break;
		}
	}	
	public void keyReleased(int key) {}
	public void saveGame(String s) {}
	public void loadGame(String s) {
		gsm.loadGame(s);
	}

}
