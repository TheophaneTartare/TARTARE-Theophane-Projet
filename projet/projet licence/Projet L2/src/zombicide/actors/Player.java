package zombicide.actors;


import java.util.ArrayList;
import java.util.List;
import zombicide.Actor;
import zombicide.Board;
import zombicide.Cell;
import zombicide.Direction;
import zombicide.Item;

public abstract class Player extends Actor{ 
	/*Item on the hand of the player*/
	protected Item onHand;
	/*inventory of the player*/
	protected List<Item> inventory;
	/*Level of the player*/
	protected int level;
	/* number of action possible in a turn*/
	private int actionDeBase=3;
	/* if the player a search for free */
	private boolean hasSearch ;
	
	/** 
	 * construct a Player 
	 * @param  the name of the player 
	 * 
	 * */
	public Player(String name) {
		super(name) ;
		this.onHand = null;
		this.inventory = new ArrayList<Item>();
		this.level = 1;
		this.heal = 5;
		this.action = 3;
		this.hasSearch = false ;
	}
	
	/** 
	 * add a item to the inventory regardless of the number of Item
	 * @param item the Item to add 
	 * */
	public void addItemInventory(Item item) {
		item.newHolder(this);
		List<Item> inventory = this.inventory ;
		inventory.add(item) ;
	}
	
	/** 
	 * Remove a Item of the inventory a put them on the ground ( if possible ) and delete them 
	 * from the hand if it was in the hand of the player 
	 * @param pos the index of the Item to remove 
	 * 
	 * */
	public void removeInventory(int pos) {
		List<Item> inventory = this.inventory ;
		Cell playerCell = this.cell ;
		Item itemDroped = inventory.get(pos);
		Item hand = this.onHand ;
		if ( itemDroped.toString() == hand.toString() ) {
			this.supHand();
		}
		
		inventory.remove(pos) ; 
		itemDroped.removeHolder() ;
		playerCell.addItem(itemDroped);
		this.cell = playerCell ;
	}
	
	
	/** 
	 * give the Item in the hand return null if there is not 
	 * 
	 * @return Item the Item in the hand 
	 * */
	public Item getHand() {
		return this.onHand ;
	}
	
	/**
	 * the item on hand change
	 * @param item the item who will be on hand 
	 */
	public void SetHand(Item item) {
		this.onHand=item;
	}
	
	/** 
	 * put back the Item in the hand in the backpack
	 * 
	 * */
	public void supHand() {
		this.onHand = null ;
	}
	
	/**
	 * reduce the number of action possible in one turn
	 * @param malus the number who will be taken of the action point
	 */
	public void MinusActionPoint(int malus) {
		this.action -= malus ; 
	}
	
	
	/** 
	 * get the List of Item in the inventory 
	 * 
	 * @return List<Item> the List of Item in the inventory 
	 * */
	public List<Item> getInventory(){
		return this.inventory ;
	}
	
	/**
	 * change the inventory in the one given in parameter 
	 * @param inventory the new inventory 
	 */
	public void setInventory(List<Item> inventory) {
		this.inventory = inventory ;
	}
	
	/** 
	 * Display the inventory to the user 
	 * */
	public void displayInventory() {
		System.out.println("Inventaire de "+this.name);
		for (Item item : this.inventory) {
			if(item != null) {
				  System.out.println(this.inventory.indexOf(item)+" - "+item.toString());
			}
		}
		if(this.onHand!=null) {
			System.out.println("à dans la main : "+this.onHand.toString());
		}
	}
	
	/**
	 * display the status of the player
	 */
	public void displayStatus() {
		System.out.print(" \n nom : "+ this.name + " point de vie : " + this.heal + " status : " + this.status());
	}
	
	/**
	 * say if the player is dead or alive 
	 * @return say if the player is dead or alive 
	 */
	public String status() {
		if (this.heal <= 0) {
			return "mort" ;
		}
		else { return "vivant"  ;} 
	} 
	
	/** 
	 * add level(s) to the player 
	 * @param nbLevel the number of level to add 
	 * */
	public void addLevel(int nbLevel) {
		this.level += nbLevel;
	}
	
	/**
	 * get the level of the player
	 * @return int the level of the player 
	 */
	public int getLevel() {
		return this.level;
	}
	
	/** 
	 * heal the player 
	 * @param nbHeal the health to restore
	 * */
	public void addHeal(int nbHeal) {
		this.heal += nbHeal;
	}

	/**
	 * reset the action and take in acount the level 
	 */
	public void resetAction() {
		if(this.level<3) {
			this.action=this.actionDeBase;
		}
		else if(this.level<7) {
			this.action=this.actionDeBase+1;
		}
		else if(this.level<11){
			this.action=this.actionDeBase+2;
		}
		else {
			this.action=this.actionDeBase+3;
		}
	}
	
	/**
	 * drop all the inventory on the floor of the cell of the player
	 */
	public void dropBag() {
		if(cell.hasFloor()) {
			for (Item item : this.inventory) {
				cell.addItem(item);
			}
		}
		
	}
	
	/**
	 * get the name of the player 
	 * @return String the name of the player 
	 */
	public String toString() {
		return this.name;
	}
	
	/**
	 * return if the player has use his free search 
	 * @return boolean true if the player has use his free search 
	 */
	public boolean getHasSearch() {
		return this.hasSearch ;
	}
	
	/**
	 * change hasSeach in true 
	 */
	public void trueHasSearch() {
		 this.hasSearch = true;
	}
	
	/**
	 * change hasSeach in false
	 */
	public void falseHasSearch() {
		 this.hasSearch = false;
	}
	
	/**
	 * Abstract method who return a character different with each class 
	 * @return the character 
	 */
	public abstract char toCharacter() ;
	

	/** 
	 * get the list of zombies in a range 
	 * @param range the range to get zombies
	 * @return List<Zombies> the list of zombies in a range 
	 * */
    public List<Zombie> getZombiesInRange(int range){

    	// initialise la liste des zombies
        List<Zombie> zombies = new ArrayList<Zombie>();

        // ajoute les zombies sur la même cellule du joueur
        zombies.addAll(this.getCell().getZombies());

        // récupère les coordonnées X et Y du joueur
        int playerX = this.getWidthPosition();
        int playerY = this.getHeightPosition();

        // récupère le board
        Board board = this.getCell().getBoard();

        // initialise les directions (exemple: dx = -1 et dy = 0 correspond au bottom)
        Direction[] directions = {Direction.BOTTOM, Direction.RIGHT, Direction.TOP, Direction.LEFT};
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, -1, 0, 1};
        
        for(int i = 0; i < 4; i++){
        	for(int j = 1; j <= range; j++){
        		int newPlayerX = playerX + dx[i] * j;
        		int newPlayerY = playerY + dy[i] * j;
        		
        		if(newPlayerX >= 0 && newPlayerX < board.getwidth() && newPlayerY >= 0 && newPlayerY < board.getHeight()){
        			Cell cell = board.getCell(newPlayerX, newPlayerY);
        			if(cell.isDoorOpen(directions[i])) {
        				zombies.addAll(cell.getZombies());
        			}
        		}
        	}
        }

        return zombies;
    }
	
}
