package gameObject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.Timer;

public class Dialogue implements ActionListener, Serializable{

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 6195388843170771634L;
	private Timer fadeInTimer, fadeOutTimer, removalTimer;
	private boolean toRemove, nextLineReady, fadedIn, fadeOut;
	private String line;
	private double x, y, fade, removalDelay, fadeInDelay, fadeOutDelay;
	
	public Dialogue(String s, int delay){
		line = s;		
		removalDelay = delay;//s.length() * 250;
		//if(removalDelay < 1000) removalDelay = 1000;
		fadeInDelay = removalDelay/3;
		fadeOutDelay = removalDelay - fadeInDelay;
		x = y = fade = 0;
		toRemove = nextLineReady = false;
		fadeInTimer = new Timer((int) (fadeInDelay), this);
		fadeOutTimer = new Timer((int) (fadeOutDelay), this);
		removalTimer = new Timer((int) removalDelay, this);
	}
	
	public Dialogue(String s, int x, int y, int delay){
		line = s;		
		removalDelay = delay;//s.length() * 250;
		//if(removalDelay < 1000) removalDelay = 1000;
		fadeInDelay = removalDelay/3;
		fadeOutDelay = removalDelay - fadeInDelay;
		this.x = x;
		this.y = y;
		fade = 0;
		toRemove = nextLineReady = false;
		fadeInTimer = new Timer((int) (fadeInDelay), this);	
		fadeOutTimer = new Timer((int) (fadeOutDelay), this);
		removalTimer = new Timer((int) removalDelay, this);
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public double getX(){
		return x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public double getY(){
		return y;
	}
	
	public double getDelay(){
		return removalDelay;
	}
	
	public double getFadeDelay(){
		return fadeInDelay;
	}
	
	public String getLine(){
		return line;
	}
	
	public double getFade(){
		return fade;
	}
	
	public void setFade(double i){
		fade = i;
		if(fade >=255){
			fade = 255;
		}
		else if(fade < 0){
			fade = 0;
		}
	}
	
	public void flagForRemoval(){
		toRemove = true;
	}
	
	public boolean toRemove(){
		return toRemove;
	}
	
	public boolean readyForNextLine(){
		return nextLineReady;
	}
	
	public void draw(Graphics2D g, Font f, Color c){
		Font tempFont = g.getFont();
		Color tempColor = g.getColor();
		
		//Sets the fade value
		if(!fadeInTimer.isRunning())
			fadeInTimer.start();
		if(!fadeOutTimer.isRunning())
			fadeOutTimer.start();
		if(!fadedIn)
			setFade((fade+(Math.pow(255, 1.75)/fadeInDelay)));
		if(fadeOut)
			setFade((fade-(Math.pow(255, 1.65)/fadeInDelay)));
		
		//Draws the line
		g.setFont(f);
		g.setColor(c);
		g.drawString(line,(int)x, (int)y);
		
		//Starts the timer to signify removal
		if(!removalTimer.isRunning()){
			removalTimer.start();
		}
		
		//revert to original font and color
		g.setFont(tempFont);
		g.setColor(tempColor);
	}

	public void actionPerformed(ActionEvent e) {
				
		if(e.getSource() == fadeInTimer){
			nextLineReady = true;
			fadedIn = true;
			fadeInTimer.stop();
		}
		if(e.getSource() == fadeOutTimer){
			fadeOut = true;
			fadeOutTimer.stop();
		}				
		if(e.getSource() == removalTimer){
			flagForRemoval();
			fadeInTimer.stop();
			removalTimer.stop();
		}
	}
	
}
