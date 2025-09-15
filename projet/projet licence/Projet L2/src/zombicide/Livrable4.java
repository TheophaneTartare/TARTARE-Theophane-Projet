package zombicide;

import java.io.IOException;
import java.util.Random;

import zombicide.actors.Player;
import zombicide.boards.RandomBoard;
import zombicide.boards.TrainingBoard;
import zombicide.players.Fighter;
import zombicide.players.Healer;
import zombicide.players.Lucky;
import zombicide.players.Nosy;

public class Livrable4 {
	
	/*print how to use the Liverable */
	public static void usage() {
	      System.out.println("Usage : java -classpath classes zombicide.Livrable4 <hauteur> <largeur> <nombre de joueurs>");
	      System.out.println("  où  <hauteur> correspond à la hauteur du plateau du jeu. (int)");
	      System.out.println("  où  <largeur> correspond à la largeur du plateau du jeu. (int)");
	      System.out.println("  où  <nombre de joueurs> correspond au nombre de joueurs de la partie. (int)");
	      System.exit(0);
	   }
	/**
	 * Creates a player with a random type.
	 *
	 * @param i The player number.
	 * @return player with a random type.
	 */
	public static Player createPlayer(int i) {
        Random random = new Random();
        int randomNumber = random.nextInt(4);

        switch (randomNumber) {
            case 0:
                return new Fighter("Fighter "+i);
            case 1:
                return new Healer("Healer "+i);
            case 2:
                return new Lucky("Lucky "+i);
            case 3:
                return new Nosy("Nosy "+i);
            default:
                return new Fighter("Fighter "+i);
        }
    }
	public static void main(String[] args) throws IOException {
		//Indication pour l'utilisation
		if (args.length <3 )  {
			Livrable4.usage();
		}
		Board board = new RandomBoard(Integer.parseInt(args[0]), Integer.parseInt(args[1])); //creation du plateau de jeu
		
		Game game = new Game(board); //creation de la partie
		
		//ajout des joueurs a la partie
		int nbPlayers = Integer.parseInt(args[2]);
		Player[] players = new Player[nbPlayers];
		
		//creation des joueurs avec un role aleatoire
		for (int i = 0; i < nbPlayers; i++) {
	        players[i] = createPlayer(i);
	    }
		
		// Ajoute les joueurs à la partie
        for (int i = 0; i < nbPlayers; i++) {
        	game.addPlayer(players[i],board.getCrossWidth(),board.getCrossHeight());
        }
		
		game.randomPlay(); 
	}
		
}