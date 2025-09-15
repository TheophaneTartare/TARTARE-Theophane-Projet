package zombicide.action;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import zombicide.Action;
import zombicide.Actor;
import zombicide.Board;
import zombicide.Cell;
import zombicide.Game;
import zombicide.Item;
import zombicide.actors.Player;
import zombicide.actors.Zombie;
import zombicide.items.Weapon;
import zombicide.util.Input;

public class Attack implements Action{
	
	/**
	 * will attack
	 * @param actor the Actor who launch the action 
	 * @param target the target of the action
	 * @param board the game board
	 * @param game the game
	 * @param random if the player is automatic 
	 */
	public void doAction(Actor actor,Actor target,Board board,Game game,boolean random) {
		if(actor.toCharacter() !='Z' && random == false) {
			this.doActionPlayer((Player)actor);
		}
		
		else if (actor.toCharacter() !='Z' && random == true) {
			this.doActionPlayerRandom((Player) actor);
		}
		else {
			this.doActionZombie((Zombie)actor,(Player)target);
		}
	}
	
	/**
	 * the attack for the player
	 * @param player the player who launche the attack
	 */
	private void doActionPlayer(Player player) {
		Item onHand=player.getHand();
		Cell cell = player.getCell() ;
		if(cell.getCanAttack()) {
			if(onHand instanceof Weapon) {
				Zombie target=this.chooseTarget(player.getZombiesInRange(onHand.getRange()));
				if(target!=null) {
					if(onHand.isNoisy()){
						cell.addNoise();
					}
					
					((Weapon)onHand).attack2(target);
					player.minusAction();
					if(target.getHeal() <= 0 ) {
						player.addLevel(1);
					}
				}
				else {
					System.out.println("Attaque impossible ,Aucune cible \n");
				}
					
			}
				
			else{
				System.out.println("Attaque impossible ,l'objet à la main n'est pas une arme \n");
			}
		}
		
	}
	
	/**
	 * ask the player the zombie he want to attack 
	 * @param zombies all the zombie in the range of attack 
	 * @return the zobmie targeted 
	 */
	private Zombie chooseTarget(List<Zombie> zombies) {
		this.displayTarget(zombies);
		if(zombies.size()!=0) {
			try {
				int indiceChoisie=Input.readInt();
				Zombie zombie=zombies.get(indiceChoisie);
				return zombie;
			}
			catch(IOException e){
				System.out.print("\n give an int :");
				return this.chooseTarget(zombies);
			}
		}
		else {
			return null;
		}
		
		
	}
	
	/**
	 * the player attack randomly
	 * @param actor the player who attack
	 */
	private void doActionPlayerRandom(Player actor) {
		Item onHand=actor.getHand();
		Cell cell = actor.getCell() ;
		if(cell.getCanAttack()) {
			if(onHand instanceof Weapon) {
				List<Zombie> zombies = actor.getZombiesInRange(onHand.getRange()) ;
				if (!(zombies.isEmpty())) {
					Random r = new Random() ; 
					Zombie target= zombies.get(r.nextInt(zombies.size()));
					if(target!=null) {
						if(onHand.isNoisy()){
							cell.addNoise();
						}
						
						((Weapon)onHand).attack2(target);
						actor.minusAction();
						if(target.getHeal() <= 0 ) {
							actor.addLevel(1);
						}
					}
				}
				
				else {
					System.out.println("Attaque impossible ,Aucune cible \n");
				}
					
			}
				
			else{
				System.out.println("Attaque impossible ,l'objet à la main n'est pas une arme \n");
			}
		}
	}
	
	/**
	 * the attack of the zombie
	 * @param zombie the zombie who attack
	 * @param target the target of the zombie
	 */
	private void doActionZombie(Zombie zombie,Player target) {
		Cell cell = zombie.getCell() ;
		if(cell.getCanAttack()) {
			target.suppHeal(zombie.getDamage());
		}
		zombie.minusAction();
	}
	
	/**
	 * will display the zombie in range of the player for him to chose
	 * @param zombies all the zombies in range
	 */
	private void displayTarget(List<Zombie> zombies) {
		System.out.print("Liste des cibles : ");
		int cmpt=0;
		for (Zombie zombie : zombies) {
		    System.out.print(zombie.toString() +"(pv:"+zombie.getHeal()+") "+cmpt+" - ");
		    cmpt++;
		}
		System.out.print("\nChoose target :\n");
		
	}
	
	public String toString() {
		return "Attack";
	
	}
}
