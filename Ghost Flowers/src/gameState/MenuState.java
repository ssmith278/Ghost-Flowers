package gameState;

import java.awt.*;
import java.awt.event.KeyEvent;
//import tileMap.Background;
import main.GamePanel;

public class MenuState extends GameState{
	
	//private Background bg;
	
	private int currentChoice = 0;
	private static final String[] CHOICES = 
		{	"- START -",
			"- LOAD -",
			"- EXIT -"
		}; //include additional options
	
	private Color titleColor;
	
	public MenuState(GameStateManager gsm){
		
		this.gsm = gsm;
		init();
	}
	
	public void init(){
		try{
			
			//bg = new Background(/*Location of background image*/"/menu_bg.gif", 1);
			//bg.setVector(0, 0.5);//scroll speed horizontally, vertically
			
			titleColor = new Color(255, 155, 155);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void update(){
		//bg.update();
	}
	public void draw(Graphics2D g){
		
		g.setBackground(Color.BLACK);
		g.clearRect(0, 0, GamePanel.WIDTH*GamePanel.SCALE, GamePanel.HEIGHT*GamePanel.SCALE);
		
		//bg.draw(g);
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		
		drawStringToCenter("Ghost Flowers", GamePanel.HEIGHT/3, g);
		
		g.setFont(font);
		
		for (int i = 0; i < CHOICES.length; i++){
			if (i == currentChoice){
				g.setColor(Color.RED);
			}
			else{
				g.setColor(Color.GRAY);
			}
			drawStringToCenter(CHOICES[i], GamePanel.HEIGHT*2/3 + i * 25, g);
		}
	}
	
	private void select(){
		
		switch(currentChoice){
		
			case 0: gsm.setNewState(GameStateManager.LEVEL1STATE);
					break;
			case 1: loadGame("Resources/Saves/save.ser");
					break;
			case 2:	System.exit(0);
					break;
		}
	}
	
	public void keyPressed(int key){
		
		switch(key){
		
		case KeyEvent.VK_ENTER:	select();
								break;
		case KeyEvent.VK_UP:	currentChoice--;
								if(currentChoice == -1)
									currentChoice = CHOICES.length-1;
								break;
		case KeyEvent.VK_DOWN:	currentChoice++;
								if(currentChoice == CHOICES.length)
									currentChoice = 0;
								break;
		}

	}
	public void keyReleased(int key){}

	public void saveGame(String s) {
		gsm.saveGame(s);
	}
	public void loadGame(String s){
		gsm.loadGame(s);
	}

}
