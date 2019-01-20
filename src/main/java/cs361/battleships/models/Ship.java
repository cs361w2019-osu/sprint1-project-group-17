package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Ship {

	@JsonProperty private String kind;
	@JsonProperty private int size;
	@JsonProperty private List<Square> occupiedSquares;

	public Ship() {
		occupiedSquares = new ArrayList<>();
	}

	public Ship(String kind) {
		this.occupiedSquares = new ArrayList<>();
		this.kind = kind;
		this.size = this.getSize();
	}

	public List<Square> getOccupiedSquares() {
		return occupiedSquares;
	}

	public void setOccupiedSquares(List<Square> occupiedSquares){
		this.occupiedSquares = occupiedSquares;
	}

  public String getKind(){
		return kind;
	}

	public int getSize () {
		switch (this.kind) {
			case "BATTLESHIP":
			return 4;
			case "DESTROYER":
			return 3;
			case "MINESWEEPER":
			return 2;
			default:
			return -1;
		}
	}
}
