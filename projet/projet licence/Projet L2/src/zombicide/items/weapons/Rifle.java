package zombicide.items.weapons;

import zombicide.Item;
import zombicide.items.Weapon;

public class Rifle extends Weapon{

	public Rifle() {
		super();
		this.isNoisy = true;
		this.damage = 1;
		this.nbDice = 2;
		this.treshold = 4;
		this.range = 3;
	}
	
	/**
	 * create the another Item of the same type
	 */
	public Item createHomologue() {
		return new Rifle();
	}
	
	/** 
	  *  get the name of the Item
	  * @return String the name of the Item 
	  * 
	  * */
	public String toString() {
		return "Carabine" ;
	}
	
}
