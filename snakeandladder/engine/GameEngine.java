package engine;

import model.*;
import strategy.DiceStrategy;
import observer.GameEventManager;
import service.MoveService;

import java.util.*;

public class GameEngine {

    private Board board;
    private TurnManager turnManager;
    private DiceStrategy dice;
    private GameEventManager eventManager;
    private MoveService moveService;
    private int winningPosition;

    public GameEngine(Board board, List<Player> players,
                      DiceStrategy dice, GameEventManager eventManager) {

        this.board = board;
        this.turnManager = new TurnManager(players);
        this.dice = dice;
        this.eventManager = eventManager;
        this.moveService = new MoveService();
        this.winningPosition = board.getSize() * board.getSize();
    }

    public void start() {

        while (true) {
            Player player = turnManager.getNextPlayer();

            if (player.isWinner()) continue;

            int roll = dice.roll();
            eventManager.notifyDice(player, roll);

            int next = player.getPosition() + roll;

            if (next > winningPosition) {
                next = player.getPosition();
            }

            next = moveService.getFinalPosition(board, next);

            player.setPosition(next);
            eventManager.notifyMove(player, next);

            if (next == winningPosition) {
                player.setWinner(true);
                eventManager.notifyWin(player);
            }
        }
    }
}