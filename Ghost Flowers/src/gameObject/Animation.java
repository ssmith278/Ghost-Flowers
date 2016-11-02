package gameObject;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Animation implements Serializable{

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -6361906402704913272L;
	transient private BufferedImage[] frames;
	private int currentFrame;
	
	private long startTime, delay;
	
	private boolean playedOnce, oneTime;
	
	public Animation(){
		playedOnce = false;
	}
	
	public Animation(boolean b){
		playedOnce = oneTime = false;
	}
	
	public void setFrames(BufferedImage[] frames){
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce = false;
	}
	
	public void playOneTime(boolean b){oneTime = b;}
	public void setDelay(long d){ delay = d; }
	public void setCurrentFrame(int i){ currentFrame = i; }
	public void update(){
		if (delay == -1) return;
		
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if (elapsed > delay) {
			currentFrame++;
			startTime = System.nanoTime();
		}
		
		if (currentFrame == frames.length) {
			playedOnce = true;
			if (oneTime && playedOnce)
				currentFrame = frames.length-1;
			else
				currentFrame = 0;
		}
	}
	
	public int getFrame() { return currentFrame; }
	public BufferedImage getImage() { return frames[currentFrame]; }
	public boolean hasPlayedOnce() { return playedOnce; }
	
}
