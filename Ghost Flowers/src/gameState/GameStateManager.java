package gameState;
import java.awt.Graphics2D;
import java.util.ArrayList;

import playState.Level1State;
import playState.Level2State;

public class GameStateManager {

	private ArrayList<GameState> gameStates;
	private int currentState, lastState;
	
	public static final int MENUSTATE = 0;
	public static final int PAUSESTATE = 1;
	public static final int ENDGAMESTATE = 2;
	public static final int SAVESTATE = 3;
	public static final int LOADSTATE = 4;
	public static final int LEVEL1STATE = 5;
	public static final int LEVEL2STATE = 6;
	//future gameStates here
	
	public GameStateManager(){
		
		gameStates = new ArrayList<GameState>();
		
		currentState = MENUSTATE;
		gameStates.add(new MenuState(this));
		gameStates.add(new PauseState(this));
		gameStates.add(new EndGameState(this));
		gameStates.add(new SaveState(this));
		gameStates.add(new LoadState(this));
		gameStates.add(new Level1State(this));
		gameStates.add(new Level2State(this));
		//add future gameStates
		
	}
	
	public void setState(int state){
		lastState = currentState;
		currentState = state;
	}
	
	public void setNewState(int state){
		lastState = currentState;
		currentState = state;
		
		switch(state){
		
		case MENUSTATE:		gameStates.set(state, new MenuState(this));
							break;
		case PAUSESTATE:	gameStates.set(state, new PauseState(this));
							break;
		case ENDGAMESTATE:	gameStates.set(state, new EndGameState(this));
							break;
		case SAVESTATE:		gameStates.set(state, new SaveState(this));
							break;
		case LOADSTATE:		gameStates.set(state, new LoadState(this));
							break;
		case LEVEL1STATE:	gameStates.set(state, new Level1State(this));
							break;
		case LEVEL2STATE:	gameStates.set(state, new Level2State(this));
		
		}
	}
	
	public GameState getState(int state){
		
		if(state < gameStates.size() && state >= 0)
			return gameStates.get(state);
		
		return null;
	}
	
	public int lastState(){
		return lastState;
	}
	
	public void init(){ gameStates.get(currentState).init(); }
	public void update(){ gameStates.get(currentState).update(); }
	public void saveGame(String s){ 
		if(currentState == SAVESTATE){
			setState(lastState);
			gameStates.get(LEVEL1STATE).saveGame(s);
		}
		else
			setNewState(SAVESTATE);
	}
	public void loadGame(String s){
		if(currentState == LOADSTATE){
			currentState = LEVEL1STATE;
			gameStates.set(LEVEL1STATE, new Level1State(this, s));
		}
		else
			setNewState(LOADSTATE);			
	}
	public void draw(Graphics2D g){	gameStates.get(currentState).draw(g); }	
	public void keyPressed(int k){ gameStates.get(currentState).keyPressed(k); }	
	public void keyReleased(int k){	gameStates.get(currentState).keyReleased(k); }
	
}
