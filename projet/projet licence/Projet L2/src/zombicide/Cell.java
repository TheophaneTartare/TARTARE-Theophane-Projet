package zombicide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import zombicide.actors.Player;
import zombicide.actors.Zombie;
import zombicide.cells.Street;

public abstract class Cell {
	
	/*The list of players currently occupying the cell.*/
	protected List<Player> players;
	/*The list of zombies currently occupying the cell.*/
	protected List<Zombie> zombies;
	/*The map representing the doors of the cell.*/
	protected HashMap<Direction, Door> doors;
	/*Indicates if the cell is a spawner.*/
	protected boolean isSpawner;
	/*The width position of the cell on the board.*/
	protected int width;
	/*The height position of the cell on the board.*/
	protected int height; 
	/* determined if Item can be on the floor */
	protected boolean hasFloor ;
	/*The "floor" of the cell on witch there are item */
	protected List<Item> floor ; 
	/* The level of noise inside this cell */
	protected int noise;
	/* a boolean that indicate if you can attack in the cell */
	protected boolean canAttack;
	/*the board of the game in which the cell is in*/
	protected Board board;
	
	/**
     * Constructs a cell with default values.
     */
	public Cell(Board board) {
		this.isSpawner = false;
		this.players = new ArrayList<Player>();
		this.zombies = new ArrayList<Zombie>();
		this.doors = new HashMap<Direction, Door>();
		this.floor = new ArrayList<Item>();
		this.canAttack = true;
		this.board = board;
	}    
	
	/** 
	 * if the cell is a Sreat there is no doors 
	 * */ 
	protected void noDoorStreet(){
		if (this.toCharacter()=='E' || this.toCharacter()=='S')
			this.doors.clear();
	}
	/**
	 * Add the door of the direction
	 * @param direction The direction of the door we are going to add
	 */
	public void addDoors(Direction direction){
		Door d = new Door(false);
		this.doors.put(direction,d);
	}
	
	/**
	 * get the doors of the room
	 * @return the HashMap with the doors 
	 */
	public HashMap<Direction, Door> getDoors(){
		return this.doors ;
	}

		
	/**
	 * Remove the door of the direction
	 * @param direction The direction of the door we are going to remove
	 */
	public void removeDoors(Direction direction){
		this.doors.remove(direction);
	}
	
	/**
	 * open the door of the direction given
	 * @param direction the direction
	 */
	public void openDoor(Direction direction) {
		Cell nextPlayerCell=board.getNextCell(this, direction);
		Door door=this.getDoor(direction);
		Door nextCellDoor=nextPlayerCell.getDoor(this.oppositeDirection(direction));
		if(this instanceof Street) {
			nextCellDoor.setOpen(true);
		}
		else if (nextPlayerCell instanceof Street){
			door.setOpen(true);
		}
		else {
			door.setOpen(true);
			nextCellDoor.setOpen(true);
		}
	}
	
	/**
	 * return the doors of the opposite Direction of the one given
	 * @param direction said direction
	 * @return the opposite of the parameter
	 */
	private Direction oppositeDirection(Direction direction) {
		if (direction==Direction.TOP) {
			return Direction.BOTTOM;
		}
		else if(direction==Direction.RIGHT) {
			return Direction.LEFT;
		}
		else if(direction==Direction.LEFT) {
			return Direction.RIGHT;
		}
		else {
			return Direction.TOP;
		}
	}
	
	/**
     * Adds a player to the cell.
     *
     * @param player The player to add.
     */
	public void addPlayer(Player player) {
		this.players.add(player);
		player.setCell(this);
	}
	
	public boolean hasFloor() {
		return this.hasFloor;
	}
	
	/**
     * Removes a player from the cell.
     *
     * @param player The player to remove.
     */
	public void removePlayer(Player player) {
		this.players.remove(player);
	}
	
	/**
     * Retrieves the list of players in the cell.
     *
     * @return The list of players.
     */
	public List<Player> getPlayers() {
		return this.players;
	}
	
	/**
     * Retrieves the number of players in the cell.
     *
     * @return The number of players.
     */
	public int getNbPlayers() {
		return this.players.size();
	}
	
