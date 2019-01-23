package cs361.battleships.models;

public class Result {

	private AtackStatus status;
	private Ship ship;
	private Square location;

	public AtackStatus getResult() {
		//TODO implement
		// this - reference to the object
		return status;
	}

	public void setResult(AtackStatus result) {
		//TODO implement
		this.status = result;
	}

	public Ship getShip() {
		//TODO implement
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public Square getLocation() {
		//TODO implement
		return location;
	}

	public void setLocation(Square square) {
		this.location = square;
	}
}
