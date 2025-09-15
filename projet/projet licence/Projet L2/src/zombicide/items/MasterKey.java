package zombicide.items;

import zombicide.Board;
import zombicide.Cell;
import zombicide.Direction;
import zombicide.Item;
import zombicide.actors.Player;
import zombicide.util.*;

public class MasterKey extends Item {
	
	Board board;
	boolean used;
	
	/** 
	 * Create a MasterKey
	 * */
	public MasterKey() {
		super();
		this.used = false;
		this.openDoor=true;
	}
	
	/** 
	 * ask in witch direction the player wants to open a door
	 * @param Player the target
	 * */ 
	public void use(Player target) {
		Cell cellPlayer = target.getCell();
		Direction direction =GetDirection.getDirection();
		while(cellPlayer.isDoorOpen(direction)){
			System.out.print("The door is also open . \n");
		 	direction =GetDirection.getDirection();
		}
		try{
			cellPlayer.openDoor(direction);
		}
		catch(NullPointerException e){
			System.out.print("There is no door in this direction . \n");
		}
	}    
	
	/**
	 * use a masterkey for open the door at the direction in parameter
	 * @param target the player
	 * @param direction the direction of the door what we will open
	 */
	public void realUse(Player target, Direction direction){
		Cell cellPlayer = target.getCell();
		cellPlayer.openDoor(direction);
		
	}
	
	/**
	 * create the another Item of the same type
	 */
	public Item createHomologue() {
		return new MasterKey();
	}
	/** 
	  *  get the name of the Item
	  * @return String the name of the Item 
	  * 
	  * */
	public String toString() {
		return "Passe-partout" ;
	}

}
