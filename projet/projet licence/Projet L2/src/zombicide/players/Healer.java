package zombicide.players;

import zombicide.actors.Player;

public class Healer extends Player {
	
	/** 
	 * create a Healer type Player
	 * @param name the name of the player 
	 * */
	public Healer(String name) {
		super(name);
	}
	
	/**
	 * who return a character different with each class 
	 * @return the character 
	 */
	public char toCharacter() {
		return 'H' ;
	}

	
}
