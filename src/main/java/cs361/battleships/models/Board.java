package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
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

		if(placedShip.getKind().equals("SUBMARINE")){
			if(isSubUnder(placedShip)) {
				String nameOfShip = "SUBMARINE";
				for (Square s : placedShip.getOccupiedSquares()) {
					var shipsAtLocation = ships.stream().filter(p -> p.isAtLocation(s)).collect(Collectors.toList());
					if(shipsAtLocation.size() == 1){
						if (nameOfShip.equals("SUBMARINE")){
							nameOfShip = shipsAtLocation.get(0).getKind();
						} else {
							if(!shipsAtLocation.get(0).getKind().equals(nameOfShip)){
								return false;
							}
						}
					}
				}
			}
		}

		if (!placedShip.getKind().equals("SUBMARINE") && ships.stream().anyMatch(s -> s.overlaps(placedShip) && !s.getKind().equals("SUBMARINE"))) {
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
		boolean laserWeapon = ships.stream().anyMatch(ship -> ship.isSunk());
		var shipsAtLocation = ships.stream().filter(ship -> ship.isAtLocation(s)).collect(Collectors.toList());
		int numShips = shipsAtLocation.size();
		var attackResult = new Result(s);

		if (numShips == 0) {
			return attackResult;
		} else if (numShips == 1) {
			var hitship = shipsAtLocation.get(0);
			if (hitship.getKind().equals("SUBMARINE")) {
				// if the sub is submerged and we don't have the laser weapon we miss
				// otherwise we hit the sub
				if (ships.stream().anyMatch(ship -> ship.overlaps(hitship) && !ship.equals(hitship)) && !laserWeapon) {
					return attackResult;
				} else {
					attackResult = hitship.attack(s.getRow(), s.getColumn());
					if (attackResult.getResult() == AtackStatus.SUNK) {
						if (ships.stream().allMatch(ship -> ship.isSunk())) {
							attackResult.setResult(AtackStatus.SURRENDER);
						}
					}
					if (attackResult.getResult() != AtackStatus.INVALID) {
						return attackResult;
					} else {
						attackResult.setResult(AtackStatus.MISS);
						return attackResult;
					}
				}
			} else {
				attackResult = hitship.attack(s.getRow(), s.getColumn());
				if (attackResult.getResult() == AtackStatus.SUNK) {
					if (ships.stream().allMatch(ship -> ship.isSunk())) {
						attackResult.setResult(AtackStatus.SURRENDER);
					}
				}
				if (attackResult.getResult() != AtackStatus.INVALID) {
					return attackResult;
				} else {
					attackResult.setResult(AtackStatus.MISS);
					return attackResult;
				}
			}
		} else if (numShips == 2) {
			var norShip = shipsAtLocation.get(0);
			var subShip = shipsAtLocation.get(1);
			if (norShip.getKind().equals("SUBMARINE")) {
				norShip = shipsAtLocation.get(1);
				subShip = shipsAtLocation.get(0);
			}
			// if we don't have the laser weapon attack only the surface ship
			attackResult = norShip.attack(s.getRow(), s.getColumn());
			if (attackResult.getResult() == AtackStatus.SUNK) {
				if (ships.stream().allMatch(ship -> ship.isSunk())) {
					attackResult.setResult(AtackStatus.SURRENDER);
				}
			}
			// if we have the laser weapon attack the submarine too
			if (laserWeapon) {
				var attackResult2 = subShip.attack(s.getRow(), s.getColumn());
				if (attackResult2.getResult() == AtackStatus.SUNK) {
					if (ships.stream().allMatch(ship -> ship.isSunk())) {
						attackResult2.setResult(AtackStatus.SURRENDER);
					}
				}
				attacks.add(attackResult);
				if (attackResult2.getResult() != AtackStatus.INVALID) {
					return attackResult2;
				} else {
					attackResult2.setResult(AtackStatus.MISS);
					return attackResult2;
				}
			} else {
				if (attackResult.getResult() != AtackStatus.INVALID) {
					return attackResult;
				} else {
					attackResult.setResult(AtackStatus.MISS);
					return attackResult;
				}
			}
		} else {
			attackResult.setResult(AtackStatus.MISS);
			return attackResult;
		}
	}


	public boolean isSubUnder(Ship sub){
		return sub.getOccupiedSquares().stream().anyMatch(s-> ships.stream().filter(ship -> ship.isAtLocation(s)).collect(Collectors.toList()).size() > 0 );
	}

	public boolean isSub(Square s){
		var shipsAtLocation = ships.stream().filter(ship -> ship.isAtLocation(s)).collect(Collectors.toList());
		if(shipsAtLocation.size() != 0){
			var hitShip = shipsAtLocation.get(0);
			if(hitShip.getOccupiedSquares().stream().anyMatch(q -> ships.stream().filter(ship -> ship.isAtLocation(q)).collect(Collectors.toList()).size() >= 2)){
				return true;
			}
		}
		return false;
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
			if(!s.getKind().equals("SUBMARINE")) {
				s.getOccupiedSquares().forEach(q -> movedSquares.add(q));
			}
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
