package controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs361.battleships.models.Game;

public class MovementGameAction {

    @JsonProperty private Game game;
    @JsonProperty private int dir;

    public Game getGame() {
        return game;
    }
    public int getDir() {
        return dir;
    }
}
