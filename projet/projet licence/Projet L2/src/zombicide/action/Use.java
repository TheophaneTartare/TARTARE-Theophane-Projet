package zombicide.action;

import zombicide.Action;
import zombicide.Actor;
import zombicide.Board;
import zombicide.Game;
import zombicide.Item;
import zombicide.actors.Player;

public class Use implements Action{
	
	/**
	 * will use the item
	 * @param actor the Actor who launch the action 
	 * @param target the target of the action
	 * @param board the game board
	 * @param game the game
	 * @param random if the player is automatic 
	 */
	public void doAction(Actor actor,Actor target,Board board,Game game,boolean random) {
		Player player=(Player)actor;
		player.minusAction();
		Item hand = player.getHand() ;
		if(hand != null) {
			hand.use(player);
			if(player.getHand().isNoisy()){
				player.getCell().addNoise();
			}
			if (hand.toCharacter() != 'W') {
				player.supHand();
			}
		}
		else {
			System.out.print("pas d'item dans la main \n ");
		}
		
		
	}
	public String toString() {
		return "Use";
	
	}
}

