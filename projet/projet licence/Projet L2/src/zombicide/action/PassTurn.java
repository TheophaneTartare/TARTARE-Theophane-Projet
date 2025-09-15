package zombicide.action;
import zombicide.Action;
import zombicide.Actor;
import zombicide.Board;
import zombicide.Game;

public class PassTurn implements Action {

	/**
	 * will do nothing 
	 * @param actor the Actor who launch the action 
	 * @param target the target of the action
	 * @param board the game board
	 * @param game the game
	 * @param random if the player is automatic 
	 */
	public void doAction(Actor actor, Actor target, Board board,Game game, boolean random) {
		System.out.print("tour passer \n") ;
		actor.minusAction();
	}
	
	public String toString() {
		return "PassTurn";
	
	}
}
