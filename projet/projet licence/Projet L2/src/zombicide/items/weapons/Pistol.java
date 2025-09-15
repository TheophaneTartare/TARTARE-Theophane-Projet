package zombicide.items.weapons;

import zombicide.Item;
import zombicide.items.Weapon;

public class Pistol extends Weapon{

	public Pistol() {
		super();
		this.isNoisy = true;
		this.damage = 1;
		this.nbDice = 1;
		this.treshold = 4;
		this.range = 1;
		this.openDoor=true;
	}
	
	/**
	 * create the another Item of the same type
	 */
	public Item createHomologue() {
		return new Pistol();
	}
	
	/** 
	  *  get the name of the Item
	  * @return String the name of the Item 
	  * 
	  * */
	public String toString() {
		return "Pistolet" ;
	}
	
}
