package gameObject;

import java.io.Serializable;
import java.util.ArrayList;

public class NPCList implements Serializable{
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 2302786584526798705L;
	private ArrayList<NPC> npcs;
	
	public NPCList(){
		npcs = new ArrayList<NPC>();
	}
	
	public NPC getNPC(int i){
		return npcs.get(i);
	}
	
	public NPC getNPC(String s){
		return npcs.get(NPCIndex(s));
	}
	
	public void addNPC(NPC npc){		
		Boolean unique = true;
		
		for(int i = 0; i < npcs.size(); i++)
			if (npcs.get(i).getName().compareTo(npc.getName()) == 0)
				unique = false;
		
		if(unique)
			npcs.add(npc);
		else
			System.out.println("NPC Addition Failed. Please input a unique name.");
	}
	
	public void removeNPC(int i){
		npcs.remove(i);
	}
	
	public int size(){
		return npcs.size();
	}
	
	public boolean isEmpty(){
		return npcs.isEmpty();
	}
		
	public int NPCIndex(String s){
		for(int i = 0; i < npcs.size(); i++)
			if (npcs.get(i).getName().compareTo(s) == 0){
				return i;
			}
		
		return -1;
	}
	
	public void stopAllDialogue(){
		for(int i = 0; i < npcs.size(); i++)
			npcs.get(i).stopDialogue();
	}
	
	public String getDialogueStates(){
		String s = "";
		
		for(int i = 0; i < npcs.size(); i++)
			if(i != npcs.size()-1){
				s += npcs.get(i).getCurrentFile() + System.lineSeparator();
				s += npcs.get(i).playedOnce() + System.lineSeparator();
			}
			else{
				s += npcs.get(i).getCurrentFile() + System.lineSeparator();
				s += npcs.get(i).playedOnce();
			}
		
		return s;
			
	}

}
