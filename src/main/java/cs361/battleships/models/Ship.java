package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import com.mchange.v1.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Ship {

	@JsonProperty private String kind;
	@JsonProperty private List<Square> occupiedSquares;
	@JsonProperty private int size;

	public Ship() {
		occupiedSquares = new ArrayList<>();
	}
	
	public Ship(String kind) {
		this();
		this.kind = kind;
		switch(kind) {
			case "MINESWEEPER":
				size = 2;
				break;
			case "DESTROYER":
				size = 3;
				break;
			case "BATTLESHIP":
				size = 4;
				break;
			case "SUBMARINE":
				size = 5;
				break;
		}
	}

	public List<Square> getOccupiedSquares() {
		return occupiedSquares;
	}

	public void place(char col, int row, boolean isVertical) {
		for (int i=0; i<size; i++) {
			if (isVertical) {
				occupiedSquares.add(new Square(row+i, col));

			} else {
				occupiedSquares.add(new Square(row, (char) (col + i)));
			}
		}
		occupiedSquares.get((size-2)).cap();
	}

	public boolean overlaps(Ship other) {
		Set<Square> thisSquares = Set.copyOf(getOccupiedSquares());
		Set<Square> otherSquares = Set.copyOf(other.getOccupiedSquares());
		Sets.SetView<Square> intersection = Sets.intersection(thisSquares, otherSquares);
		return intersection.size() != 0;
	}

	public boolean isAtLocation(Square location) {
		return getOccupiedSquares().stream().anyMatch(s -> s.equals(location));
	}

	public String getKind() {
		return kind;
	}

	public Result attack(int x, char y) {
		var attackedLocation = new Square(x, y);
		var square = getOccupiedSquares().stream().filter(s -> s.equals(attackedLocation)).findFirst();
		if (!square.isPresent()) {
			return new Result(attackedLocation);
		}
		var attackedSquare = square.get();

		if(attackedSquare.isCap() && this.getKind().equals("MINESWEEPER")){
			attackedSquare.hit();
			attackedSquare.capHit();
		}

		else if (attackedSquare.isCap() && !attackedSquare.isCapHit()){
			attackedSquare.capHit();
			var result = new Result(attackedLocation);
			result.setResult(AtackStatus.BLOCKED);
			return result;
		}

		else if (attackedSquare.isHit()) {
			var result = new Result(attackedLocation);
			result.setResult(AtackStatus.INVALID);
			return result;
		}
		attackedSquare.hit();
		var result = new Result(attackedLocation);
		result.setShip(this);
		if (isSunk()) {
			result.setResult(AtackStatus.SUNK);
		}
		var attackedSquare = square.get();
		if (attackedSquare.isCap()){
			return new Result(attackedLocation);
		} else {
			result.setResult(AtackStatus.HIT);
		}
		return result;
	}

	public Result sonarAttack(int x ,char y){
		var attackedLocation = new Square(x, y);
		var square = getOccupiedSquares().stream().filter(s -> s.equals(attackedLocation)).findFirst();
		if (!square.isPresent()) {
			return new Result(attackedLocation);
		}
		Result result = new Result(attackedSquare);
		result.setResult(AtackStatus.HIT);
		return result;
	}

	@JsonIgnore
	public boolean isSunk() {
		if (getOccupiedSquares().stream().allMatch(s -> s.isHit()))
			return true;
		if (getOccupiedSquares().stream().anyMatch((s -> (s.isHit() && s.isCapHit()))))
			return true;
		return false;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Ship)) {
			return false;
		}
		var otherShip = (Ship) other;

		return this.kind.equals(otherShip.kind)
				&& this.size == otherShip.size
				&& this.occupiedSquares.equals(otherShip.occupiedSquares);
	}

	@Override
	public int hashCode() {
		return 33 * kind.hashCode() + 23 * size + 17 * occupiedSquares.hashCode();
	}

	@Override
	public String toString() {
		return kind + occupiedSquares.toString();
	}
}
