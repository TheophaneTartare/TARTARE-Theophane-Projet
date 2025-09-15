package zombicide.items;

import zombicide.Cell;
import zombicide.Item;
import zombicide.actors.Player;

public class AidKit extends Item { 
	
	/** 
	 * Create a Aidkit
	 * */
	public AidKit () {
		super();
	}
	
	
	/** 
	 * heal the target of one pv 
	 * @param Player the target
	 * */
	public void use(Player target) {
		Cell targetCell = target.getCell() ; 
		Player holder = this.getHolder() ;
		Cell playerCell = holder.getCell();
		if (targetCell.equals(playerCell)) {
			target.addHeal(1);
			holder.minusAction();
			this.newHolder(holder);
		}
		else {
			System.out.println("la cible selectioner n'est pas valide");
		}
	}
	
	/**
	 * create the another Item of the same type
	 */
	public Item createHomologue() {
		return new AidKit();
	}
	
	/** 
	  *  get the name of the Item
	  * @return String the name of the Item 
	  * 
	  * */
	public String toString() {
		return "Trousse de secours" ;
	}

}
