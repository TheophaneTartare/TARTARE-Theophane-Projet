package zombicide;

import java.util.ArrayList;
import java.util.List;

import zombicide.actors.Player;
import zombicide.actors.Zombie;
import zombicide.boards.TrainingBoard;
import zombicide.items.Flask;
import zombicide.items.Map;
import zombicide.players.Fighter;
import zombicide.players.Healer;
import zombicide.players.Lucky;
import zombicide.players.Nosy;
import zombicide.zombies.Abomination;
import zombicide.zombies.Runner;
import zombicide.zombies.Tank;
import zombicide.zombies.Walker;

public class Livrable2 {
	
	public static void main(String[] args) {
		
		Board board = new TrainingBoard(); //creation du plateau d'entrainement
		Game game = new Game(board); //creation de la partie
		
		//On creer les zombies
		Zombie zombie1 = new Walker("Lecrackhead");
		Zombie zombie2 = new Abomination("Tibooutshape");
		Zombie zombie3 = new Runner("Sansjambe");
		Zombie zombie4 = new Tank("Lelourd");
		
		//On ajoute les zombies à la partie 
		game.addZombie(zombie1,0,2);
		game.addZombie(zombie2,2,0);
		game.addZombie(zombie3,4,0);
		game.addZombie(zombie4,0,4);
		
		//On creer les joueurs
		Player player1 = new Fighter("Rambo");
		Player player2 = new Healer("Suport");
		Player player3 = new Lucky("Lechanceux");
		Player player4 = new Nosy("Lefêtar");
		
		game.addPlayer(player1, 2, 2);
		game.addPlayer(player2, 2, 2);
		game.addPlayer(player3, 2, 2);
		game.addPlayer(player4, 2, 2);
		
		
		
		//creation des cartes
		Map map1 = new Map(board) ;
		Map map2 = new Map(board) ;
		Map map3 = new Map(board) ;
		Map map4 = new Map(board) ;
		
		//ajout des cartes au inventaire des joueurs
		
		//affichage de l'inventaire 
		
		//creation des fioles
		
		
		//ajout des fioles au inventaire des joueurs
		
		
		//ajout des fioles dans la main des joueurs
		
		//affichage de l'inventaire 
		
		
		System.out.println("Avant déplacement");
		game.displayBoard();//affiche le plateau
		
		board.move(player1, Direction.TOP);
		board.move(player2, Direction.TOP);
		board.move(player3, Direction.TOP);
		board.move(player4, Direction.TOP);
		
		
		System.out.println("Apres déplacement");
		game.displayBoard();//affiche le plateau
		
	}
}
