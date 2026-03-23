package controller;

import model.*;
import service.BoardService;
import factory.DiceFactory;
import observer.*;

import engine.GameEngine;
import strategy.DiceStrategy;

import java.util.*;

public class GameController {

    public void startGame() {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int x = sc.nextInt();
        String difficulty = sc.next();

        Board board = new Board(n);
        BoardService.initializeBoard(board, n, difficulty);

        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= x; i++) {
            players.add(new Player("P" + i));
        }

        DiceStrategy dice = DiceFactory.getDice("standard");

        GameEventManager manager = new GameEventManager();
        manager.subscribe(new LoggerObserver());

        GameEngine engine = new GameEngine(board, players, dice, manager);
        engine.start();
    }
}