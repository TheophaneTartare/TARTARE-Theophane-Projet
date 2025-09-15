package zombicide.items;

import zombicide.Item;
import zombicide.actors.Player;
import zombicide.actors.Zombie;
import zombicide.players.Lucky;

public abstract class Weapon extends Item {
	
	public int damage;
	public int nbDice;
	public int treshold;
	
	/** 
	 * Create a Weapon
	 * */
	public Weapon() {
		super();
		this.damage = 0;
		this.nbDice = 0;
		this.treshold = 0;
		
	}
	
	/** 
	 * ask the player where he wants to attack and lauch the attack 
	 * @param Player the target 
	 * */
	public void use(Player target) {
		
	}
	
	/** 
	 * get the treshold of the wepon 
	 * @return int the treshold 
	 * */ 
	public int getTtreshold() {
		return this.treshold;
	}
	
	
	
	/** 
	 * throw a dice of 6 face and return the result 
	 * @return int the result of the throw
	 * */
	public int throwDice() {
		int dice = (int)(Math.floor(Math.random() * 6) + 1);  
		return dice;
	}
	
	/**
	 * return a character that says that the item is a weapon
	 */
	public char toCharacter() {
		return 'W' ;
	}

	/**
	 * simulates the chances of success of a player's attacks
	 * @param nbrDice the number of dice of the player
	 * @param isFighter if the player is a fighter or not
	 * @return if the attack work or not
	 */
	private boolean canAttack(int nbrDice, boolean isFighter){
		for (int i=0 ; i< nbrDice ; i++){
			int x = this.throwDice() ;
			if (isFighter) {
				x += 1 ;
			}
			if (x >= this.treshold){
				return true ;
			}
		}
		return false;
	}

	/**
	 * if the player succeeded in his attack or not
	 * @return if the attack work or not
	 */
	public boolean attack(){
	    boolean res;
	    boolean isFighter = false ;
	    Player player = this.holder;
	    
	    if (player.toCharacter() == 'F'){
	        isFighter = true ;
	    }
	    
	    if (player.toCharacter() == 'L'){
	    	Lucky luckyPlayer = (Lucky) player;
	        int aDiceSup = luckyPlayer.getADiceSup();
	        res = this.canAttack(this.nbDice + aDiceSup, isFighter);
	    }
	    else {
	        res = this.canAttack(this.nbDice, isFighter);
	    }
	    
	    System.out.print(res +"\n");
	    return res;
	}
	
	/**
	 * attack the zombie if attack() has return true 
	 * @param zombie the zombie who will be attacked  
	 */
	public void attack2(Zombie zombie){
		if(this.attack()){
			zombie.takeDamage(this.damage);
		}
	}

}