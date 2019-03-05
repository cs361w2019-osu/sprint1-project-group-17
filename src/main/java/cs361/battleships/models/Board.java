package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Board {

	@JsonProperty private List<Ship> ships;
	@JsonProperty private List<Result> attacks;
	@JsonProperty private List<Result> blocks;
	@JsonProperty private List<Result> sonars;
	@JsonProperty private List<Square> movedSquares;
	// used by js for UI.
	@JsonProperty private int lastAttack;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		ships = new ArrayList<>();
		attacks = new ArrayList<>();
		blocks = new ArrayList<>();
		sonars = new ArrayList<>();
		movedSquares = new ArrayList<>();
		lastAttack = 0;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		// if more than 4 ships placed then cant place ship
	    if (ships.size() >= 4) {
			return false;
		}
		if (ships.stream().anyMatch(s -> s.getKind().equals(ship.getKind()))) {
			return false;
		}
		final var placedShip = new Ship(ship.getKind());
		placedShip.place(y, x, isVertical);
		if (ships.stream().anyMatch(s -> s.overlaps(placedShip))) {
			return false;
		}
		if (placedShip.getOccupiedSquares().stream().anyMatch(s -> s.isOutOfBounds())) {
			return false;
		}
		ships.add(placedShip);
		return true;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		Result attackResult = attack(new Square(x, y));
		if (attackResult.getResult() == AtackStatus.BLOCKED) {
			blocks.add(attackResult);
			lastAttack = 1;
		} else {
			attacks.add(attackResult);
			lastAttack = 0;
		}
		return attackResult;
	}

	private Result attack(Square s) {
		if (attacks.stream().anyMatch(r -> r.getLocation().equals(s))) {
			var attackResult = new Result(s);
			attackResult.setResult(AtackStatus.MISS);
			return attackResult;
		}
		var shipsAtLocation = ships.stream().filter(ship -> ship.isAtLocation(s)).collect(Collectors.toList());
		if (shipsAtLocation.size() == 0) {
			var attackResult = new Result(s);
			return attackResult;
		}
		var hitShip = shipsAtLocation.get(0);
		var attackResult = hitShip.attack(s.getRow(), s.getColumn());
		if (attackResult.getResult() == AtackStatus.SUNK) {
			if (ships.stream().allMatch(ship -> ship.isSunk())) {
				attackResult.setResult(AtackStatus.SURRENDER);
			}
		}
		return attackResult;
	}

	public void sonarPulse(int x , char y){
		Square s = new Square(x,y);
		if(!s.isOutOfBounds() && !sonars.stream().anyMatch(r -> r.getLocation().equals(s))){
			Result sonarResult = sonarPulseAttack(s);
			sonars.add(sonarResult);
			lastAttack = 2;
		}
	}

	private Result sonarPulseAttack(Square s){
		var shipsAtLocation = ships.stream().filter(ship -> ship.isAtLocation(s)).collect(Collectors.toList());
		if (shipsAtLocation.size() == 0) {
			var sonarResult = new Result(s);
			return sonarResult;
		}
		var hitShip = shipsAtLocation.get(0);
		var sonarResult = hitShip.sonarAttack(s.getRow(), s.getColumn());
		return sonarResult;
	}

	public boolean move(int dir){
		ships.stream().forEach(s->s.move(dir));
		if(overlap())
			return false;
		movedSquares = new ArrayList<>();
		return true;
	}

	public boolean overlap(){
		for(Ship s: ships){
			s.getOccupiedSquares().forEach(q-> movedSquares.add(q));
		}
		int count;
		for(Ship s: ships){
			count = 0;
			for(Square q: s.getOccupiedSquares()){
				for(Square mq: movedSquares){
					if(q.equals(mq))
						count++;
				}
			}
			if(count > s.getSize())
				return true;
		}
		return false;
	}

	List<Ship> getShips() {
		return ships;
	}
}
