package zombicide.actors;

import zombicide.Actor;

public class Zombie extends Actor{
	/*damage of the zombie*/
	protected int damage;
	/*weakness of the zombie*/
	protected boolean weak ;
	
	/**
     * Constructs a zombie with the specified name.
     *
     * @param name name of the zombie.
     */
	public Zombie(String name) {
		super(name) ; 
		this.damage = 1;
		this.heal = 1;
		this.action = 1;
	}
	
	/**
	 * get the damage the zombie will deal
	 * @return the damage
	 */
	public int getDamage() {
		return this.damage;
	}
	
	/** 
	 * refill the zombie action
	 * */
	public void resetAction() {
		this.action = 1 ;
	}
	
	/**
	 * take the damage
	 * @param dmg the damage
	 */
	public void takeDamage(int dmg){
		this.heal -= dmg;
	}
	
	/**
	 * give the zombie name
	 * @return the zombie name 
	 */
	public String toString() {
		return this.name;
	}
	
	/**
	 * Abstract method who return a character, can be different for each class
	 * @return the character 
	 */
	public char toCharacter() {
		return 'Z' ;
	}
}
