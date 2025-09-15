package zombicide.action;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import zombicide.Action;
import zombicide.Actor;
import zombicide.Board;
import zombicide.Cell;
import zombicide.Game;
import zombicide.Item;
import zombicide.actors.Player;
import zombicide.util.Input;

public class Search implements Action{
	
	/**
	 * search for item on the floor in the player cell 
	 * if there are will ask if you want to take one
	 * @param actor the Actor who launch the action 
	 * @param target the target of the action
	 * @param board the game board
	 * @param game the game
	 * @param random if the player is automatic 
	 */
	public void doAction(Actor actor,Actor target,Board board,Game game , boolean random) {
		Player player=(Player)actor;
		if(player.toCharacter() =='N' && !(player.getHasSearch())) {
			player.trueHasSearch();
		}
		else {
			player.minusAction();
		}
		
		Cell curentCell = player.getCell() ;
		Boolean isThereItem = curentCell.displayFloor(); 
		
		if (isThereItem ) { 
			List<Item> floor = curentCell.getFloor() ;
			if (random == false) {
				this.whenItem(floor,player);
			}
			else {
				this.RandomPickItem(floor,player) ;
			}
			
		}
		else {
			System.out.print(" il n'y a pas d'objet \n") ;
		}
	}
	
	
	private void RandomPickItem(List<Item> floor , Player player) {
		Random r = new Random() ;
		List<Item> inventory = player.getInventory() ; 
		if (inventory.size() < 5) {
			inventory.add(floor.get(r.nextInt(floor.size()))) ;
			System.out.print(" le joueur a pris un item") ;
		}
	}
	
	/**
	 * ask if you want to take one of the item on the floor 
	 * @param floor the floor of the cell
	 * @param player the player who search
	 */
	private void whenItem(List<Item> floor , Player player) {
		boolean wantPick = this.askIfWantToPickItem() ;
		if (wantPick) {
			int IndexOfItemToPick = this.askWichItem(floor);
			this.take(player , floor , IndexOfItemToPick) ;
			
		}
		else {
			System.out.print("refus de prendre l'objet \n");
		}
		
	}
	
	/**
	 * ask if you want to take one of the item on the floor 
	 * @return
	 */
	private boolean askIfWantToPickItem() {
		System.out.print(" il y a au moins un objet à recuperer voulez vous le faire \n 0 : non 1 : oui \n ") ;
		int ouinon = this.askInt(0, 1) ;
		if(ouinon == 1) {
			return true ;
		}
		else {return false ; }
		
	}
	
	/**
	 * ask the player wich item he want 
	 * @param floor the floor 
	 * @return the index of the item the player whant
	 */
	private int askWichItem(List<Item> floor) {
		System.out.print("selectioner l'indice de l'item \n");
		return this.askInt(0, floor.size() -1  ) ;
	}
	
	/**
	 * take the item 
	 * @param player the player who take a item
	 * @param floor the floor of the cell
	 * @param index the index of the item to take 
	 */
	private void take( Player player , List<Item> floor , int index ) {
		List<Item> playerInventory = player.getInventory() ;
		Item itemToAdd = floor.get(index) ; 
		
		
 		if (playerInventory.size() < 5) {
 			player.addItemInventory(itemToAdd);
 			floor.remove(itemToAdd);
		}
		
		else {
			this.WhenInventoryfull(player, itemToAdd ,floor);
		}
	}
	
	/**
	 * if the inventory is full ask the player if he want to replace the item with one of his
	 * or leave the item to the floor
	 * @param player the player who take the item
	 * @param itemTOAdd the item to add 
	 * @param floor the floor 
	 */
	private void WhenInventoryfull(Player player , Item itemTOAdd,List<Item> floor) {
		System.out.print("pas de place dans l'inventaire. \n voulez vous faire un remplacement 0 : non , 1 : oui \n");
		boolean wantToPick = this.askIfWantToPickItem() ;
		if (wantToPick) {
			player.displayInventory(); 
			List<Item> inventory = player.getInventory() ;
			System.out.print("selectioner l'index de l'item a remplacer \n");
			int index = this.askInt(0, inventory.size());
			player.removeInventory(index);
			player.addItemInventory(itemTOAdd);
			floor.remove(itemTOAdd);
		}
		
		else {
			System.out.print("Item non pris \n ");
		}
	}
	
	
	/**
	 * ask a int between the born
	 * @param borninf the minimum
	 * @param bornsup the maximum
	 * @return the int given
	 */
	private int askInt(int borninf , int bornsup) {
		try {
			int indiceChoisie= Input.readInt() ;
			if(indiceChoisie >= borninf && indiceChoisie <= bornsup )  {
				return indiceChoisie ;
			}
			else {
				System.out.print("\n give an int valid :");
				return this.askInt(borninf,bornsup);
			}
			
			}
		
			catch(IOException e){
				System.out.print("\n give an int :");
				return this.askInt(borninf,bornsup);
			}
	}
	
	public String toString() {
		return "Search";
	
	}
}
