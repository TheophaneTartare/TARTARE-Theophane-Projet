package zombicide;

import zombicide.boards.TrainingBoard;
import zombicide.cells.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest{

    private Cell c;
    private Cell c1;
    private Board b1;
    
    @BeforeEach
    public void before(){
        this.b1 = new TrainingBoard();
        this.c = new Room(b1);
        this.c1 = new Street(b1);
    }

    @Test
    public void cellDoorTest(){
        assertEquals('R',c.toCharacter());
        assertEquals(0,c.doors.size());
        assertEquals(0,c1.doors.size());
        assertFalse(c.getIsSpawner());

    }

    @Test
    public void cellStringTest(){
        assertEquals('S', c1.toCharacter());
        c1.setIsSpawner(true);
        assertEquals('E', c1.toCharacter());
    }
    
    @Test
    public void cellPositionTest() {
    	c.setPosition(0, 0);
    	assertEquals(0, c1.getwidth());
    	assertEquals(0, c1.getheight());
    }

}