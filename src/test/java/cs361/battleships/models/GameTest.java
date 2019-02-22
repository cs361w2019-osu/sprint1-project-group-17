package cs361.battleships.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testAttack() {
        Game game = new Game();
        assertTrue(game.attack(1, 'A'));
        assertFalse(game.attack(1, 'A'));
    }

    @Test
    public void testSonar() {
        Game game = new Game();
        assertTrue(game.sonar(1, 'A'));
    }

}
