package service;

import model.Board;
import model.Snake;
import model.Ladder;

import java.util.*;

public class BoardService {

    public static void initializeBoard(Board board, int n, String difficulty) {

        int max = n * n;
        int count = difficulty.equalsIgnoreCase("hard") ? n : n / 2;

        Random random = new Random();

        Set<Integer> occupied = new HashSet<>();

        generateSnakes(board, count, max, random, occupied);
        generateLadders(board, count, max, random, occupied);
    }

    private static void generateSnakes(Board board, int count, int max,
                                       Random random, Set<Integer> occupied) {

        while (board.getSnakes().size() < count) {

            int head = random.nextInt(max - 1) + 2;
            int tail = random.nextInt(head - 1) + 1;

            if (occupied.contains(head) || occupied.contains(tail)) continue;
            if (head == max) continue;

            Snake snake = new Snake(head, tail);
            board.addSnake(snake);

            occupied.add(head);
            occupied.add(tail);
        }
    }

    private static void generateLadders(Board board, int count, int max,
                                        Random random, Set<Integer> occupied) {

        while (board.getLadders().size() < count) {

            int start = random.nextInt(max - 1) + 1;
            int end = random.nextInt(max - start) + start + 1;

            if (occupied.contains(start) || occupied.contains(end)) continue;
            if (start == 1) continue;

            if (end == max && random.nextBoolean()) continue;

            Ladder ladder = new Ladder(start, end);
            board.addLadder(ladder);

            occupied.add(start);
            occupied.add(end);
        }
    }
}