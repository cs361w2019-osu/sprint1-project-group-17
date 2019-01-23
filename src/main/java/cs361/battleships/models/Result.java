package cs361.battleships.models;

public class Result {

	private AttackStatus result;
	private Ship ship;
	private Square location;

	public AttackStatus getResult() {
		return result;
	}

	public void setResult(AttackStatus result) {
		this.result = result;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public Square getLocation() {
		return location;
	}

	public void setLocation(Square square) {
		this.location = square;
	}
}
