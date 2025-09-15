package zombicide;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.*;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

import zombicide.items.* ;
import zombicide.items.weapons.Pistol;
import zombicide.actors.Player;
import zombicide.actors.Zombie;
import zombicide.boards.TrainingBoard;
import zombicide.cells.Room;
import zombicide.players.*;
import zombicide.zombies.Abomination;
import zombicide.zombies.Tank;
import zombicide.zombies.Walker;

public class ItemTest {
	private Room testRoom ;
	private List<Player> players ; 
	private Board board;

	
	@BeforeEach
    public void before(){
        this.board = new TrainingBoard();
		this.testRoom = new Room(board) ;
        Fighter player1 = new Fighter("Logan");
        Healer player2 = new Healer("Marvin") ;
        Lucky player3 = new Lucky("Théophane") ;
        Nosy player4 = new Nosy("Lucas") ;
        List<Player> players = new ArrayList<>() ;
        players.add(player1) ;
        players.add(player2) ;
        players.add(player3) ;
        players.add(player4) ;
        this.players = players ;
    }
	
	//test de ramassage des ( weapons compris ) ainsi que de drop
	@Test 
	public void pickupInHandAndDropTest() {
		// mise en place de la piece 
		Room testRoom = this.testRoom ;
		List<Player> players = this.players ;
		Player player3 = players.get(0) ;
		testRoom.addPlayer(player3);
		Flask flask = new Flask() ;
		testRoom.addItem(flask);
		
		// on prend le joueur de la piece et la flask de la piece
		List<Player> testRoomPlayers = testRoom.getPlayers() ;
		Player roomPlayer = testRoomPlayers.get(0);
		List<Item> floor = testRoom.getFloor() ;
		Item roomFlask = floor.get(0) ;
		
		// ont ajoute a l'inventaire la flask et on test 
		roomPlayer.addItemInventory(roomFlask);
		List<Item> inventory = roomPlayer.getInventory() ;
		Item playerFlask = inventory.get(0) ; 
		assertEquals(flask.toString() , playerFlask.toString() ) ;
	}
		
	@Test
	public void addRemoveHand(){
		// on ajoute a la main et on test
		Room testRoom = this.testRoom ;
		List<Player> players = this.players ;
		Player player3 = players.get(0) ;
		testRoom.addPlayer(player3);
		Flask flask = new Flask() ;
		testRoom.addItem(flask);

		List<Player> testRoomPlayers = testRoom.getPlayers() ;
		Player roomPlayer = testRoomPlayers.get(0);
		roomPlayer.addItemInventory(flask);
	
		roomPlayer.SetHand(flask); 
		Item handItem = roomPlayer.getHand();
		assertEquals(flask.toString(),handItem.toString()) ;
		
		// on remet l'item au sol et on test
		roomPlayer.removeInventory(0);
		List<Item> newFloor = testRoom.getFloor() ;
		Item newRoomFlask = newFloor.get(0) ;
		assertEquals(newRoomFlask.toString(), flask.toString() ) ;
	}
	
	// test aidKit 
	@Test
	public void testHealAidkit(){
		Room testRoom = this.testRoom ;
		List<Player> players = this.players ;
		Player player3 = players.get(0) ;
		Player player2 = players.get(0) ;
		testRoom.addPlayer(player3);
		testRoom.addPlayer(player2);
		AidKit aid = new AidKit() ;
		testRoom.addItem(aid);
		player3.addItemInventory(aid);
		player3.SetHand(aid);
		aid.newHolder(player3);
		aid.use(player2);
		assertEquals(6,player2.getHeal());
	}
	 
	
	// test de Flask 
	@Test
	public void testHealFlask(){
		Room testRoom = this.testRoom ;
		List<Player> players = this.players ;
		Player player3 = players.get(0) ;
		testRoom.addPlayer(player3);
		Flask flask = new Flask() ;
		testRoom.addItem(flask);
		player3.addItemInventory(flask);
		player3.SetHand(flask);
		flask.newHolder(player3);
		flask.use(null);
		assertEquals(6,player3.getHeal());
	}

	
	// test de infradedScope 
	
	// test de MasterKey (necessite un choix dans les directions) 
	
	// test d'une arme 
	@Test
	public void attackTest(){
		Weapon w = new Pistol();
		Player p = players.get(0);
		Zombie z = new Walker("wa");
		w.holder = p;
		boolean attack = w.attack();
		if (attack){
			z.takeDamage(w.damage);
			assertEquals(z.heal,0);
		}
		else {
			assertEquals(z.heal,1);
		}
	}

	@Test
	public void attackTestResist(){
		Weapon w = new Pistol();
		Player p = players.get(0);
		Zombie z = new Tank("t");
		Zombie z1 = new Abomination("a");
		w.holder = p;
		z.takeDamage(w.damage);
		z1.takeDamage(w.damage);
		assertEquals(z.heal,4);
		assertEquals(z1.heal,6);

	}
}
