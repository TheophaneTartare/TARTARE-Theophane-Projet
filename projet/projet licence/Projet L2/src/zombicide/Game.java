package zombicide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import zombicide.action.Attack;
import zombicide.action.Heal;
import zombicide.action.LookAround;
import zombicide.action.MakeNoise;
import zombicide.action.Move;
import zombicide.action.OpenDoor;
import zombicide.action.PassTurn;
import zombicide.action.Search;
import zombicide.action.TakeOnHand;
import zombicide.action.Use;
import zombicide.actors.Player;
import zombicide.actors.Zombie;
import zombicide.items.AidKit;
import zombicide.items.Flask;
import zombicide.items.InfradedScope;
import zombicide.items.Map;
import zombicide.items.MasterKey;
import zombicide.items.weapons.Axe;
import zombicide.items.weapons.ChainSaw;
import zombicide.items.weapons.CrowBar;
import zombicide.items.weapons.Pistol;
import zombicide.items.weapons.Rifle;
import zombicide.util.Input;
import zombicide.zombies.Abomination;
import zombicide.zombies.Runner;
import zombicide.zombies.Tank;
import zombicide.zombies.Walker;

/**
 * Represents a game of Zombicide.
 */
public class Game {
	
	/*The list of players in the game.*/
	private List<Player> players;
	/*The list of zombies in the game.*/
	private List<Zombie> zombies;
	/*The game board.*/
	private Board board;
	/*The list of reference item.*/
	private List<Item> itemsReference ;
	/*The list of possible action.*/
	private List<Action> listeAction;
	/*Indicates if it's the first round of the game.*/
	private boolean firstRound=true;

	/**
     * Constructs a game with the specified game board.
     *
     * @param board The game board.
     */
	public Game(Board board) {
		this.players = new ArrayList<Player>();
		this.zombies = new ArrayList<Zombie>();
		this.itemsReference = new ArrayList<Item>();
		this.board = board;
		this.initItemsReference();
		this.initListeAction();
	}
	
	/**
	 * return the list of players currently in the game.
	 *
	 * @return The list of players.
	 */
	public List<Player>getPlayers(){
		return this.players;
	}
	
	/**
     * Adds a player to the game and places it on the game board.
     *
     * @param player The player to add.
     */
	public void addPlayer(Player player , int i , int j) {
		this.players.add(player);
		this.board.addPlayer(player, i, j);
	}
	
	/**
     * Adds a zombie to the game and places it on the game board.
     *
     * @param zombie The zombie to add.
     */
	public void addZombie(Zombie zombie , int i ,int j) {
		this.zombies.add(zombie);
		this.board.addZombie(zombie, i, j);
	}
	
	
	/**
     * Displays the game board.
     */
	public void displayBoard() {
		this.board.display();
	}
	
	/**
	 * launch the game 
	 * */
	public void play() throws IOException {
		this.initGame();
		while((this.players.size()!=0)&&(zombies.size()!=0||this.firstRound) && this.sumExpPlayer()<30) {
			this.playerPhase();
			this.zombiePhase();
			this.endPhase();
			this.firstRound=false;
		}
		this.gameEnd();
	}
	
	/*
	 * launch the game with the player in auto-pilote 
	 */
	public void randomPlay() throws IOException {
		this.initGame();
		while((this.players.size()!=0)&&(zombies.size()!=0||this.firstRound) && this.sumExpPlayer()<30) {
			this.randomPlayerPhase();
			this.zombiePhase();
			this.endPhase();
			this.firstRound=false;
		}
		this.gameEnd();
	}
	
	
	/**
	 * Determines the end state of the game and prints the appropriate message
	 * "GAME WIN" or "GAME OVER"
	 */
	private void gameEnd() {
		if((this.players.size()==0)) {
			System.out.print("GAME OVER . \n");
		}
		else {
			System.out.print("GAME WIN . \n");
		}
	}
	
