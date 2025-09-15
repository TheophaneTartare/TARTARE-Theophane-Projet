package zombicide.players;

import zombicide.actors.Player;

public class Nosy extends Player {
	
	
	/** 
	 * create a Nosy type Player
	 * @param name the name of the player 
	 * */
	public Nosy(String name) {
		super(name);
		
	}
	
	/**
	 * method who return a character different with each class 
	 * @return the character 
	 */
	public char toCharacter() {
		return 'N' ;
	}
	
}
