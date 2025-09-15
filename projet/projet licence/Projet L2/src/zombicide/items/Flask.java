package zombicide.items;

import zombicide.Item;
import zombicide.actors.Player;

public class Flask extends Item {
	
	/** 
	 * Create a Flask
	 * */
	public Flask() {
		super();
		this.isNoisy = true;
	}
	
	/** 
	 * heal the holder of one pv
	 * @param Player the target
	 * */
	public void use(Player target) {
		this.holder.addHeal(1);
	}
	
	/** 
	  *  get the name of the Item
	  * @return String the name of the Item 
	  * 
	  * */
	public String toString() {
		return "Fiole de guérison" ;
	}
	
	/**
	 * create the another Item of the same type
	 */
	public Item createHomologue() {
		return new Flask();
	}
}
