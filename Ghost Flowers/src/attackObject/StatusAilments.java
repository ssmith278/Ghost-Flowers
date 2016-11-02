package attackObject;

import java.io.Serializable;

import playerObject.Player;

public class StatusAilments implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1190373183753519731L;
	//0-3 Player status ailments
	public final static int SLOW = 0;
	public final static int FREEZE = 1;
	public final static int KNOCKUP = 2;
	public final static int KNOCKBACK = 3;
	public final static int BURN = 4;
	public final static int COMBO_MARK = 5;
	//5+ enemy only status ailments
	
	private final double BURN_DELAY = 2;
	private final double BURN_DURATION = 7;
	private final double SLOW_DURATION = 3;
	private final double FROZEN_DURATION = 2;
	private final double CMARK_DURATION = 5;
	private final int MAX_CMARK = 3;
	private final double KNOCKBACK_DURATION = 0.5;
	
	private int burnDMG, cmDMG, cmCount;
	private long btStartTime, bdStartTime, sStartTime, cmStartTime, fStartTime, kbStartTime;
	private double maxSpeed, initialKnockUp, initialKnockBack;
	private boolean slowed, frozen, knockedUp, knockedBack, burned, marked;
	
	private Player player;
	
	public StatusAilments(Player p){
		player = p;			
		slowed = frozen = knockedUp = knockedBack = burned = marked = false;
		cmCount = 0;
	}
	
	public void setAilments(int... x){
		
		for(int element : x){
			switch(element){
				case SLOW:			slowed = true;
									System.out.println("Slowed.");
									sStartTime = System.nanoTime();
									break;
				case FREEZE:		frozen = true;
									System.out.println("Frozen.");
									fStartTime = System.nanoTime();
									break;
				case KNOCKUP:		knockedUp = true;
									break;
				case KNOCKBACK:		knockedBack = true;
									kbStartTime = System.nanoTime();
									break;
				case BURN:			burned = true;
									btStartTime = System.nanoTime();
									bdStartTime = System.nanoTime();
									break;
				case COMBO_MARK:	cmCount++;
									marked = true;
									System.out.println("Player combo mark: " + cmCount);
									if(cmCount >= MAX_CMARK){
										player.setHealth(player.getHealth()-cmDMG);
										cmCount = 0;
										marked = false;
										System.out.println("Mark activated");
									}
									cmStartTime = System.nanoTime();
									break;
			}
			
		}
	}
	
	public void removeSlow(){
		slowed = false;
	}
	public void removeFreeze(){
		frozen = false;
	}
	public void removeBurn(){
		burned = false;
	}
	public void removeMark(){
		cmCount = 0;
		marked = false;
	}
	public boolean isSlowed(){
		return slowed;
	}
	public boolean isFrozen(){
		return frozen;
	}
	public boolean isKnockedUp(){
		return knockedUp;
	}
	public boolean isKnockedBack(){
		return KNOCKBACK_DURATION > (System.nanoTime()-kbStartTime)/1E9;
	}
	public boolean isBurned(){
		return burned;
	}
	public boolean isMarked(){
		return marked;
	}
	
	public void setBurnTick(int bt){
		burnDMG = bt;
	}
	public void setCMDMG(int cmd){
		cmDMG = cmd;
	}
	public int getCMCount(){
		return cmCount;
	}
	public void setMaximumSpeed(double ms){
		maxSpeed = ms;
	}
	public void setInitialKnockUp(int ku){
		initialKnockUp = ku;
	}
	public void setInitialKnockBack(int kb){
		initialKnockBack = kb;
	}
	
	
	public void update(){
		if(slowed && (System.nanoTime() - sStartTime)/1E9 < SLOW_DURATION){
			if(player.getDx() > maxSpeed)
				player.setVector(maxSpeed, player.getDy());
			else if(player.getDx() < -maxSpeed)
				player.setVector(-maxSpeed, player.getDy());
			
//			System.out.println("Player is slowed for " + (int)(SLOW_DURATION - (System.nanoTime() - sStartTime)/1E9) + " more seconds.");
		}
		else{
			slowed = false;
		}
		
		if(frozen && (System.nanoTime() - fStartTime)/1E9 < FROZEN_DURATION){
			player.setVector(0, player.getDy());
		}
		else
			frozen = false;
		
		if(knockedUp){
			player.setVector(player.getDx(), initialKnockUp);
			knockedUp = false;
			System.out.println("Knocked Up.");
		}
		if(knockedBack){
			player.setVector(initialKnockBack, player.getDy());
			knockedBack = false;
			kbStartTime = System.nanoTime();
			System.out.println("Knocked Back.");
		}
		
		if(burned && (System.nanoTime() - bdStartTime)/1E9 > BURN_DURATION){
			burned = false;
		}
		
		if(burned && (System.nanoTime() - btStartTime)/1E9 > BURN_DELAY){
			player.setHealth(player.getHealth() - burnDMG);
			System.out.println("Burned for " + burnDMG);
			btStartTime = System.nanoTime();
		}
		
		if(marked && ((System.nanoTime() - cmStartTime)/1E9) > CMARK_DURATION){
			cmCount = 0;
			marked = false;
			System.out.println("Mark expired");
		}
	}

}
