package zombicide.zombies;

import zombicide.actors.Zombie;

public class Tank extends Zombie {
	
	/** 
	 * create a Tank type Zombie
	 * @param name the name of the zombie 
	 * */
	public Tank(String name) {
		super(name);
		this.damage = 2;
		this.heal = 4;
	}
	
	/**
	 * take the damage with the " armor " 
	 */
	public void takeDamage(int dmg){
		if (dmg >1){
			this.heal -= dmg;
		}
	}
	
}
