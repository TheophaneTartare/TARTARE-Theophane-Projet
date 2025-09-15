package zombicide.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import zombicide.Action;
import zombicide.Actor;
import zombicide.Board;
import zombicide.util.GetDirection;
import zombicide.zombies.Abomination;
import zombicide.zombies.Tank;
import zombicide.Cell;
import zombicide.Direction;
import zombicide.Door;
import zombicide.Game;
import zombicide.Item;
import zombicide.actors.Player;
import zombicide.actors.Zombie;

public class OpenDoor implements Action{
	
	/**
	 * open a door
	 * @param actor the Actor who launch the action 
	 * @param target the target of the action
	 * @param board the game board
	 * @param game the game
	 * @param random if the player is automatic 
	 */
	public void doAction(Actor actor,Actor target,Board board,Game game,boolean random) {
		Player player=(Player)actor;
		Item itemOnHand=player.getHand();
		if(itemOnHand.canOpenDoor()) {
			Direction direction ;
			if(random == false) {
				direction = GetDirection.getDirection();
			}
			else {
				direction = this.getRandomDirection() ;
			}
			
			Cell playerCell=player.getCell();
			if (!player.getHand().isUsed()) {
				try {
				HashMap<Direction, Door> listDoors = playerCell.getDoors() ;
				if(listDoors.get(direction) != null) {
					playerCell.openDoor(direction);
					player.minusAction();
					if(itemOnHand.isNoisy()) {
						playerCell.addNoise();
					}
					this.addZombie(board,game);
				}
				else {
					System.out.print("There is no door in this direction . \n");
				}
				
				}
				catch(NullPointerException e){
					System.out.print("There is no door in this direction . \n");
					if(random == false) {
						this.doAction(player,target, board,game,random);
					}
					
				}
			}
		}		
	}
	
	/**
	 * give a radom direction 
	 * @return the direction 
	 */
	private Direction getRandomDirection() {
		List<Direction> directionList = new ArrayList<Direction>() ;
		directionList.add(Direction.TOP);
		directionList.add(Direction.BOTTOM);
		directionList.add(Direction.RIGHT);
		directionList.add(Direction.LEFT);
		Random r = new Random() ;
		return directionList.get(r.nextInt(directionList.size())) ;
	}
	
	/**
	 * will add zombies with each door open
	 * @param board the board
	 * @param game the game 
	 */
	private void addZombie(Board board,Game game) {
		 Random random = new Random();
		 int nbZombie = random.nextInt(3) + 1;//choisie aleatoire entre 1 et 3
		 int nbSpecialZombie=random.nextInt(3) ;
		 
	     List<Cell> sewers = board.getSewers();
	        
	     //apparition des zombies
	     while (nbZombie > 0) {
	            Cell randomSewer = sewers.get(random.nextInt(sewers.size()));
	            Zombie zombie = new Zombie("Zombie");
	            game.addZombie(zombie, randomSewer.getwidth(),randomSewer.getheight());
	            nbZombie--;
	     }
	     //apparition des abomination ou balaise
	     if(nbSpecialZombie==0) {
	    	 Cell randomSewer = sewers.get(random.nextInt(sewers.size()));
	    	 Tank tank=new Tank("Tank");
	    	 game.addZombie(tank, randomSewer.getwidth(),randomSewer.getheight());
	     }
	     else if(nbSpecialZombie==1) {
	    	 Cell randomSewer = sewers.get(random.nextInt(sewers.size()));
	    	 Abomination abomination=new Abomination("Abomination");
	    	 game.addZombie(abomination, randomSewer.getwidth(),randomSewer.getheight());
	     }
		 
	}
			
	public String toString() {
		return "OpenDoor";
	
	}

}
