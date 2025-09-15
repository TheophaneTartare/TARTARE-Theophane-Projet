package zombicide;

import java.util.ArrayList;
import java.util.List;

import zombicide.actors.Player;
import zombicide.actors.Zombie;
import zombicide.boards.RandomBoard;
import zombicide.boards.TrainingBoard;
import zombicide.players.Healer;
import zombicide.zombies.Walker;

public class Livrable1 {

	public static void main(String[] args) {
		if (args.length == 0 )  {
			Board board = new TrainingBoard();
			Game game = new Game(board);
			
			Player player = new Healer("healer");
			Zombie zombie = new Walker("walker");
			List<Player> allPlayers = new ArrayList<Player>() ;
			allPlayers.add(player);
			game.addPlayer(player,2,2);
			for (Player p : allPlayers){
				board.addPlayer(p,2,2);
			}
			game.addZombie(zombie,0,0);
			
			game.displayBoard();
		}
		
		else if (args.length == 1 ) {
			System.out.println(Integer.parseInt(args[0]));
			Player player = new Healer("healer");
			List<Player> allPlayers = new ArrayList<Player>() ;
			allPlayers.add(player);
			Board board = new RandomBoard(Integer.parseInt(args[0]),Integer.parseInt(args[0]));
			Game game = new Game(board);
			
			game.displayBoard();
			
		}
		
		
		else if (args.length == 2 ) {
			Player player = new Healer("healer");
			List<Player> allPlayers = new ArrayList<Player>() ;
			allPlayers.add(player);
			Board board = new RandomBoard(Integer.parseInt(args[0]),Integer.parseInt(args[0]));
			Game game = new Game(board);
			System.out.println("plateau aleatoire :");
			
			game.displayBoard();
			
			
			
			Board board2 = new TrainingBoard();
			Game game2 = new Game(board2);
			
			Player player2 = new Healer("healer");
			Zombie zombie2 = new Walker("walker");
			
			game2.addPlayer(player2,2,2);
			for (Player p : allPlayers){
				board2.addPlayer(p,2,2);
			}
			game2.addZombie(zombie2,0,0);
			
			System.out.println("plateau d'entrainement :");
			game2.displayBoard();
			
		}
	}

}
