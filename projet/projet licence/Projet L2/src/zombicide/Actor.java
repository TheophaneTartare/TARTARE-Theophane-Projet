package zombicide;

public abstract class Actor {
	/* Name of the actor*/
	protected String name ; 
	/* life point of the actor*/
	protected int heal;
	/* action point of the actor*/
	protected int action;
	/* width position on the board*/
	protected int widthPosition;
	/* height position on the board*/
	protected int heightPosition;
	/*Cell where actor is*/
	protected Cell cell;
	
	
	/** 
	 * create an Actor
	 * @param name the name of the Actor 
	 * 
	 * */
	public Actor(String name) {
		this.name = name ;
	}
	
	
	/** 
	 * get the name of the Actor 
	 * @param String the name of the Actor
	 * */
	public String getName() {
		return this.name ;
	}
	/** 
	 * get the health of the actor 
	 * @return int the health remaining 
	 * */ 
	public int getHeal() {
		return this.heal;
	}
	
	/** 
	 * set the position on the board of the player 
	 * @param heightPosition the height
	 * @param widthPosition the width
	 *  
	 * */
	public void setPosition(int widthPosition,int heightPosition) {
		this.widthPosition=widthPosition;
		this.heightPosition=heightPosition;
	}
	
	/** 
	 * get the number of remaining number of Action 
	 * */
	public int getAction() {
		return this.action;
	}
	
	/** 
	 * set the cell in wich the actor is in
	 * @param Cell the concerned cell
	 * */
	public void setCell(Cell cell) {
		this.cell = cell ;
	}
	
	/** 
	 * get the width of the cell 
	 * @return int the width 
	 * */
	public int getWidthPosition() {
		return this.widthPosition;
	}
	
	/** 
	 * get the height of the cell 
	 * @return int the height 
	 * */ 
	public int getHeightPosition() {
		return this.heightPosition;
	}
	
	/** 
	 * take 1 action point 
	 * */
	public void minusAction() {
		this.action = this.action-1 ; 
	}
	
	/** 
	 * get the cell of the player 
	 * @return Cell the cell of the player 
	 * */ 
	public Cell getCell () {
		return this.cell;
	}
	
	/**
	 * make the Actor lose the health when hit 
	 * @param damage the damage taken
	 */
	public void suppHeal(int damage) {
		this.heal=this.heal-damage;
	}
	
	/**
	 * reset the number of action
	 */
	public abstract void resetAction() ;
	
	/**
	 * Abstract method who return a character different with each class 
	 * @return the character 
	 */
	public abstract char toCharacter() ;
	
}
