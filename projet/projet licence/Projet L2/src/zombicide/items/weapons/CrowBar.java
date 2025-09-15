package zombicide.items.weapons;

import zombicide.Item;
import zombicide.items.Weapon;

public class CrowBar extends Weapon {

	public CrowBar() {
		super();
		this.damage = 1;
		this.nbDice = 1;
		this.treshold = 4;
		this.range = 0;
		this.openDoor=true;
	}
	
	/**
	 * create the another Item of the same type
	 */
	public Item createHomologue() {
		return new CrowBar();
	}
	
	/** 
	  *  get the name of the Item
	  * @return String the name of the Item 
	  * 
	  * */
	public String toString() {
		return "Pied de biche" ;
	}
	
}
