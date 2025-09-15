package zombicide.players;

import zombicide.actors.Player;
import java.util.Random;

public class Lucky extends Player {
	
	
	/** 
	 * create a Lucky type Player
	 * @param name the name of the player 
	 * */
	public Lucky(String name) {
		super(name);
	}
	
	/**
	 * return a int if the lucky as one more dice roll
	 * @return 1 if you have one more roll 0 if not 
	 */
	public int getADiceSup(){
		Random random = new Random();
		int randomNumber = random.nextInt(100) + 1;
		if (randomNumber > 50){
			return 1;
		}
		return 0;
	}
	
	/**
	 * who return a character different with each class 
	 * @return the character 
	 */
	public char toCharacter() {
		return 'L' ;
	}
	
}
