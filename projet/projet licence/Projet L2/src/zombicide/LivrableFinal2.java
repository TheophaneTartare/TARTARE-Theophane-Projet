package zombicide;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import zombicide.actors.Player;
import zombicide.actors.Zombie;
import zombicide.boards.RandomBoard;
import zombicide.boards.TrainingBoard;
import zombicide.players.Fighter;
import zombicide.players.Healer;
import zombicide.players.Lucky;
import zombicide.players.Nosy;
import zombicide.zombies.Abomination;
import zombicide.zombies.Runner;
import zombicide.zombies.Tank;
import zombicide.zombies.Walker;

public class LivrableFinal2 {
	
	public static void main(String[] args) throws IOException {
		//On creer les joueurs
		//Player player1 = new Fighter("Rambo");
		//Player player2 = new Healer("Suport");
		//Player player3 = new Lucky("Lechanceux");
		Player player4 = new Nosy("Lefêtar");
		
		
		if (args.length == 0 )  {
			Board board = new TrainingBoard(); //creation du plateau d'entrainement
			Game game = new Game(board); //creation de la partie
			
			//On ajoute les joueurs à la partie
			//game.addPlayer(player1,2,2);
			game.addPlayer(player4,2,2);
			
			game.play() ;
		}
		
		else if (args.length == 1 ) {
			Board board = new RandomBoard(Integer.parseInt(args[0]),Integer.parseInt(args[0]));
			Game game = new Game(board);
			
			//On ajoute les joueurs à la partie
			game.addPlayer(player4,board.getCrossWidth(),board.getCrossHeight());
			
			game.play() ;
			
		}
		
		
		else if (args.length == 2 ) {
			Board board = new RandomBoard(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
			Game game = new Game(board);
			
			
			//On ajoute les joueurs à la partie
			game.addPlayer(player4,board.getCrossWidth(),board.getCrossHeight());
			
			game.play() ;
		}
	}
}