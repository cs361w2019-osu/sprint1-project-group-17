package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class Square {

	@JsonProperty private int row;
	@JsonProperty private char column;
	@JsonProperty private boolean hit = false;
	@JsonProperty private boolean cap = false;
	@JsonProperty private boolean capHit = false;

	public Square() {
	}

	public Square(int row, char column) {
		this.row = row;
		this.column = column;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}


	@Override
	public boolean equals(Object other) {
		if (other instanceof Square) {
			return ((Square) other).row == this.row && ((Square) other).column == this.column;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return 31 * row + column;
	}

	@JsonIgnore
	public boolean isOutOfBounds() {
		return row > 10 || row < 1 || column > 'J' || column < 'A';
	}

	public boolean isMoveOutOfBounds(int dir) {
		if(dir == 8)
			return (row - 1) > 10 || (row - 1) < 1 || column > 'J' || column < 'A';
		else if(dir == 2)
			return (row + 1) > 10 || (row + 1) < 1 || column > 'J' || column < 'A';
		else if(dir == 4)
			return row > 10 || row < 1 || (char)(column - 1) > 'J' || (char)(column - 1) < 'A';
		else
			return row > 10 || row < 1 || (char)(column + 1) > 'J' || (char)(column + 1) < 'A';
	}

	public boolean isHit() {
		return hit;
	}

	public void hit() {
		hit = true;
	}

	public boolean isCapHit() {
		return capHit;
	}

	public void capHit(){
		capHit = true;
	}

	public boolean isCap() {
		return cap;
	}

	public void cap() {
		cap = true;
	}

	public void move(int dir){
		if(dir == 8)
			row = row - 1;
		else if (dir == 2)
			row = row + 1;
		else if (dir == 4)
			column = (char)(column - 1);
		else
			column = (char)(column + 1);
	}

	@Override
	public String toString() {
		return "(" + row + ", " + column + ')';
	}
}
