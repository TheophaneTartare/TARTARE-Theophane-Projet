package zombicide.util;

import zombicide.Direction;

public class GetDirection {
	
	public static  Direction getDirection(){
	
		Direction direction;
		do {
			System.out.print(" give a direction : ? Possible choice N(nord),E(est),S(sud),O(ouest)");
			direction = stringToDirection(Input.readString());
		}while(direction==null);
		
		return direction;
	}	

	private static Direction stringToDirection(String string) {
		switch(string) {
			case "N":
				return Direction.TOP;
				
			case "S":
				return Direction.BOTTOM;
				
			case "O":
				return Direction.LEFT;
			case "E":
				return Direction.RIGHT;
			default:
				System.out.print("Not a direction");
				return null;
				
		}
	}
}
