package zombicide.action;

import java.util.List;

import zombicide.Action;
import zombicide.Actor;
import zombicide.Board;
import zombicide.Cell;
import zombicide.Direction;
import zombicide.Game;
import zombicide.actors.Player;
import zombicide.actors.Zombie;

public class LookAround implements Action{
	
	/**
	 * look around and give all the player,zombie and door open in the cell of the player
	 * @param actor the Actor who launch the action 
	 * @param target the target of the action
	 * @param board the game board
	 * @param game the game
	 * @param random if the player is automatic 
	 */
	public void doAction(Actor actor,Actor target,Board board,Game game , boolean Random) {
		
		Cell curentCell = actor.getCell();
		if (curentCell.toCharacter() == 'C') {
			System.out.println("le continentale vous empeche de voir") ;
		}
		else {
			List<Player> cellPlayer = curentCell.getPlayers() ; 
			List<Zombie> cellZombie = curentCell.getZombies() ;
			System.out.println("Les joueur sur la case sont : ");
			for ( int i = 0 ; i < cellPlayer.size(); i ++ ) {
				Player playerBis = cellPlayer.get(i) ;
				System.out.println(playerBis.getName());
			}
			
			System.out.println("Les zombie sur la case sont : ");
			for ( int i = 0 ; i < cellZombie.size(); i ++ ) {
				Zombie zombie = cellZombie.get(i) ;
				System.out.println(zombie.getName());
			}
			
			// il faut voir comment si les deux porte sont ouverte ou non
			System.out.println("Les porte ouverte sont : ");
			boolean cellB = curentCell.isDoorOpen(Direction.BOTTOM);
			boolean cellT = curentCell.isDoorOpen(Direction.TOP);
			boolean cellL = curentCell.isDoorOpen(Direction.LEFT);
			boolean cellR = curentCell.isDoorOpen(Direction.RIGHT);
			if (cellB){
				System.out.println("S");
			}
			if (cellT){
				System.out.println("N");
			}
			if (cellL){
				System.out.println("O");
			}
			if (cellR){
				System.out.println("E");
			}
		}
	}
	public String toString() {
		return "LookAround";
	
	}
}
