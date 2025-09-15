package zombicide.action;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import zombicide.Action;
import zombicide.Actor;
import zombicide.Board;
import zombicide.Game;
import zombicide.Item;
import zombicide.actors.Player;
import zombicide.util.Input;

public class TakeOnHand implements Action{
	
	/**
	 * take one item on hand 
	 * @param actor the Actor who launch the action 
	 * @param target the target of the action
	 * @param board the game board
	 * @param game the game
	 * @param random if the player is automatic 
	 */
	public void doAction(Actor actor,Actor target,Board board,Game game , boolean random) {
		Player player=(Player)actor;
		player.minusAction();
		int index ;
		if (random == false) {
			index =this.getIndex(player);
		}
		else {
			index = this.getRandomIndex(player);
		}
		
		List<Item> inventory = player.getInventory();
		inventory.add(player.getHand()) ;
		
		player.SetHand(inventory.get(index));
		inventory.remove(index);
		/* Permet de supprimer les éléments du tableau si l'objet est null pour fludifier l'affichage */
		inventory.removeIf(Objects::isNull);
		
		player.setInventory(inventory);
	}
	
	/**
	 * ask the index of the player in parameter 
	 * @param player
	 * @return
	 */
	private int getIndex(Player player) {
		this.displatInventory(player);
		
		try {
		int indiceChoisie= Input.readInt() ;
		return indiceChoisie;
		}
		catch(IOException e){
			System.out.print("\n give an int :");
			return getIndex(player);
		}
	}
	
	private int getRandomIndex(Player player) {
		Random random=new Random();
		List<Item> inventory = player.getInventory() ;
		if (inventory.isEmpty()) {
			return 0 ;
		}
		else {
			int index=random.nextInt(inventory.size());
			System.out.print(index);
			return index;
		}
		
	}
	
	/**
	 * display the inventory 
	 * @param player the player who will display the inventory
	 */
	private void displatInventory(Player player) {
		System.out.print("Inventaire : ");
		int cmpt=0;
		for (Item item : player.getInventory()) {
		    System.out.print(item.getClass().getSimpleName() +" "+cmpt+" - ");
		    cmpt++;
		}
		System.out.print("\n Choose item :\n");
	}
	public String toString() {
		return "TakeOnHand";
	
	}
}
