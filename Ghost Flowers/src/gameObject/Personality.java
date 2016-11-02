package gameObject;

import java.io.Serializable;

import playerObject.PlayerManager;


public class Personality implements Serializable{

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -1025998827481673213L;
	private PlayerManager player;
	public static final int FIRE = 0;
	public static final int AIR = 1;
	public static final int WATER = 2;
	public static final int EARTH = 3;
	
	public static final int AGGRESSION = 0;
	public static final int SWIFTNESS = 1;
	public static final int MALEVOLENCE = 2;
	public static final int SUBTLETY = 3;
	public static final int COMMANDING = 4;
	
	private int firestat, airstat, waterstat, earthstat;
	private int aggressionStat, swiftnessStat, malevolenceStat, subtletyStat, commandingStat;
	
	public Personality(PlayerManager player){
		
		this.player = player;

		firestat = airstat = waterstat = earthstat = 0;
		aggressionStat = swiftnessStat = malevolenceStat = subtletyStat = commandingStat = 0;
		
	}
	
	public void calculateElement(){
		
		int[] temp = {firestat, airstat, waterstat, earthstat};
		int currentHigh = 0;
		for(int i = 1; i < temp.length; i++)
			if(temp[i] > temp[currentHigh])
				currentHigh = i;
				
		player.setElement(currentHigh);
		System.out.println(toString());
		System.out.println("Current Element: " + player.getElement());
	}
	
	private void checkForTies(int lastAddition, int lastValue){
		
		int[] temp = {firestat, airstat, waterstat, earthstat};
		int currentHigh = 0;
		for(int i = 1; i < temp.length; i++)
			if(temp[i] > temp[currentHigh])
				currentHigh = i;
			else if(temp[i] == temp[currentHigh]){
				System.out.println("Tiebreaker.");
				switch(lastAddition){
					case AGGRESSION:	addAggression(lastValue);
										break;
					case SWIFTNESS:		addSwiftness(lastValue);
										break;
					case MALEVOLENCE:	addMalevolence(lastValue);
										break;
					case SUBTLETY:		addSubtlety(lastValue);
										break;
					case COMMANDING:	addCommanding(lastValue);
										break;
				}
			}
	}

	public int getAggression() { return aggressionStat; }
	public void addAggression(int aggression) {
		this.aggressionStat += aggression;
			firestat += 2*aggression;
			airstat += 1*aggression;
			earthstat += -1*aggression;
			waterstat += -2*aggression;

		checkForTies(AGGRESSION, aggression);
		calculateElement();
	}
	public int getSwiftness() { return swiftnessStat; }
	public void addSwiftness(int swiftness) {
		this.swiftnessStat += swiftness;
			firestat += 1*swiftness;
			airstat += 2*swiftness;
			earthstat += -2*swiftness;
			waterstat += -1*swiftness;

		checkForTies(SWIFTNESS, swiftness);
		calculateElement();
	}
	public int getMalevolence() { return malevolenceStat; }
	public void addMalevolence(int malevolence) {
		this.malevolenceStat += malevolence;
			firestat += 2*malevolence;
			airstat += -1*malevolence;
			earthstat += 1*malevolence;
			waterstat += -2*malevolence;	

		checkForTies(MALEVOLENCE, malevolence);
		calculateElement();
	}
	public int getSubtlety() { return subtletyStat; }
	public void addSubtlety(int subtlety) {
		this.subtletyStat += subtlety;
			firestat += -2*subtlety;
			airstat += 2*subtlety;
			earthstat += -1*subtlety;
			waterstat += 1*subtlety;	
			
		checkForTies(SUBTLETY, subtlety);
		calculateElement();
	}
	public int getCommanding() { return commandingStat; }
	public void addCommanding(int commanding) {
		this.commandingStat += commanding;
			firestat += 2*commanding;
			airstat += 1*commanding;
			earthstat += -2*commanding;
			waterstat += -1*commanding;
			
		checkForTies(COMMANDING, commanding);
		calculateElement();
	}
	
	public String toString(){
		return 	"Aggression: " + aggressionStat +
				" Swiftness: " + swiftnessStat + 
				" Malevolence: " + malevolenceStat + 
				" Subtlety: " + subtletyStat + 
				" Commanding: " + commandingStat +
				"\n" +
				"Fire: " + firestat +
				" Air: " + airstat +
				" Water: " + waterstat +
				" Earth: " + earthstat
				;
	}
	
	
}
