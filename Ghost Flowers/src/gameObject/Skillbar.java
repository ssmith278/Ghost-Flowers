package gameObject;

import java.awt.Color;
import java.awt.Graphics2D;

import attackObject.AttackObject;
import tileMap.TileMap;

public class Skillbar extends GameObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8576319424374424911L;
	private int numSkills;
	private AttackObject[] skillList;
	private double[] skillCDs, CDPercent;
	
	public Skillbar(TileMap tilemap, double x, double y, AttackObject... skillList) {
		super(tilemap);
		
		this.x = x;
		this.y = y;
		
		this.skillList = skillList;
		this.numSkills = skillList.length;
		this.width = 30;
		this.height = 30;
		
		skillCDs = new double[numSkills];
		CDPercent = new double[numSkills];
		
		for(int i = 0; i < numSkills; i++)
			skillCDs[i] = skillList[i].getCDTimer();
		
		for(int i = 0; i < numSkills; i++)
			CDPercent[i] = 100;
			
	}
	
	public void update(){
		for(int i = 0; i < numSkills; i++)
			skillCDs[i] = skillList[i].getCDTimer();
		
		for(int i = 0; i < numSkills; i++)
			CDPercent[i] = skillCDs[i]*100/skillList[i].getCooldown();
	}
	
	public void draw(Graphics2D g){
		for(int i = 0; i < numSkills; i++){
			g.setColor(Color.black);
			g.fillRect((int)(x+(i*(width+1))),(int)y,width,(int)(height - (height*CDPercent[i]/100)));
			g.setColor(Color.white);
			g.drawRect((int)(x+(i*(width+1))),(int)y,width,height);
		}
	}

}
