package zombicide.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import zombicide.Action;
import zombicide.Actor;
import zombicide.Board;
import zombicide.Cell;
import zombicide.Direction;
import zombicide.Game;
import zombicide.actors.Player;
import zombicide.actors.Zombie;
import zombicide.util.GetDirection;

public class Move implements Action{
	
	
	/**
	 * will move
	 * @param actor the Actor who launch the action 
	 * @param target the target of the action
	 * @param board the game board
	 * @param game the game
	 * @param random if the player is automatic 
	 */
	public void doAction(Actor actor,Actor target,Board board,Game game, boolean random) {	
		if(actor.toCharacter() !='Z' && random == false) {
			this.doActionPlayer((Player)actor,board);
		}
		else if(actor.toCharacter() !='Z' && random == true) {
			this.doActionPlayerRandom((Player)actor,board) ;
		}
		else {
			this.doActionZombie((Zombie)actor,board);
		}
	}
	
	/**
	 * the action move for the player 
	 * @param player the player who will move
	 * @param board the board
	 */
	private void doActionPlayer(Player player,Board board) {
		player.minusAction();
		Direction direction=GetDirection.getDirection();
		board.move(player,direction);
	}
	
	/**
	 * move randomly
	 * @param player the player who will move 
	 * @param board the board
	 */
	private void doActionPlayerRandom(Player player,Board board) {
		player.minusAction();
		List<Direction> directionList = new ArrayList<Direction>() ;
		directionList.add(Direction.TOP);
		directionList.add(Direction.BOTTOM);
		directionList.add(Direction.RIGHT);
		directionList.add(Direction.LEFT);
		Random r = new Random() ;
		board.move(player,directionList.get(r.nextInt(directionList.size())));
	}
	
	/**
	 * the action move for the zombie 
	 * @param zombie the zombie who will move
	 * @param board the board
	 */
	private void doActionZombie(Zombie zombie,Board board) {
		zombie.minusAction();
		Cell noisyCell=board.getMoreNoisyCell();
		if((noisyCell.getNoise()!=0)) {
			
			int widthCell=noisyCell.getwidth();
			int heightCell=noisyCell.getheight();
			boolean moveUP=zombie.getWidthPosition()>widthCell;
			boolean moveDown=zombie.getWidthPosition()<widthCell;
			boolean moveRight=zombie.getHeightPosition()<heightCell;
			boolean moveLeft=zombie.getHeightPosition()>heightCell;
			if(moveUP) {
				board.move(zombie, Direction.TOP);
			}
			else if(moveDown){
				board.move(zombie, Direction.BOTTOM);
			}
			else if(moveRight) {
				board.move(zombie, Direction.RIGHT);
			}
			else if(moveLeft){
				board.move(zombie, Direction.LEFT);
			}
		}
	}
		
	public String toString() {
		return "Move";
	
	}
	
		

}
