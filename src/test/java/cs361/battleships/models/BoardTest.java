package cs361.battleships.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    private Board board;

    @Before
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testInvalidPlacement() {
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
    }

    @Test
    public void testPlaceMinesweeper() {
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 1, 'A', true));
    }

    @Test
    public void testAttackEmptySquare() {
        board.placeShip(new Ship("MINESWEEPER"), 1, 'A', true);
        Result result = board.attack(2, 'E');
        assertEquals(AtackStatus.MISS, result.getResult());
    }

    @Test
    public void testAttackShip() {
        Ship minesweeper = new Ship("MINESWEEPER");
        board.placeShip(minesweeper, 1, 'A', true);
        minesweeper = board.getShips().get(0);
        Result result = board.attack(2, 'A');
        assertEquals(AtackStatus.HIT, result.getResult());
        assertEquals(minesweeper, result.getShip());
    }

    @Test
    public void testCaptainsQuarters() {
        Ship destroyer = new Ship("DESTROYER");
        board.placeShip(destroyer, 1, 'A', true);
        destroyer = board.getShips().get(0);
        Result result = board.attack(2, 'A');
        assertEquals(AtackStatus.BLOCKED, result.getResult());
        assertEquals(null, result.getShip());
        Result result2 = board.attack(2, 'A');
        assertEquals(AtackStatus.SURRENDER, result2.getResult());
        assertEquals(destroyer, result2.getShip());
    }

    @Test
    public void testAttackSameSquareMultipleTimes() {
        Ship minesweeper = new Ship("MINESWEEPER");
        board.placeShip(minesweeper, 1, 'A', true);
        board.attack(2, 'A');
        Result result = board.attack(2, 'A');
        assertEquals(AtackStatus.MISS, result.getResult());
    }

    @Test
    public void testAttackSameEmptySquareMultipleTimes() {
        Result initialResult = board.attack(1, 'A');
        assertEquals(AtackStatus.MISS, initialResult.getResult());
        Result result = board.attack(1, 'A');
        assertEquals(AtackStatus.MISS, result.getResult());
    }

    @Test
    public void testSurrender() {
        board.placeShip(new Ship("MINESWEEPER"), 1, 'A', true);
        var result = board.attack(1, 'A');
        assertEquals(AtackStatus.SURRENDER, result.getResult());
    }

    @Test
    public void testPlaceMultipleShipsOfSameType() {
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 1, 'A', true));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 5, 'D', true));
        assertFalse(board.placeShip(new Ship("DESTROYER"), 1, 'A', true));
    }

    @Test
    public void testCantPlaceMoreThan4Ships() {
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 1, 'A', true));
        assertTrue(board.placeShip(new Ship("BATTLESHIP"), 5, 'D', true));
        assertTrue(board.placeShip(new Ship("DESTROYER"), 6, 'A', false));
        assertTrue(board.placeShip(new Ship("SUBMARINE"), 8, 'A', false));
        assertFalse(board.placeShip(new Ship(""), 8, 'A', false));

    }

    @Test
    public void testSonarPulse() {
        Ship testShip = new Ship("BATTLESHIP");
        board.placeShip(testShip,1, 'A', true);
        board.sonarPulse(1,'A');
    }

    @Test
    public void testSubmarine() {
        assertTrue(board.placeShip(new Ship("BATTLESHIP"), 5, 'D', false));
        assertTrue(board.placeShip(new Ship("SUBMARINE"), 5, 'D', false));
        Square s = new Square(4, 'F');
        assertTrue(board.isSub(s));
        assertEquals(AtackStatus.HIT,board.attack(5,'D').getResult());
        assertEquals(AtackStatus.MISS,board.attack(4,'F').getResult());
        assertEquals(AtackStatus.BLOCKED,board.attack(5,'F').getResult());
        assertEquals(AtackStatus.SUNK,board.attack(5,'F').getResult());
        assertEquals(AtackStatus.HIT,board.attack(4,'F').getResult());
        assertEquals(AtackStatus.BLOCKED,board.attack(5,'G').getResult());
        assertEquals(AtackStatus.SURRENDER,board.attack(5,'G').getResult());
    }

    @Test
    public void testSubOverlap() {
        assertTrue(board.placeShip(new Ship("BATTLESHIP"), 5, 'D', false));
        assertTrue(board.placeShip(new Ship("DESTROYER"), 4, 'D', false));
        assertFalse(board.placeShip(new Ship("SUBMARINE"), 5, 'D', false));
    }
}
