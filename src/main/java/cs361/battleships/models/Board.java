package cs361.battleships.models;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Board {
	 private AtackStatus atackstatus;
	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		// TODO Implement
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		// TODO Implement
		return false;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		//TODO Implement
		atackstatus = attack(x,y).getResult();
		switch (atackstatus){
			case MISS:
				System.out.println("MISS!");
			case HIT:
				System.out.println("HIT!");
			case SUNK:
				System.out.println("SUNK!");
			case SURRENDER:
				System.out.println("SURRENDER!");
			case INVALID:
				System.out.println("INVALID!");
		}
		return null;
	}

	public List<Ship> getShips() {
		//TODO implement
		return null;
	}

	public void setShips(List<Ship> ships) {
		//TODO implement
	}

	public List<Result> getAttacks() {
		//TODO implement
		return null;
	}

	public void setAttacks(List<Result> attacks) {
		//TODO implement
	}
}
