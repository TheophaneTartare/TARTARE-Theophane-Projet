package zombicide.cells;

import zombicide.Board;
import zombicide.Cell;

/**
 * Represents a room cell on the Zombicide game board.
 */
public class Room extends Cell {
	
	/**
     * Constructs a room cell.
     */
	public Room(Board board) {
		super(board);
		this.hasFloor = true ;
	}

	/**
     * Returns the character representation of a room cell.
     *
     * @return The character 'R' representing a room cell.
     */
	public char toCharacter() {
		return 'R';
	}
	
}
