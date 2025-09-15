package zombicide;

/**
 * Represents a door on the game board in Zombicide.
 */
public class Door {
	
	/*the status of the door .*/
    private boolean open;

    /**
     * Constructs a door with the specified initial open state.
     *
     * @param open The initial open state of the door.
     */
    public Door(boolean open){
        this.open = open;
    }
    
    /**
     * Checks if the door is open.
     *
     * @return true if the door is open, false otherwise.
     */
    public boolean isOpen(){
        return this.open;
    }

    /**
     * Sets the open state of the door.
     *
     * @param newOpen The new open state of the door.
     */
    public void setOpen(boolean newOpen){
        this.open = newOpen;
    }
}