	/**
	 * Do necessary actions for the end phase of the game.
	 */
	private void endPhase() {
		System.out.print("end phase . \n");
		this.suppDiePlayer();
		this.board.resetNoise();
		this.addNewZombie();
		this.resetAction();
	}
	
	/**
	 * Resets the actions available to players and zombies.
	 */
	private void resetAction() {
		for (Player player : this.players) {
			player.resetAction();
		}
		for(Zombie zombie :this.zombies) {
			zombie.resetAction();
		}
	}
	
	/**
	 * Removes dead players from the game .
	 */
	private void suppDiePlayer() {
		Iterator<Player> iterator = this.players.iterator();
		while (iterator.hasNext()) {
	        Player player = iterator.next();
	        if (player.getHeal() <= 0) {
	        	player.dropBag();
	            iterator.remove();
	            Cell playerCell=player.getCell();
	            playerCell.removePlayer(player);
	        }
	    }
	}
	
	/**
	 * Removes dead zombies from the game .
	 */
	private void suppDieZombie() {
		Iterator<Zombie> iterator = this.zombies.iterator();
		while (iterator.hasNext()) {
	        Zombie zombie = iterator.next();
	        if (zombie.getHeal() <= 0) {
	        	Cell cell = zombie.getCell();
	        	cell.removeZombie(zombie);
	            iterator.remove();
	        }
	    }
	}
	
	/**
	 * Adds new zombies to the game.
	 */
	private void addNewZombie() {
		if(this.zombies.size()>0||this.firstRound) {
			int nbZombie=this.calculNbOfZombie();
			if(nbZombie!=-1) {
				for (Cell sewer : board.getSewers()) {
					for(int i=0;i<nbZombie;i++) {
						Zombie zombie= this.randomZombie() ;
						this.board.addZombie(zombie, sewer.getwidth(),sewer.getheight() );
						this.zombies.add(zombie);
					}
				}
			}
		}
	}
	
	/**
	 * chose randomly a type of zombie and create it
	 * @return the zombie created
	 */
	private Zombie randomZombie() {
		Zombie zombie ;
		Random r = new Random() ;
		int randomNumber = r.nextInt(100) + 1;
		if (randomNumber < 65) {
			zombie = new Walker("walker") ;
			
		}
		else if (randomNumber < 91 ) {
			zombie = new Runner("runner");
		}
		else if (randomNumber < 98) {
			zombie = new Tank("tank") ;
		}
		else {
			zombie = new Abomination("abomination") ;
		}
		return zombie ;
	}
	
	/**
	 * Calculates the number of zombies to be added based on player experience.
	 * 
	 * @return The number of zombies to add, or -1 if there are no players.
	 */
	private int calculNbOfZombie() {
		if(this.players.size()==0) {
			return -1;
		}
		double moyenne=this.sumExpPlayer()/this.players.size();
		
		return (int) Math.ceil(moyenne/3);
	}
	
	
	/**
	 * manage the item at the start of the game 
	 * */
	private void initGame() {
		this.startStuffPlayers();
		this.initRandomStartItem();
		
	}
	
	/**
	 * give a gun in the hand of all the player 
	 *  */
	private void startStuffPlayers() {
		for (Player player : this.players) {
			Item pistol=new Pistol();
			player.addItemInventory(pistol);
			
			List<Item> inventory = player.getInventory();
			inventory.add(player.getHand()) ;
			
			player.SetHand(inventory.get(0));
			inventory.remove(0);
			/* Permet de supprimer les éléments du tableau si l'objet est null pour fludifier l'affichage */
			inventory.removeIf(Objects::isNull);
			
			player.setInventory(inventory);
			
        }
	}
	
	
	
	/**
	 * add a random number of item to the board 
	 * */
	private void initRandomStartItem() {
		Random r = new Random() ;
		int random = r.nextInt(101) ; 
		for (int i = 0 ; i < random ; i++) {
			this.addRandomItemInRandomCell() ;
		}
		
	}
	
