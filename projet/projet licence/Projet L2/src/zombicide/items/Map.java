package zombicide.items;

import zombicide.Board;
import zombicide.Item;
import zombicide.actors.Player;

public class Map extends Item {
	
	private Board board;
	
	/** 
	 * Create a Map
	 * */
	public Map(Board board) {
		super();
		this.board=board;
	}
	
	/** 
	 * show the board 
	 * @param Player the target 
	 * */
	public void use(Player target) {
		this.board.display();
	}
	
	/**
	 * create the another Item of the same type
	 */
	public Item createHomologue() {
		return new Map(board);
	}
	
	/** 
	  *  get the name of the Item
	  * @return String the name of the Item 
	  * 
	  * */
	public String toString() {
		return "Carte" ;
	}

}
