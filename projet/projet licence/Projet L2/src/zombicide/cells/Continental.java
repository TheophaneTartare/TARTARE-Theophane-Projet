package zombicide.cells;

import zombicide.Board;
import zombicide.Cell;

/**
 * Represents a continental cell on the Zombicide game board.
 */
public class Continental extends Cell {
	
	/**
     * Constructs a continental cell.
     */
	public Continental(Board Board) {
		super(Board);
		this.hasFloor = true ;
		this.canAttack=false;
	}

	/**
     * Returns the character representation of a continental cell.
     *
     * @return The character 'C' representing a continental cell.
     */
	public char toCharacter() {
		return 'C';
	}

}