	/**
	 * choose in a random maner the item to add 
	 * */
	private void addRandomItemInRandomCell() {
		Random r = new Random();
		int random = r.nextInt(this.itemsReference.size()) ; 
		Item itemReference = this.itemsReference.get(random) ;
		Item itemToAddInGame = itemReference.createHomologue() ; 
		this.addInRandomCell(itemToAddInGame) ; 
	}
	
	/** take the item , choose a random cell of the board and add the item on the floor 
	 * @param item : the item to add randomly 
	 * */
	private void addInRandomCell(Item item) {
		Random r = new Random() ;
		List<Cell> rooms = this.board.getListRoom() ;
		Cell cell = rooms.get( r.nextInt(rooms.size())) ;
		int randomWidth = cell.getwidth() ;
		int randomHeight = cell.getheight() ;
		this.board.addOneItem(item, randomWidth, randomHeight); 
	}
	
	/**
	 * Calculates the sum of experience levels of all players.
	 * 
	 * @return The sum of experience levels.
	 */
	private int sumExpPlayer() {
		int sum=0;
		for (Player player : this.players) {
			sum+=player.getLevel();
		}
		return sum;
	}
	
	/** 
	 * make all the players play their turn 
	 * */
	public void playerPhase() {
		System.out.print("Player phase . \n");
		for (Player player : this.players) {
			if(player.toCharacter() == 'N') {
				player.falseHasSearch();
			}
			while(player.getAction()>0) {
				this.suppDieZombie();
				System.out.print("joueur : " + player.toString());
			    System.out.print(" ,action restante : "+player.getAction() + "\n");
				this.chooseAction(player);
			}
		}
	}
	
	/**
	 * make all the players play their turn 
	 */
	private void randomPlayerPhase() {
		System.out.print("Player phase . \n");
		for (Player player : this.players) {
			if(player.toCharacter() == 'N') {
				player.falseHasSearch();
			}
			while(player.getAction()>0) {
				this.suppDieZombie();
				System.out.print("joueur : " + player.toString());
			    System.out.print(" ,action restante : "+player.getAction() + "\n");
				this.chooseRandomAction(player);
			}
		}
	}
	
	
	/** 
	 * make all the zombies play their turn 
	 * */
	public void zombiePhase() {
		System.out.print("Zombie phase . \n");
		for (Zombie zombie : this.zombies) {
			while(zombie.getAction()>0 && zombie.getHeal()>0) {
				this.zombieAction(zombie);
			}
		}
	}
	
	/**
	 * add all the Action wanted in the game 
	 * */
	private void initListeAction() {
		this.listeAction=new ArrayList<Action>();
		Attack attack=new Attack();
		LookAround LookAround=new LookAround();
		MakeNoise MakeNoise=new MakeNoise();
		Move Move=new Move();
		OpenDoor OpenDoor=new OpenDoor();
		Search Search=new Search();
		TakeOnHand TakeOnHand=new TakeOnHand();
		Use Use=new Use();
		PassTurn pass = new PassTurn() ;
		
		listeAction.add(attack);
		listeAction.add(LookAround);
		listeAction.add(MakeNoise);
		listeAction.add(Move);
		listeAction.add(OpenDoor);
		listeAction.add(Search);
		listeAction.add(TakeOnHand);
		listeAction.add(Use);
		listeAction.add(pass);
	}
	
