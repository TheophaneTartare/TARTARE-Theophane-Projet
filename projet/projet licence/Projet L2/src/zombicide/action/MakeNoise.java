package zombicide.action;

import zombicide.Action;
import zombicide.Actor;
import zombicide.Board;
import zombicide.Game;

public class MakeNoise implements Action{
	
	/**
	 * will do noise in the cell
	 * @param actor the Actor who launch the action 
	 * @param target the target of the action
	 * @param board the game board
	 * @param game the game
	 * @param random if the player is automatic 
	 */
	public void doAction(Actor actor,Actor target,Board board,Game game, boolean random) {
		actor.minusAction();
		actor.getCell().addNoise();
	}
	
	public String toString() {
		return "MakeNoise";
	
	}
}
