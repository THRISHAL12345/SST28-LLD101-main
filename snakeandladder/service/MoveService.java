package service;

import model.Board;

public class MoveService {

    public int getFinalPosition(Board board, int position) {

        if (board.getSnakes().containsKey(position)) {
            return board.getSnakes().get(position);
        }

        if (board.getLadders().containsKey(position)) {
            return board.getLadders().get(position);
        }

        return position;
    }
}