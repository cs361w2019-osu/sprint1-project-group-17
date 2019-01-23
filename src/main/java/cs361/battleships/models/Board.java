package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;

public class Board {

	private AttackStatus atackstatus;
	private List<Ship> ships;
	private List<Result> attacks;
	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	private List<Ship> ships;
	private List<Result> attacks;

	public Board() {
		this.ships = new ArrayList<>();
		this.attacks = new ArrayList<>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		int MAX = 10;
		int shipSize = ship.getSize();
		int col = charToint(y);

		if (shipSize <= 0 || x>MAX || col<0 || x<0 || col>MAX){
			return false;
		}

		/* check boundary*/
		if (isVertical){
			for(int i=0;i<shipSize;i++){
				if((x+i)>MAX){
					return false;
				}
			}
		}
		else{
			for(int i=0;i<shipSize;i++){
				if((col+i)>MAX){
					return false;
				}
			}
		}
		/*check square empty*/
		List<Ship> existingShips = this.getShips();
		for(Ship exShips : existingShips ){
			for(Square occSqu : exShips.getOccupiedSquares()){
				if(isVertical){
					for(int i=0;i<shipSize;i++){
						int tInt = x + i;
						if(occSqu.getRow() == tInt && occSqu.getColumn() == y){
							return false;
						}
					}
				}
				else{
					for(int i=0;i<shipSize;i++){
						int tInt = col + i;
						if(occSqu.getRow() == x && occSqu.getColumn() == intTochar(tInt)){
							return false;
						}
					}
				}
			}
		}
		/*add occupiedSquares to ship */
		List<Square> occupy = new ArrayList<>();
		if(isVertical){
			for(int i=0;i<shipSize;i++){
				int tInt = x + i;
				occupy.add(new Square(tInt,y));
			}
		}
		else{
			for(int i=0;i<shipSize;i++){
				int tInt = col + i;
				char tChar = intTochar(tInt);
				occupy.add(new Square(x,tChar));
			}
		}

		ship.setOccupiedSquares(occupy);

		return this.ships.add(ship);
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		//TODO Implement
		//Set position to attack
		var pos = new Result();
		pos.setLocation(new Square(x,y));

		//make sure they have valid input (coordinates)
		//return position
		if (x < 1 || x > 10 || y < 'A' || y > 'J'){
			pos.setResult(AttackStatus.INVALID);
			System.out.println("INVALID!");
			return pos;
		}


		return pos;
	}

	// Method checks if all of a player's ships have been sunk
	public boolean checkGameOver() {
		List<Result> pastAttacks = getAttacks();
		int sunkenShips = 0;
		for (Result elem : pastAttacks) {
			if (elem.getResult() == AttackStatus.SUNK) {
				sunkenShips++;
			}
		}
		if (sunkenShips >= 3) {
			return true;
		} else {
			return false;
		}
	}

	public List<Ship> getShips() {
		return this.ships;
	}

	public void setShips(List<Ship> ships) {
		this.ships = ships;
	}

	public List<Result> getAttacks() {
		return this.attacks;
	}

	public void setAttacks(List<Result> attacks) {
		this.attacks = attacks;
	}

	public int charToint(char y){
		int temp = ((int)y);
		temp = temp - 65 + 1;
		return temp;
	}

	public char intTochar(int y){
		y = y + 65 - 1;
		return ((char)y);
	}
}
