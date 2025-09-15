package zombicide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import zombicide.actors.Player;
import zombicide.actors.Zombie;
import zombicide.boards.TrainingBoard;
import zombicide.cells.Room;
import zombicide.items.Flask;
import zombicide.items.Weapon;
import zombicide.items.weapons.Pistol;
import zombicide.items.weapons.Rifle;
import zombicide.players.Fighter;
import zombicide.players.Healer;
import zombicide.players.Lucky;
import zombicide.players.Nosy;
import zombicide.zombies.Tank;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;




public class PlayerTest {
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

	
	@Test 
	public void playersCanDieAndHaveHisBackPackTest() {
		// mise en place du test
		Room testRoom = this.testRoom ;
		List<Item> testFloor = new ArrayList<Item>(); ;
		Flask flask = new Flask() ;
		testFloor.add(flask);
		List<Player> players = this.players ;
		Player player1 = players.get(0) ;
		testRoom.addPlayer(player1);
		
		player1.addItemInventory(flask);
		
		
		
		//test si le joueurs meurt et si il laisse le contenue de son sac a dos a terre
		assertEquals( 5, player1.getHeal());
		player1.suppHeal(4);
		assertEquals( 1, player1.getHeal());
		player1.dropBag();
		assertEquals(testFloor,testRoom.getFloor());
		
	}

	@Test
	public void getzombies(){
		Player p = this.players.get(0);
		Zombie z = new Zombie("z");
		Weapon w = new Pistol();
		List<Zombie> lz = new ArrayList<Zombie>();
		lz.add(z);
		p.SetHand(w);
		testRoom.addPlayer(p);
		testRoom.addZombie(z);
		assertEquals(lz,p.getZombiesInRange(w.getRange()));
	}

	@Test
	public void getzombiesrange(){
		Player p = this.players.get(0);
		Zombie z = new Zombie("z");
		Zombie z1 = new Zombie("z1");
		Weapon w = new Rifle();
		List<Zombie> lz = new ArrayList<Zombie>();
		lz.add(z);
		lz.add(z1);
		Cell c = board.getCell(2, 2);
		Cell c1 = board.getCell(0, 2);
		c.addPlayer(p);
		c.addZombie(z);
		c1.addZombie(z1);
		p.SetHand(w);
		assertEquals(lz,p.getZombiesInRange(w.getRange()));
	}

}
