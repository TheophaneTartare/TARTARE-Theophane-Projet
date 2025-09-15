package zombicide.items;

import zombicide.Board;
import zombicide.Cell;
import zombicide.Item;
import zombicide.actors.Player;

public class InfradedScope extends Item {
	
	
	private Board board;
	/** 
	 * Create a InfradedScope
	 * */
	public InfradedScope(Board board){
		super();
		this.board=board;
	}
	
	/**
	 * @param Player the target
	 * */
	public void use(Player target){
		Cell playerCell=target.getCell();
		board.displayInfradedScope(playerCell);
	}
	
	/**
	 * create the another Item of the same type
	 */
	public Item createHomologue() {
		return new InfradedScope(board);
	}
	
	/** 
	  *  get the name of the Item
	  * @return String the name of the Item 
	  * 
	  * */
	public String toString(){
		return "Lunettes infrarouges" ;
	}
	

}
