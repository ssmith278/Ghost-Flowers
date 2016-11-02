package gameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import main.GamePanel;

public class SaveState extends GameState{

	private final int MAX_SAVES = 3;
	private ArrayList<String> choices;
	private int recentSave = 0;
	private int currentChoice = 0;
	
	public SaveState(GameStateManager gsm){
		this.gsm = gsm;
		choices = new ArrayList<String>();
		init();
	}
	public void init() {
		choices.clear();
		choices.add("NEW SAVE");
		scanFolder();
		choices.add("BACK");
	}	
	public void update() {}
	public void draw(Graphics2D g) {
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
				
		g.setColor(Color.white);
		g.setFont(titleFont);
		drawStringToCenter("Save Game", GamePanel.HEIGHT/3, g);
		
		g.setFont(font);
		for (int i = 0; i < choices.size(); i++){
			if (i == currentChoice){
				g.setColor(Color.RED);
			}
			else{
				g.setColor(Color.WHITE);
			}
			drawStringToCenter(choices.get(i).toUpperCase(), GamePanel.HEIGHT*2/3 + i * 25, g);
		}
	}
	
	private void select(){
		
		if(currentChoice == 0)
			saveGame(defaultSaveFolder + "save" + (recentSave + 1) + ".save");
		else if(currentChoice > 0 && currentChoice < choices.size()-1)
			saveGame(defaultSaveFolder + choices.get(currentChoice));
		else if(currentChoice == choices.size()-1)
			gsm.setState(gsm.lastState());	
			
		
	}
	
	private void scanFolder(){
		String filename = "";
		
		final File folder = new File(defaultSaveFolder);
		for(int i = 0; i < folder.listFiles().length; i++){
			filename = folder.listFiles()[i].getName();
			File currentFile = new File(filename);
			if(!currentFile.isDirectory() && filename.substring(filename.indexOf("."),filename.length()).equals(".ser")){
				choices.add(filename);
			}
		}
		
		for(int i = 1; i < choices.size()-1; i++)
			if(choices.get(i).compareToIgnoreCase(choices.get(recentSave)) > 0)
				recentSave = i;
		
		System.out.println("Recent Save: " + recentSave);
	}
	
	public void keyPressed(int key) {
		
		switch(key){
		case KeyEvent.VK_ESCAPE:	gsm.setState(GameStateManager.PAUSESTATE);
									break;
		case KeyEvent.VK_ENTER:		select();
									break;
		case KeyEvent.VK_UP:		currentChoice--;
									if(currentChoice == -1)
										currentChoice = choices.size()-1;
									break;
		case KeyEvent.VK_DOWN:		currentChoice++;
									if(currentChoice == choices.size())
										currentChoice = 0;
									break;
		}
	}	
	public void keyReleased(int key) {}
	public void saveGame(String s) {
		System.out.println("Saving " + s);
		System.out.println("Number of saves: " + (choices.size()-2));
		init();
		gsm.saveGame(s);
	}
	public void loadGame(String s) {}
}
