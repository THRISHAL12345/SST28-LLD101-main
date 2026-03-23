package model;

import java.util.*;

public class Board {

    private final int size;
    private final Map<Integer, Integer> snakes;
    private final Map<Integer, Integer> ladders;

    public Board(int size) {
        this.size = size;
        this.snakes = new HashMap<>();
        this.ladders = new HashMap<>();
    }

    public int getSize() {
        return size;
    }

    public int getWinningPosition() {
        return size * size;
    }

    public Map<Integer, Integer> getSnakes() {
        return Collections.unmodifiableMap(snakes);
    }

    public Map<Integer, Integer> getLadders() {
        return Collections.unmodifiableMap(ladders);
    }

    public void addSnake(Snake snake) {
        snakes.put(snake.getHead(), snake.getTail());
    }

    public void addLadder(Ladder ladder) {
        ladders.put(ladder.getStart(), ladder.getEnd());
    }
}