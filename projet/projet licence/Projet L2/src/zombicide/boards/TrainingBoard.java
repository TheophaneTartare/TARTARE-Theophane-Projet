package zombicide.boards;


import zombicide.Board;
import zombicide.cells.Continental;
import zombicide.cells.DrugStore;
import zombicide.cells.Room;
import zombicide.cells.Street;

public class TrainingBoard extends Board {
	
	/**
     * Constructs a training board with default dimensions and initializes it.
     */
	public TrainingBoard() {
		super(5, 5);
		this.initSewers();
	}

	/**
     * Initializes the layout of the training board.
     */
	protected void initBoard() {
		for(int i = 0; i < this.width; i++) {
			for(int j = 0; j < this.height; j++) {
				if(i == 2 || j == 2) {
					this.cells[i][j] = new Street(this);
				}else if(i == 0 && j == 0){
					this.cells[i][j] = new DrugStore(this);
				}else if(i == 3 && j == 3){
					this.cells[i][j] = new Continental(this);
				}else{
					this.cells[i][j] = new Room(this);
				}
				this.cells[i][j].setPosition(i, j);
			}
		}
		this.cells[2][2].setIsSpawner(true);
		
	}
	
	/**
	 * set all the sewer in the board
	 */
	protected void initSewers() {
		this.cells[2][2].setIsSpawner(true);
		this.sewers.add(this.cells[2][2]) ;
	}
	
}
