package zombicide;

import zombicide.cells.*;
import zombicide.boards.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void testBoardCellDoorCoin(){
        
        Board b = new TrainingBoard();
        assertEquals(2,b.cells[0][0].doors.size());
        assertEquals(2,b.cells[ 0 ][ b.height-1 ].doors.size());
        assertEquals(2,b.cells[ b.width-1 ][ 0 ].doors.size());
        assertEquals(2,b.cells[ b.width-1 ][ b.height-1 ].doors.size());
        assertTrue(b.cells[ 0 ][ 0 ].doors.containsKey(Direction.RIGHT));
        assertTrue(b.cells[ 0 ][ 0 ].doors.containsKey(Direction.BOTTOM));  
        assertTrue(b.cells[ 0 ][ b.height-1 ].doors.containsKey(Direction.LEFT));
        assertTrue(b.cells[ 0 ][ b.height-1 ].doors.containsKey(Direction.BOTTOM));
        assertTrue(b.cells[ b.width-1 ][ 0 ].doors.containsKey(Direction.RIGHT));
        assertTrue(b.cells[ b.width-1 ][ 0 ].doors.containsKey(Direction.TOP));
        assertTrue(b.cells[ b.width-1 ][ b.height-1 ].doors.containsKey(Direction.LEFT));
        assertTrue(b.cells[ b.width-1 ][ b.height-1 ].doors.containsKey(Direction.TOP));
    }


    @Test
    public void testBoardCellDoorBordure(){
        Board b = new TrainingBoard();
        for (int i=0;i<b.width;i++){
            for (int j=0;j<b.height;j++){
                if (j==0 && i!=0 && i!=b.width-1 || i==0 && j!=0 && j!=b.height-1 || j==b.height-1 && i!=0 && i!=b.width-1 || i==b.width-1 && j!=0 && j!=b.height-1){
                    if ( j==0 && i!=0 && i!=b.width-1 && b.cells[i][j].toCharacter() != 'S' && b.cells[i][j].toCharacter() != 'E'){
                        assertTrue(b.cells[i][j].doors.containsKey(Direction.TOP));
                        assertTrue(b.cells[i][j].doors.containsKey(Direction.RIGHT));
                        assertTrue(b.cells[i][j].doors.containsKey(Direction.BOTTOM));
                        assertEquals(3,b.cells[i][j].doors.size());
                    }
                    else if ( i==0 && j!=0 && j!=b.height-1 && b.cells[i][j].toCharacter() != 'S' && b.cells[i][j].toCharacter() != 'E'){
                        assertTrue(b.cells[i][j].doors.containsKey(Direction.BOTTOM));
                        assertTrue(b.cells[i][j].doors.containsKey(Direction.RIGHT));
                        assertTrue(b.cells[i][j].doors.containsKey(Direction.LEFT));
                        assertEquals(3,b.cells[i][j].doors.size());
                    }
                    else if ( j==b.height-1 && i!=0 && i!=b.width-1 && b.cells[i][j].toCharacter() != 'S' && b.cells[i][j].toCharacter() != 'E'){
                        assertTrue(b.cells[i][j].doors.containsKey(Direction.TOP));
                        assertTrue(b.cells[i][j].doors.containsKey(Direction.LEFT));
                        assertTrue(b.cells[i][j].doors.containsKey(Direction.BOTTOM));
                        assertEquals(3,b.cells[i][j].doors.size());
                    }
                    else if ( i==b.width-1 && j!=0 && j!=b.height-1 && b.cells[i][j].toCharacter() != 'S' && b.cells[i][j].toCharacter() != 'E'){
                        assertTrue(b.cells[i][j].doors.containsKey(Direction.RIGHT));
                        assertTrue(b.cells[i][j].doors.containsKey(Direction.LEFT));
                        assertTrue(b.cells[i][j].doors.containsKey(Direction.TOP));
                        assertEquals(3,b.cells[i][j].doors.size());
                    }
                    else if (b.cells[i][j].toCharacter() == 'E' || b.cells[i][j].toCharacter() == 'S'){
                        assertEquals(0,b.cells[i][j].doors.size());
                    }
                }
            }
        }
    }

    @Test
    public void testBoardCellDoorMilieu(){
        Board b = new TrainingBoard();
        for (int i=0;i<b.width;i++){
            for (int j=0;j<b.height;j++){
                if (i!=0 && j!=0 && i!=b.width-1 && j!=b.height-1 && b.cells[i][j].toCharacter() != 'S' && b.cells[i][j].toCharacter() != 'E'){
                    assertEquals(4,b.cells[i][j].doors.size());
                    assertTrue(b.cells[i][j].doors.containsKey(Direction.BOTTOM));
                    assertTrue(b.cells[i][j].doors.containsKey(Direction.LEFT));
                    assertTrue(b.cells[i][j].doors.containsKey(Direction.TOP));
                    assertTrue(b.cells[i][j].doors.containsKey(Direction.RIGHT));
                }
                else if (b.cells[i][j].toCharacter() == 'S' || b.cells[i][j].toCharacter() == 'E'){
                    assertEquals(0,b.cells[i][j].doors.size());
                }
            }
        }
    }
}