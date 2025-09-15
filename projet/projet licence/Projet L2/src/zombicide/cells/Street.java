package zombicide.cells;

import zombicide.Board;
import zombicide.Cell;

/**
 * Represents a street cell on the Zombicide game board.
 */
public class Street extends Cell {
	
	/**
     * Constructs a street cell.
     */
	public Street(Board board) {
		super(board);
		this.hasFloor = false ;
	}

	
	/**
     * Returns the character representation of a street cell.
     * If the street cell is a spawner, it returns 'E' (for entrance), otherwise 'S' (for street).
     *
     * @return The character 'E' if the street cell is a spawner, otherwise 'S'.
     */
	public char toCharacter() {
		if(this.isSpawner) {
			return 'E';
		}else{
			return 'S';
		}
	}

}
