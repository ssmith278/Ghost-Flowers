package gameObject;

import gameState.GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;

import main.GamePanel;
import tileMap.TileMap;

public class NPC extends GameObject implements Serializable{

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 5151871524602645378L;
	private ArrayList<Dialogue> dialogue;
	private Dialogue[] readLines;
	private String currentFile, name;
	
	private int currentLine, currentReadLine, delay;
	private boolean playDialogue, playedOnce, firstLine;
	
	public NPC(TileMap tilemap, String name, int delay){
		super(tilemap);
		
		this.name = name;
		currentLine = currentReadLine = 0;
		this.delay = delay;
		playDialogue = false;
		playedOnce = false;
	}
	
	public NPC(TileMap tilemap, String name, int x, int y, int delay) {
		super(tilemap);
		
		width = 40;
		height = 40;
		cwidth = 30;
		cheight = 30;
		
		this.name = name;
		this.x = x;
		this.y = y;
		this.delay = delay;
		currentLine = currentReadLine = 0;
		playDialogue = false;
		playedOnce = false;
	}
	
	public String getName(){
		return name;
	}
	
	public void setDelay(int delay){
		this.delay = delay;
	}
	
	//Reads NPC dialog from file
	public void populateDialogue(String s){
		playedOnce = false;
		dialogue = new ArrayList<Dialogue>();
		currentFile = s;
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(s));
			while(br.ready()){
				dialogue.add(new Dialogue(br.readLine(), delay));
			}
			br.close();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//Draw NPC sprite
	public void draw(Graphics2D g){
		setMapPosition();
		g.setColor(Color.RED);
		g.fillRect((int)(x+xmap) - width/2, (int)(y+ymap) - height/2, width, height);
		
	}
	
	public void triggerDialogue(){
		if(!playDialogue){
			readLines = new Dialogue[3];
			playDialogue = true;
			playedOnce = true;
			firstLine = true;
		}
	}
	
	public boolean isTriggered(){
		return playDialogue;
	}
	
	public String getCurrentFile(){
		return currentFile;
	}
	
	public boolean playedOnce(){
		return playedOnce;
	}
	
	public void setPlayedOnce(boolean b){
		playedOnce = b;
	}
	
	private void addToReadLines(String s, Graphics2D g){
		readLines[currentReadLine] = new Dialogue(s, GameState.getStringCenteredX(s, g), (GamePanel.HEIGHT/2), delay);
		currentReadLine++;
		currentReadLine%=3;
	}
	
	private int previousReadLine(){
		int x = currentReadLine-1;
		if(x < 0)
			x = 2;
		return x;
	}
	
	public void stopDialogue(){
		currentLine = currentReadLine = 0;
		playDialogue = false;
	}
	
	public void drawDialogue(Graphics2D g){
		
		if(!dialogue.isEmpty()){
			
			for(int i = 0; i < readLines.length; i++){
				if(readLines[i] != null){
					readLines[i].setY((readLines[i].getY()-((readLines[i].getY()/3)/(GamePanel.HEIGHT/3))));
					readLines[i].draw(g, new Font("Cambria", Font.PLAIN, GamePanel.WIDTH/25), new Color(0,0,0,(int) readLines[i].getFade()));					
					if(i != previousReadLine() && readLines[i].toRemove())
						readLines[i] = null;
					if(readLines[previousReadLine()].toRemove() && currentLine == dialogue.size()){
						stopDialogue();
					}
				}
			}
			
			if(firstLine){
				addToReadLines(dialogue.get(currentLine).getLine(), g);
				currentLine++;
				firstLine = false;
			}
			else if(readLines[previousReadLine()] != null && readLines[previousReadLine()].readyForNextLine() && currentLine < dialogue.size()){
				addToReadLines(dialogue.get(currentLine).getLine(), g);
				currentLine++;
			}			
			
		}
		
	}	

}
