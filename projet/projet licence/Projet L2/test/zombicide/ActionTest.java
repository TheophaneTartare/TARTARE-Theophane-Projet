package zombicide;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import zombicide.items.* ;
import zombicide.items.weapons.Pistol;
import zombicide.action.Attack;
import zombicide.action.Heal;
import zombicide.action.LookAround;
import zombicide.action.MakeNoise;
import zombicide.action.Move;
import zombicide.action.PassTurn;
import zombicide.action.Search;
import zombicide.action.TakeOnHand;
import zombicide.action.Use;
import zombicide.actors.Player;
import zombicide.actors.Zombie;
import zombicide.boards.TrainingBoard;
import zombicide.cells.Continental;
import zombicide.cells.Room;
import zombicide.players.*;
import zombicide.zombies.Walker;

public class ActionTest {
    private Game game;
    private Board board;
    private Player player;
    private Room room;
    private Continental cont;

    @BeforeEach
    public void before(){
        this.board = new TrainingBoard();
        this.game = new Game(board);
        this.room = new Room(board);
        this.cont = new Continental(board);
        this.player = new Fighter("lalaland");
    }

    ////// Test LookAround

    @Test
    public void lookAroundRoom(){
        room.addPlayer(player);

        LookAround lookAround = new LookAround();
    
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    
        String expectedOutput = "Les joueur sur la case sont :\nlalaland\nLes zombie sur la case sont :\nLes porte ouverte sont :\nS\nN\nO\nE";
    
        lookAround.doAction(player, player, board, game, false);
    
        String actualOutput = outContent.toString().trim();
    
        String[] expectedLines = expectedOutput.split("\n");
        String[] actualLines = actualOutput.split("\n");
    
        for (int i = 0; i < Math.min(expectedLines.length, actualLines.length); i++) {
            assertEquals(expectedLines[i], actualLines[i].trim());
        }
    
        assertEquals(expectedLines.length, actualLines.length);
    } 

    @Test
    public void lookAroundContinental(){
        LookAround lookAround = new LookAround();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        cont.addPlayer(player);
        String res = "le continentale vous empeche de voir";
        lookAround.doAction(player, player, board, game, false);
        assertEquals(res.trim(),outContent.toString().trim());
    }

    /////// Test Make Noise

    @Test
    public void makeNoiseTest(){
        room.addPlayer(player);
        MakeNoise makeNoise = new MakeNoise();
        makeNoise.doAction(player, player, board, game, false);
        assertEquals(1,player.cell.noise);
        assertEquals(2,player.action);
    }

    //////// Test Heal

    @Test
    public void healTest(){
        room.addPlayer(player);
        Heal heal = new Heal();
        heal.doAction(player, player, board, game, false);
        assertEquals(6, player.heal);
    }

    /////// Test Attack
    /////// Test Move
    /////// Test Open door
    /////// Test Pass turn
    @Test
    public void passTest(){
        Player p = new Fighter("l");
        PassTurn pass = new PassTurn();
        pass.doAction(p, p, board, game, false);
        assertEquals(2,p.getAction());
    }
    /////// Test Search
    @Test
    public void searchTestnoitem(){
        room.addPlayer(player);
        Search s = new Search();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String expectedOutput = "il n'y a pas d'objet";

        s.doAction(player, player, board, game, false);
        String actualOutput = outContent.toString().trim();

        assertEquals(expectedOutput,actualOutput);
    }

    /////// Test Take on hand
    @Test
    public void takeonhandTest(){
        Weapon w = new Pistol();
        Player p = new Fighter("l");
        p.addItemInventory(w);
        TakeOnHand toh = new TakeOnHand();
        toh.doAction(p, p, board, game, false);
        assertEquals(w,p.getHand());
    }
    /////// Test Use

    @Test
    public void useTest(){
        Use use = new Use();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        use.doAction(player, player, board, game, false);

        String expectedOutput = "pas d'item dans la main";
        String actualOutput = outContent.toString().trim();

        assertEquals(expectedOutput,actualOutput);
    }

    @Test
    public void useOnHandTest(){
        Player p1 = new Fighter("z");
        room.addPlayer(p1);
        room.addPlayer(player);
        AidKit aid = new AidKit();
        aid.holder = p1;
        
        player.SetHand(aid);
        assertEquals(aid, player.getHand());
    
        Use use = new Use();
        use.doAction(player, player, board, game, false);
    
        assertEquals(6, player.getHeal());
        assertEquals(2, p1.getAction());
        assertNull(player.getHand());
    }
}