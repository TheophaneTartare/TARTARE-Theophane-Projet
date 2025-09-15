package zombicide;

import zombicide.actors.Player;

public abstract class Item {
	
	/* the player who owns this item */
	protected Player holder ;
	
	/* boolean to know if the item makes noise or not */
	protected boolean isNoisy;
	/*boolean to know if the item can open door*/
	protected boolean openDoor;
	/*boolean to know if the item is used */
	protected boolean used;
	/*boolean to know the range of this item*/
	protected int range;

	/** 
	 * create an Item
	 * @param isNoisy if the item make noise
	 * 
	 * */
	public Item() {
		this.holder = null;
		this.isNoisy = false;
		this.openDoor= false;
		this.range = 0;
	}
	
	/** 
	 * abstract method use the item on the target ( may necessarily be the holder ) 
	 * @param Player the target 
	 * */
	public abstract void use(Player target) ;
	
	public boolean isUsed() {
		return this.used;
	}
	/** 
	 * set the holder of the Item
	 * @param Player the new holder
	 * */
	public void newHolder(Player player) {
		this.holder  = player ;
	}
	
	/**
	 * say if the item can be used to open a door
	 * @return the boolean tha say of the item can a open a door
	 */
	public boolean canOpenDoor() {
		return this.openDoor;
	}
	
	/** 
	 * get the range of the wepon 
	 * @return int the range of the weapon 
	 * */
	public int getRange() {
		return this.range;
	}
	
	/** 
	 * remove the holder 
	 * */
	public void removeHolder() {
		this.holder = null ; 
	}
	
	/** 
	 * get the holder of the Item
	 * @return Player the holder, null if no holder 
	 * */
	public Player getHolder () {
		return this.holder ; 
	}
	
	/** 
	 * get if this item is noisy
	 * @return true if this element is noisy, otherwise false
	 * */
	public boolean isNoisy() {
		return this.isNoisy;
	}
	
	 /** 
	  * abstract methode get the name of the Item
	  * @return String the name of the Item 
	  * 
	  * */
	public abstract String toString() ; 
	
	/**
	 * give a character that is that is I for normal item and W for weapon
	 * @return
	 */
	public char toCharacter() {
		return 'I' ;
	}
	
	/**
	 * create a homologue of the item
	 * @return
	 */
	public abstract Item createHomologue() ;

}
