package zombicide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import zombicide.actors.Player;
import zombicide.actors.Zombie;
import zombicide.cells.Continental;
import zombicide.cells.DrugStore;
import zombicide.cells.Room;
import zombicide.cells.Street;

/**
 * The abstract class representing the game board for Zombicide.
 */
public abstract class Board {
	/* The width of the board.*/
	protected int width;
	/* The height of the board.*/
	protected int height;
	/* The 2D array representing the cells of the board.*/
	protected Cell[][] cells;
	/* a list with all the cell that are sewers in the board */
	protected List<Cell> sewers;
	/* main crossroad Width */
	protected int crossWidth ;
	/* main crossroad Height */
	protected int crossHeight ;
	/* list of the room */
	private List<Cell> listRoom ;
	
	
	/**
     * Build a board with the specified width and height.
     *
     * @param width The width of the board.
     * @param height The height of the board.
     */
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.cells = new Cell[width][height];
		this.sewers=new ArrayList<Cell>();
		this.listRoom = new ArrayList<Cell>() ;
		this.crossWidth = 0 ;
		this.crossHeight = 0 ;
		this.initBoard(0, width  , 0,height   );
		this.placeSpecialRoom();
		this.initDoor();
	}
	
	/**
	 * give the width of the board
	 * @return the width of the board
	 */
	public int getwidth() {
		return this.width ;
	}
	
	/**
	 * give the height of the board
	 * @return the height of the board
	 */
	public int getHeight() {
		return this.height ; 
	}
	
	/**
	 * get all the sewers
	 * @return all the sewers in the board
	 */
	public List<Cell> getSewers(){
		return this.sewers;
	}
	
	/**
	 * get all the room of the board
	 * @return all the room of the board
	 */
	public List<Cell> getListRoom(){
		return this.listRoom ;
	}
	
	/**
	 * get the crossWidth
	 * @return the crossWidth
	 */
	public int getCrossWidth() {
		return this.crossWidth ;
	}
	
	/**
	 * get the crossHeight
	 * @return the crpssHeight 
	 */
	public int getCrossHeight() {
		return this.crossHeight ;
	}

	/**
	 * initialize the board 
	 * 
	 * @param startWidth The starting width coordinate of the board.
	 * @param endWidth   The ending width coordinate of the board.
	 * @param startHeight The starting height coordinate of the board.
	 * @param endHeight  The ending height coordinate of the board.
	 */
	public void initBoard(int startWidth , int endWidth , int startHeight , int endHeight) {
		if (endWidth - startWidth < 5 && endHeight  - startHeight < 5) {
			this.fillBulding(startHeight , endHeight, startWidth , endWidth);
		}
		
		else if (endWidth  - startWidth >=  5 && endHeight  - startHeight >= 5) {
			this.whenHeightAndWidthLong(startWidth, endWidth, startHeight, endHeight);
		}
		else {
			this.whenOneSideNotLongEnough(startWidth, endWidth, startHeight, endHeight);
		}
		
	}
	
	/**
	 * when both width and height are superior to five make a crossroad and do initBoard at the four side
	 * 
	 * @param startWidth The starting width coordinate of the board.
	 * @param endWidth   The ending width coordinate of the board.
	 * @param startHeight The starting height coordinate of the board.
	 * @param endHeight  The ending height coordinate of the board.
	 */
	private void whenHeightAndWidthLong(int startWidth , int endWidth , int startHeight , int endHeight) {
		int miniCrossWidth = startWidth + this.randomNumber( endWidth - startWidth )   ;
		int miniCrossHeight = startHeight + this.randomNumber( endHeight - startHeight ) ;
		
		if (this.crossHeight == 0 ) {
			this.crossHeight = miniCrossHeight ; 
		}
		if (this.crossWidth == 0) {
			this.crossWidth = miniCrossWidth ;
		}
		this.pathWidth(miniCrossWidth, startHeight , endHeight);
		this.pathHeight(startWidth, endWidth, miniCrossHeight );
	
		this.initBoard( startWidth ,  miniCrossWidth - 1,  startHeight ,  miniCrossHeight - 1 ) ;
		this.initBoard( miniCrossWidth + 1 ,  endWidth ,  startHeight ,  miniCrossHeight - 1 ) ;
		this.initBoard( startWidth ,  miniCrossWidth - 1 ,  miniCrossHeight + 1 ,  endHeight) ;
		this.initBoard( miniCrossWidth ,  endWidth ,  miniCrossHeight + 1 ,  endHeight) ;
	}
	
	/**
	 * when either the width or the height are superior to five but the other is not you make a road 
	 * that cut in two the longest size and do initboard on the two side 
	 * 
	 * @param startWidth The starting width coordinate of the board.
	 * @param endWidth   The ending width coordinate of the board.
	 * @param startHeight The starting height coordinate of the board.
	 * @param endHeight  The ending height coordinate of the board.
	 */
	private void whenOneSideNotLongEnough(int startWidth , int endWidth , int startHeight , int endHeight) {
		if (endWidth - startWidth < 5) {
			int miniCrossHeight = startHeight + this.randomNumber( endHeight - startHeight ) ;
			
			this.pathHeight(startWidth, endWidth, miniCrossHeight );
			this.initBoard(startWidth, endWidth, startHeight, miniCrossHeight - 1);
			this.initBoard(startWidth, endWidth,miniCrossHeight + 1 , endHeight);	
		}
		else {
			int miniCrossWidth = startWidth + this.randomNumber( endWidth - startWidth )  ;
							
			this.pathWidth(miniCrossWidth , startHeight , endHeight);
			this.initBoard(startWidth, miniCrossWidth - 1 , startHeight, endHeight);
			this.initBoard(miniCrossWidth + 1 , endWidth, startHeight, endHeight);
		}
	}
	
	/**
	 * make a road that cut the board in two on the width 
	 * @param randomWidth the width where the separation will be
	 * @param startHeight the height where the separation start 
	 * @param endHeight the height where the separation end
	 */
	private void pathWidth(int randomWidth , int startHeight , int endHeight) {
		if (endHeight == this.height) {
			endHeight -= 1 ;
		} 
		for (int i = startHeight ; i <= endHeight ; i ++ ) {
			this.cells[randomWidth][i] = new Street(this) ;
			this.cells[randomWidth][i].setPosition(randomWidth,i);
		}
	}
	
	/**
	 * make a road that cut the board in two on the height
	 * @param startWidth the width where the separation start 
	 * @param endWidth the width
	 * @param randomHeight the height where the separation will be
	 */
	private void pathHeight(int startWidth , int endWidth,int randomHeight ) {
		if (endWidth == this.width) {
			endWidth -= 1 ;
		}
		for (int i = startWidth ; i <= endWidth ; i ++ ) {
			this.cells[i][randomHeight] = new Street(this) ;
			this.cells[i][randomHeight].setPosition(i,randomHeight);
		}
	}
	
	
	/**
	 *  fill the "square" given with the parameter of room and add them in the list of room
	 * @param startWidth The starting width coordinate of the board.
	 * @param endWidth   The ending width coordinate of the board.
	 * @param startHeight The starting height coordinate of the board.
	 * @param endHeight  The ending height coordinate of the board.
	 */
	private void fillBulding(int startHeight, int endHeight, int startWidth , int endWidth) {
		if (endWidth == this.width) {
			endWidth -= 1 ;
		}
		if (endHeight == this.height) {
			endHeight -= 1 ;
		} 
		
		for ( int i = startWidth ; i <= endWidth ; i ++ ) {
			for (int j = startHeight ; j <= endHeight ; j ++ ) {
				if ( this.cells[i][j] == null ) {
					this.cells[i][j]=new Room(this);
					this.listRoom.add(this.cells[i][j]) ;
					this.cells[i][j].setPosition(i, j) ;
				}
			}
		}
	}
	
	/**
	 * take the list of room a randomly chose two cell that will be the Drugstore and the Continental
	 */
	private void placeSpecialRoom() {
		int x = this.randomNumber(this.listRoom.size() -1 ) ;
		int y = this.randomNumber(this.listRoom.size() -1  ) ;
		if ( x == y) {
			y += 1 ;
		}
		if ( y == this.listRoom.size() ) {
			y = 0 ;
		}
		Cell drug = this.listRoom.get(x) ;
		Cell cont = this.listRoom.get(y) ;
		this.cells[drug.getwidth()][drug.getheight()] = new DrugStore(this) ;
		this.cells[drug.getwidth()][drug.getheight()].setPosition(drug.getwidth(), drug.getheight()) ;
		this.cells[cont.getwidth()][cont.getheight()] = new Continental(this) ;
		this.cells[cont.getwidth()][cont.getheight()].setPosition(cont.getwidth(), cont.getheight()) ;
	}
	
	/**
     * Génère un nombre aléatoire dans l'intervalle [2, n-2].
     *
     * @param n La valeur maximale de l'intervalle.
     * @return Un nombre aléatoire entre 2 et n-2.
     */
	private int randomNumber(int n) {
		int res = new Random().nextInt(n)  ;
		
		if ( res < 2) {
			res = 2 ;
		}
		else if ( res > n-3) { 
			res = n-3 ;
		}
		return res ;
	}
	
	/**
	 * give the cell of the parameter 
	 * @param x the width of wanted cell
	 * @param y the height of wanted cell
	 * @return
	 */
	public Cell getCell(int x, int y){
		return this.cells[x][y];
	}
	
	/**
	 * give the most noisy cell
	 * @return the cell with the most noise
	 */
	public Cell getMoreNoisyCell(){
		Cell cell = this.cells[0][0];
		for(int x = 0; x < this.height; x++) {
			for(int y = 0; y < this.height; y++) {
				if(cell.getNoise() < this.cells[x][y].getNoise()){
					cell = this.cells[x][y];
				}
			}
		}
		return cell;
	}

	
	/* Initializes the doors coin on the board.*/
	public void initDoorsCoin(){
		this.cells[0][0].addDoors(Direction.BOTTOM);
		this.cells[0][0].addDoors(Direction.RIGHT);
		this.cells[ 0 ][ this.height-1 ].addDoors(Direction.BOTTOM);
		this.cells[ 0 ][ this.height-1 ].addDoors(Direction.LEFT);
		this.cells[ this.width-1 ][ 0 ].addDoors(Direction.TOP);
		this.cells[ this.width-1 ][ 0 ].addDoors(Direction.RIGHT);
		this.cells[ this.width-1 ][ this.height-1 ].addDoors(Direction.TOP);
		this.cells[ this.width-1 ][ this.height-1 ].addDoors(Direction.LEFT);
	}

	/* Initializes the doors bordure on the board.*/
	protected void initDoorsBordure(){
		for (int i=0;i<this.width;i++){
			for (int j=0;j<this.height;j++){
				if (j==0 && i!=0 && i!=this.width-1){
					this.cells[i][j].addDoors(Direction.RIGHT);
					this.cells[i][j].addDoors(Direction.TOP);
					this.cells[i][j].addDoors(Direction.BOTTOM);
				}
				else if (i==0 && j!=0 && j!=this.height-1){
					this.cells[i][j].addDoors(Direction.BOTTOM);
					this.cells[i][j].addDoors(Direction.RIGHT);
					this.cells[i][j].addDoors(Direction.LEFT);
				}
				else if (j==this.height-1 && i!=0 && i!=this.width-1){
					this.cells[i][j].addDoors(Direction.BOTTOM);
					this.cells[i][j].addDoors(Direction.LEFT);
					this.cells[i][j].addDoors(Direction.TOP);
				}
				else if (i==this.width-1 && j!=0 && j!=this.height-1){
					this.cells[i][j].addDoors(Direction.TOP);
					this.cells[i][j].addDoors(Direction.LEFT);
					this.cells[i][j].addDoors(Direction.RIGHT);
				}
			}
		}
	}

	/**
	 * init doors who aren't in a coin or a bordure
	 */
	protected void initDoorRest(){
		for (int i=0;i<this.width;i++){
			for (int j=0;j<this.height;j++){
				if (j!=0 && j!=this.height-1 && i!=0 && i!=this.width-1){
					this.cells[i][j].addDoors(Direction.BOTTOM);
					this.cells[i][j].addDoors(Direction.TOP);
					this.cells[i][j].addDoors(Direction.LEFT);
					this.cells[i][j].addDoors(Direction.RIGHT);
				}
			}
		}
	}
	
	/**
	 * remove the doors in the cell Street
	 */
	protected void doorInStreet(){
		for (int i=0;i<this.width;i++){
			for (int j=0;j<this.height;j++){
				this.cells[i][j].noDoorStreet();
			}
		}
	}

	/**
	 * init the doors
	 */
	protected void initDoor(){
		this.initDoorsCoin();
		this.initDoorsBordure();
		this.initDoorRest();
		this.doorInStreet();
	}
	
	/*
	---------------------
	|D  |R  |E  |R  |R  |
	|R  |R  |S  |R  |R  |
	|E  |S  |Ss1|S  |E  |
	|R  |R  |S  |C  |R  |
	|R  |R  |E  |R  |R  |
	---------------------
	*/
	
	/* Displays the board layout along with players and zombies.*/
	public void display() {
        for(int i = 0; i < this.cells.length ; i++){
        	/* Debut de la limite en haut du plateau de jeu */
        	for(int k = 0; k < this.cells[0].length ; k++) {
    			System.out.print("----");
    		}
        	/* Fin de la limite avec un saut de ligne */
    		System.out.println("-");
    		/* 1er ligne : Affichage des cases avec leur lettre et le nombre de survivants*/
            for(int j = 0; j < this.cells[i].length ; j++){
            	System.out.print('|');
                System.out.print(this.cells[i][j].toCharacter());
                if(this.cells[i][j].getNbPlayers() != 0) {
                	System.out.print("s"+this.cells[i][j].getNbPlayers());
                }else {
                	System.out.print("  ");
                }
            }
            /* Saut de ligne */
        	System.out.print('|');
            System.out.println();
            /* 2eme ligne : Affichage des cases avec le nombre de zombie*/
            for(int j = 0; j < this.cells[i].length ; j++){
            	System.out.print("| ");
                if(this.cells[i][j].getNbZombies() != 0) {
                	System.out.print("z"+this.cells[i][j].getNbZombies());
                }else {
                	System.out.print("  ");
                }
            }
            /* Saut de ligne */
        	System.out.print('|');
            System.out.println();
        }
    	/* Debut de la limite en bas du plateau de jeu */
        for(int k = 0; k < this.cells[0].length ; k++){
			System.out.print("----");
		}
    	/* Fin de la limite avec un saut de ligne */
		System.out.println("-");
    }
	
	/**
	 * the display that is do when you use the infradedScope
	 * @param cell the cell of the player who activated the infradedScope
	 */
	public void displayInfradedScope(Cell cell) {
		int Ligne=cell.width;
		int Colonne=cell.height;
		int debutLigne = Math.max(0, Ligne - 1);
	    int finLigne = Math.min(cells.length - 1, Ligne + 1);
	    int debutColonne = Math.max(0, Colonne - 1);
	    int finColonne = Math.min(cells[0].length - 1, Colonne + 1);

	    // Ligne de séparation supérieure
	    for (int k = debutColonne; k <= finColonne; k++) {
	        System.out.print("-----");
	    }
	    System.out.println();

	    for (int i = debutLigne; i <= finLigne; i++) {
	        System.out.print("|");

	        for (int k = debutColonne; k <= finColonne; k++) {
	            System.out.print(" ");
	            System.out.print(cells[i][k].toCharacter());
	            if (cells[i][k].getNbPlayers() != 0) {
	                System.out.print("s" + cells[i][k].getNbPlayers());
	            }
	            if(this.cells[i][k].getNbZombies() != 0) {
                	System.out.print("z"+this.cells[i][k].getNbZombies());
	            }
                else {
	                System.out.print("  ");
	            }
	            System.out.print("|");
	        }
	        System.out.println();

	        // Ligne de séparation entre les lignes de cases
	        System.out.print("-");
	        for (int k = debutColonne; k <= finColonne; k++) {
	            System.out.print("-----");
	        }
	        System.out.println();
	    }
	}
	
	/**
     * Adds players to the central room of the training board.
     *
     * @param players The list of players to add.
     */
	public void addPlayer( Player player , int i , int j) {
		this.cells[i][j].addPlayer(player);
		player.setPosition(i,j);	
	}
	
	
	/**
	 * add the item on the cell with the width and the height in parameter
	 * @param item the item
	 * @param width the width of the cell
	 * @param height the height of the cell
	 */
	public void addOneItem(Item item , int width , int height) {
		Cell cell = this.cells[width][height] ;
		cell.addItem(item); ;
	}
	
	

	/**
     * Adds a zombie to the board.
     *
     * @param zombie The zombie to add.
     */
	public void addZombie(Zombie zombie, int i , int j) {
		this.cells[i][j].addZombie(zombie);
		zombie.setPosition(i,j);	
	}
	
	/**
	 * Move the actor in the given direction if possible.
	 *
	 * @param actor     The actor to be moved.
	 * @param direction The direction in which the actor should be moved.
	 */
	public void move(Actor actor,Direction direction) {
		Cell actorCell = this.cells[actor.getWidthPosition()][actor.getHeightPosition()];  
		boolean haveDoor = actorCell.doors.containsKey(direction);
		boolean constraintDoor = (!haveDoor || actorCell.isDoorOpen(direction));
		if (direction==Direction.LEFT){
			this.moveLeft(actor, constraintDoor);
		}
		else if (direction==Direction.BOTTOM){
			this.moveBottom(actor, constraintDoor);
		}
		else if (direction==Direction.RIGHT){
			this.moveRight(actor, constraintDoor);
		}
		else {
			this.moveTop(actor, constraintDoor);
		}
	}

	/**
	 * move the actor to the cell at his left
	 * @param actor the actor who will move at the left
	 * @param constraintDoor the constraint of the door
	 */
	private void moveLeft(Actor actor,boolean constraintDoor){
		if (actor.getHeightPosition()-1 >= 0 && constraintDoor){
			Cell newCellActor = this.cells[actor.getWidthPosition()][actor.getHeightPosition()-1];
			if (!newCellActor.doors.containsKey(Direction.RIGHT) || newCellActor.isDoorOpen(Direction.RIGHT)){
				this.RemoveActor(actor);
				actor.setPosition(actor.getWidthPosition(), actor.getHeightPosition()-1);
				this.moveActor(actor);
			}
		}
	}

	/**
	 * move the actor to the cell down him
	 * @param actor the actor who will move at the bottom
	 * @param constraintDoor the constraint of the door
	 */
	private void moveBottom(Actor actor,boolean constraintDoor){
		if (actor.getWidthPosition()+1 <=this.width-1 && constraintDoor){
			Cell newCellActor = this.cells[actor.getWidthPosition()+1][actor.getHeightPosition()];
			if (!newCellActor.doors.containsKey(Direction.TOP) || newCellActor.isDoorOpen(Direction.TOP)){
				this.RemoveActor(actor);
				actor.setPosition(actor.getWidthPosition()+1, actor.getHeightPosition());
				this.moveActor(actor);
			}
		}
	}

	/**
	 * move the actor to the cell at his right
	 * @param actor the actor who will move at the right
	 * @param constraintDoor the constraint of the door
	 */
	private void moveRight(Actor actor,boolean constraintDoor){
		if (actor.getHeightPosition()+1 <= this.height-1 && constraintDoor){
			Cell newCellActor = this.cells[actor.getWidthPosition()][actor.getHeightPosition()+1];
			if (!newCellActor.doors.containsKey(Direction.LEFT) || newCellActor.isDoorOpen(Direction.LEFT)){
				this.RemoveActor(actor);
				actor.setPosition(actor.getWidthPosition(), actor.getHeightPosition()+1);
				this.moveActor(actor);
			}
		}
	}

	/**
	 * move the actor to th cell up him
	 * @param actor the actor who will move at the top
	 * @param constraintDoor the constraint of the door
	 */
	private void moveTop(Actor actor,boolean constraintDoor){
		if (actor.getWidthPosition()-1 >= 0 && constraintDoor){
			Cell newCellActor = this.cells[actor.getWidthPosition()-1][actor.getHeightPosition()];
			if (!newCellActor.doors.containsKey(Direction.BOTTOM) || newCellActor.isDoorOpen(Direction.BOTTOM)){
				this.RemoveActor(actor);
				actor.setPosition(actor.getWidthPosition()-1, actor.getHeightPosition());
				this.moveActor(actor);
			}
		}
	}

	/**
	 * Removes the specified actor from the cell it currently occupies.
	 *
	 * @param actor The actor to be removed from the cell it occupies.
	 */
	private void RemoveActor(Actor actor) {
		Cell actorCell=this.cells[actor.getWidthPosition()][actor.getHeightPosition()]; //Cell of actor
		if(actor instanceof Player) {
			actorCell.removePlayer((Player)actor);
		}
		else {
			actorCell.removeZombie((Zombie)actor);
		}
	}
	
	/**
	 * Moves the specified actor to the cell.
	 *
	 * @param actor The actor to be moved to its new cell position.
	 */
	private void moveActor(Actor actor){
		
		Cell actorCell=this.cells[actor.getWidthPosition()][actor.getHeightPosition()]; //Cell of actor
		if(actor instanceof Player) {
			actorCell.addPlayer((Player)actor);
		}
		else {
			actorCell.addZombie((Zombie)actor);
		}
		
	}
	
	/**
	 * obtain the cell of the direction
	 * @param cell the current cell
	 * @param direction the direction of the futur cell 
	 * @return the cell on the direction of the current cell
	 */
	public Cell getNextCell(Cell cell,Direction direction) {
		int widthCurrentCell=cell.getwidth();
		int heightCurrentCell=cell.getheight();
		if(direction==Direction.TOP){
			return cells[widthCurrentCell-1][heightCurrentCell];
		}
		else if(direction==Direction.RIGHT){
			return cells[widthCurrentCell][heightCurrentCell+1];
		}
		else if(direction==Direction.BOTTOM){
			return cells[widthCurrentCell+1][heightCurrentCell];
		}
		else {
			return cells[widthCurrentCell][heightCurrentCell-1];
		}
	}
	
	/**
	 * revert to 0 the noise in  all cell
	 */
	public void resetNoise() {
		for (int i=0;i<this.width;i++){
			for (int j=0;j<this.height;j++){
				this.cells[i][j].noise=0;
			}
		}
	}
}
