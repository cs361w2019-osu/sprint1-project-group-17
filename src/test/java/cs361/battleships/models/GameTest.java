package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {

    private Game game = new Game();

    @Test
    public void testPlaceShip() {

        assertTrue(game.placeShip(new Ship("MINESWEEPER"),1,'A',true));
        assertFalse(game.placeShip(new Ship("MINESWEEPER"),4,'A',true));
        assertFalse(game.placeShip(new Ship("DESTROYER"),1,'A',true));
        assertTrue(game.placeShip(new Ship("DESTROYER"),2,'B',true));
        assertTrue(game.placeShip(new Ship("BATTLESHIP"),3,'C',true));

    }

    @Test
    public void testAttack() {
        assertTrue(game.attack(1,'A'));
        assertFalse(game.attack(1,'A'));
    }

    @Test
    public void testSonar() {
        assertTrue(game.sonar(2,'A'));
    }
}