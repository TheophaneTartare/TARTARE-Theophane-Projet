package zombicide.zombies;

import zombicide.actors.Zombie;

public class Runner extends Zombie {
	
	/** 
	 * create a Runner type Zombie
	 * @param name the name of the zombie 
	 * */
	public Runner(String name) {
		super(name);
		this.heal = 1;
		this.action = 2 ;
	}
	
	/**
	 * reset the action with 2 action and not 1 
	 */
	public void resetAction() {
		super.resetAction();
		this.action = 2 ; 
	}
	
	
}
