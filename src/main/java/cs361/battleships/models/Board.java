package cs361.battleships.models;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Board {
	 private AtackStatus atackstatus;
	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	private List<Ship> ships;
	private List<Result> attacks;

	public Board() {
		this.ships = new ArrayList<Ship>();
		this.attacks = new ArrayList<Result>();
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
}
