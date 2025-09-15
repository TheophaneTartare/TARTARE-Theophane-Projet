package zombicide.boards;

import zombicide.Board;



public class RandomBoard extends Board{
	
	/**
     * Constructeur de la classe RandomBoard.
     *
     * @param width   La largeur du plateau.
     * @param height  La hauteur du plateau.
     * @param players La liste des joueurs à ajouter sur le plateau.
     */
	public RandomBoard(int width , int height) {
		// on crée un Board 
		super(width ,height );
		this.setSewer();
	}
	
	
	
	/**
	 * set all the sewer in the board
	 */
	private void setSewer(){
		this.cells[this.crossWidth][0].setIsSpawner(true);
		this.cells[this.crossWidth][this.height-1].setIsSpawner(true);
		this.cells[0][this.crossHeight].setIsSpawner(true);
		this.cells[this.width-1][this.crossHeight].setIsSpawner(true);
		
		this.sewers.add(this.cells[this.crossWidth][0]);
		this.sewers.add(this.cells[this.crossWidth][this.height-1]) ; 
		this.sewers.add(this.cells[0][this.crossHeight]);
		this.sewers.add(this.cells[this.width-1][this.crossHeight]);
	}
	
	
	
	
	

	
	

}