	/** 
	 * add all the Item wanted in the game 
	 * */
	private void initItemsReference() {
		this.itemsReference=new ArrayList<Item>();
		AidKit AidKit=new AidKit();
		Flask Flask=new Flask();
		InfradedScope InfradedScope=new InfradedScope(this.board);
		Map Map=new Map(this.board);
		MasterKey MasterKey=new MasterKey();
		Axe Axe=new Axe();
		ChainSaw ChainSaw=new ChainSaw();
		CrowBar CrowBar=new CrowBar();
		Pistol Pistol=new Pistol();
		Rifle Rifle=new Rifle();
		
		itemsReference.add(AidKit);
		itemsReference.add(Flask);
		itemsReference.add(InfradedScope);
		itemsReference.add(Map);
		itemsReference.add(MasterKey);
		itemsReference.add(Axe);
		itemsReference.add(ChainSaw);
		itemsReference.add(CrowBar);
		itemsReference.add(Pistol);
		itemsReference.add(Rifle);
	}
	
	
	/**  chose the action the player will do 
	 * @param currentPlayer the Player who act 
	 * */
	public void chooseAction(Player currentPlayer){
		if(currentPlayer.toCharacter() == 'H') {
			this.addActionHealToListAction();
		}
		this.displayListAction(currentPlayer);
		
		try {
		int indiceChoisie=Input.readInt();
		Action action=listeAction.get(indiceChoisie);
		action.doAction(currentPlayer,null, this.board, this,false);
		this.suppActionHealToListAction();
		}
		catch(IOException |IndexOutOfBoundsException e){
			System.out.print("\n give an int :");
			this.chooseAction(currentPlayer);
		}
		
	}
	
	/**
	 * while randomly chose an action
	 * @param currentPlayer the player who play
	 */
	public void chooseRandomAction(Player currentPlayer){
		if(currentPlayer.toCharacter() == 'H') {
			this.addActionHealToListAction();
		}
		Random r = new Random() ;
		int indiceChoisie = r.nextInt(this.listeAction.size()) ;
		Action action=listeAction.get(indiceChoisie);
		System.out.print("Le joueur "+currentPlayer.getName()+" fait l'action "+action.toString()+"\n");
		action.doAction(currentPlayer,null, this.board, this,true);
		this.suppActionHealToListAction();
	}
	
	/**
	 * Adds a healing action to the list of actions.
	 */
	private void addActionHealToListAction() {
		Heal heal=new Heal();
		listeAction.add(heal);
	}
	
	/**
	 * remove a healing action to the list of actions.
	 */
	private void suppActionHealToListAction() {
		Iterator<Action> iterator = listeAction.iterator();
        while (iterator.hasNext()) {
            Action action = iterator.next();
            if (action instanceof Heal) {
                iterator.remove();
            }
        }
	}
	
	/**
	 * Display the list of actions.
	 */
	private void displayListAction(Player currentPlayer) {
		//affichage de la liste d'action
		System.out.print("Liste des actions : ");
		int cmpt=0;
		for (Action action : listeAction) {
		    System.out.print(action.getClass().getSimpleName() +" "+cmpt+" - ");
		    cmpt++;
		}
		System.out.print("\nChoose Action :\n");
	}
	
	/**  chose the action the player will do 
	 * @param currentPlayer the Player who act 
	 * */
	private void zombieAction(Zombie zombie) {
		List<Player> havePlayer=this.havePlayerInCell(zombie);
		if(havePlayer!=null) {
			Player target=this.chooseTarget(havePlayer);
			Attack attack=new Attack();
			attack.doAction(zombie, target, board , this,false);
		}
		else {
			Move move=new Move();
			move.doAction(zombie, null, board , this,false);
		}
	}
	
	/** give the player in the same cell as the zombie 
	 * @param zombie said zombie 
	 * @return the player ont the cell
	 * */
	private List<Player> havePlayerInCell(Zombie zombie) {
		Cell zombieCell=zombie.getCell();
		List<Player> listePlayer=zombieCell.getPlayers();
		if(listePlayer.size()>0) {
			return listePlayer;
		}
		else {
			return null;
		}
	}
	
	/** choose a target in the list 
	 * @param listePlayer the potential victim
	 * @return the victim
	 *  */
	private Player chooseTarget(List<Player> listePlayer) {
		Random random = new Random();
		int i = random.nextInt(listePlayer.size());
		return listePlayer.get(i);
	}
	
	/** 
	 * display the status of all the player 
	 * */
	public void displayPlayersStatus() {
		for (Player player : this.players) {
			player.displayStatus() ;
		}
	}
	
	
	
	
	

}
