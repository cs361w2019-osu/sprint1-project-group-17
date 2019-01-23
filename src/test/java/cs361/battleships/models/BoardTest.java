package cs361.battleships.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    @Test
    public void testInvalidPlacement() {
        Board board = new Board();
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));
    }

    @Test
    public void testCheckGameOver() {
        Board board = new Board();
        Result attack1 = new Result();
        attack1.setResult(AttackStatus.SUNK);
        List<Result> attacks = new ArrayList<Result>();

        attacks.add(attack1);
        board.setAttacks(attacks);
        assertFalse(board.checkGameOver());

        attacks.add(attack1);
        board.setAttacks(attacks);
        assertFalse(board.checkGameOver());

        attacks.add(attack1);
        board.setAttacks(attacks);
        assertTrue(board.checkGameOver());

        attacks.add(attack1);
        board.setAttacks(attacks);
        assertTrue(board.checkGameOver());
    }
}
