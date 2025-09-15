package zombicide.players;

import zombicide.actors.Player;

public class Fighter extends Player {
	
	/** 
	 * create a Fighter type Player
	 * @param name the name of the player 
	 * */
	public Fighter(String name) {
		super(name);
	}
	
	/**
	 * who return a character different with each class 
	 * @return the character 
	 */
	public char toCharacter() {
		return 'F' ;
	}
	
}
