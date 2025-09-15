package zombicide.action;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import zombicide.Action;
import zombicide.Actor;
import zombicide.Board;
import zombicide.Cell;
import zombicide.Game;
import zombicide.actors.Player;
import zombicide.util.Input;


public class Heal implements Action {
	
	/**
	 * heal 1 pv the target
	 * @param actor the Actor who launch the action 
	 * @param target the target of the action
	 * @param board the game board
	 * @param game the game
	 * @param random if the player is automatic 
	 */
	public void doAction(Actor actor, Actor target, Board board,Game game,boolean random) {
		Cell cell = actor.getCell() ;
		Player playerToHeal ;
		if (random) {
			playerToHeal=chooseRandomTarget(cell);
		}
		else {
			playerToHeal=chooseTarget(cell);
		}
		
		if(playerToHeal!=null) {
			playerToHeal.addHeal(1);
		}
		else {
			System.out.println("Aucun joueur peut etre soignée \n");
		}
	}
	
	/**
	 * chose the target to heal randomly return null if not
	 * @param cell the cell of the healer
	 * @return the player who will be heal
	 */
	private Player chooseRandomTarget(Cell cell) {
		List<Player> listePlayers=cell.getPlayers();
		if(listePlayers.size()!=0) {
			Random r = new Random() ;
			return listePlayers.get(r.nextInt(listePlayers.size())) ;
		}
		
		else {
			return null ;
		}
	}
 	
	/**
	 * will ask the healer what player he want to heal
	 * @param cell the cell of the healer
	 * @return the player who will be heal
	 */ 
	private Player chooseTarget(Cell cell) {
		List<Player> listePlayers=cell.getPlayers();
		this.displayTarget(listePlayers);
		if(listePlayers.size()!=0) {
			try {
				int indiceChoisie=Input.readInt();
				Player player=listePlayers.get(indiceChoisie);
				return player;
			}
			catch(IOException e){
				System.out.print("\n give an int :");
				return this.chooseTarget(cell);
			}
		}
		else {
			return null;
		}
	}
	
	
	/**
	 * display the player in range to be heal
	 * @param listePlayers all the player in range to be heal
	 */
	private void displayTarget(List<Player> listePlayers) {
		System.out.print("Liste des joueurs pouvant etre soignée : ");
		int cmpt=0;
		for (Player player : listePlayers) {
		    System.out.print(player.toString() +"(pv:"+player.getHeal()+") "+cmpt+" - ");
		    cmpt++;
		}
		System.out.print("\n Choose target to heal :\n");
	}
	public String toString() {
		return "Heal";
	
	}
}
