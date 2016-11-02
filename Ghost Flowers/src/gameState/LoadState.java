package gameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import main.GamePanel;

public class LoadState extends GameState{
	
	private static ArrayList<String> choices;
	private static int recentSave = 0;
	private int currentChoice = 0;
	
	public LoadState(GameStateManager gsm){
		this.gsm = gsm;
		choices = new ArrayList<String>();
		init();
	}
	public void init() {
		choices.clear();
		choices.add("RECENT SAVE");
		scanFolder();
		choices.add("BACK");
	}	
	public void update() {}
	public void draw(Graphics2D g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);	
		
		g.setColor(Color.white);
		g.setFont(titleFont);
		drawStringToCenter("Load Game", GamePanel.HEIGHT/3, g);
		
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
		
		if(currentChoice == 0 && choices.size() > 2)
			loadGame(defaultSaveFolder + choices.get(recentSave));
		else if(currentChoice == 0 && choices.size() == 2)
			System.out.println("No recent save exists.");
		else if(currentChoice > 0 && currentChoice < choices.size()-1)
			loadGame(defaultSaveFolder + choices.get(currentChoice));
		else if(currentChoice == choices.size()-1)
			gsm.setState(gsm.lastState());	
			
		
	}
	
	private void scanFolder(){
		String filename = "";
		
		final File folder = new File(defaultSaveFolder);
		for(int i = 0; i < folder.listFiles().length; i++){
			filename = folder.listFiles()[i].getName();
			File currentFile = new File(filename);
			if(!currentFile.isDirectory() && filename.substring(filename.indexOf("."),filename.length()).equals(".save")){
				choices.add(filename);
			}
		}
		
		for(int i = 0; i < choices.size(); i++)
			if(choices.get(i).compareToIgnoreCase(choices.get(recentSave)) > 0)
				recentSave = i;
	}
	
	public void keyPressed(int key) {
		
		switch(key){
		case KeyEvent.VK_ESCAPE:	gsm.setState(gsm.lastState());
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
	public void saveGame(String s) {}
	public void loadGame(String s) {
		gsm.loadGame(s);
	}

}