	/**
     * Adds a zombie to the cell.
     *
     * @param zombie The zombie to add.
     */
	public void addZombie(Zombie zombie) {
		this.zombies.add(zombie);
		zombie.setCell(this);
	}
	
	/**
     * Removes a zombie from the cell.
     *
     * @param zombie The zombie to remove.
     */
	public void removeZombie(Zombie zombie) {
		this.zombies.remove(zombie);
	}
	
	/**
     * Retrieves the list of zombies in the cell.
     *
     * @return The list of zombies.
     */
	public List<Zombie> getZombies() {
		return this.zombies;
	}
	
	/**
     * Retrieves the number of zombies in the cell.
     *
     * @return The number of zombies.
     */
	public int getNbZombies() {
		return this.zombies.size();
	}
	
	/**
     * Sets whether the cell is a spawner or not.
     *
     * @param isSpawner Whether the cell is a spawner.
     */
	public void setIsSpawner(boolean isSpawner) {
		this.isSpawner = isSpawner;
	}
	
	/**
     * Retrieves whether the cell is a spawner or not.
     *
     * @return Whether the cell is a spawner.
     */
	public boolean getIsSpawner() {
		return this.isSpawner;
	}
	
	/**
     * Sets the position of the cell on the board.
     *
     * @param width The width position.
     * @param height The height position.
     */
	public void setPosition(int width , int height) {
		this.width = width ;
		this.height = height ;
	}
	
	/**
     * get the width position of the cell on the board.
     *
     * @return The width position.
     */
	public int getwidth() {
		return this.width ;
	}
	
	/**
     * get the height position of the cell on the board.
     *
     * @return The height position.
     */
	public int getheight() {
		return this.height ;
	}
	
	/**
	 * get the number of doors of the room
	 * @return the number of doors
	 */
	public int getNbDoor(){
		return this.doors.size();
	}
	
	/** 
	 * get the door the the direction
	 * @param Direction the direction wanted
	 * @return Door the door of the direction 
	 * */
	private Door getDoor(Direction direction) {
		return this.doors.get(direction);
	}
	
	/**
	 * get the boolean if the door is open or not in the direction
	 * @param direction the direction of the door
	 * @return if the door in the direction is open or not
	 */
	public boolean isDoorOpen(Direction direction){
		if (this.doors.containsKey(direction)){
			return this.getDoor(direction).isOpen() ;
		}
		return true;
	}

	/** 
	 * add an item to the floor
	 * @param the item to add
	 * */
	public void addItem(Item item) {
		if (this.hasFloor) {
			List<Item> floor = this.floor ;
			floor.add(item) ;
		}	
	}
	
	/** 
	 * get the floor and the item on it 
	 * @return List<item> the floor 
	 * */
	public List<Item> getFloor() {
		return this.floor ;
	}
	
	/**
	 * say if you can attack in the room
	 * @return if you can attack
	 */
	public boolean getCanAttack() {
		return canAttack;
	}
	
	/** 
	 * display the floor to the player with the item and return a boolean if there is or no object in it
	 * @return boolean true if there is a floor false if not
	 * */
	public boolean displayFloor() {
		if (this.hasFloor) {
			List<Item> floor = this.getFloor() ;
			if ( floor.size() == 0) {
				return false ;
			}
			else {
				for (int i = 0 ; i < floor.size() ; i ++ ) {
					Item item = floor.get(i) ;
					System.out.println(item.toString() + " est a l'emplacement "+ i ) ;
				}
				return true ;
			}
		}
		else {
			return false ;
		} 
	}
	
	/** 
	 * add one level of noise 
	 * */
	public void addNoise() {
		this.noise++;
	}
	
	/** 
	 * reset the level of noise 
	 * */
	public void resetNoise() {
		this.noise = 0;
	}
	
	/**
	 * get the noise of the cell
	 * @return the noise in the cell
	 */
	public int getNoise(){
		return this.noise;
	}
	
	/**
	 * return the board where the cell is in
	 * @return Board the board where the cell is in
	 */
	public Board getBoard() {
		return this.board;
	}
	
	/**
     * Abstract method to convert the cell to its character representation.
     *
     * @return The character representation of the cell.
     */
	public abstract char toCharacter();
	 
}
