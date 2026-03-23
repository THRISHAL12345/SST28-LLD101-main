package snakeandladder.observer;

import snakeandladder.model.Player;
import java.util.*;

public class GameEventManager {

    private List<GameObserver> observers = new ArrayList<>();

    public void subscribe(GameObserver observer) {
        observers.add(observer);
    }

    public void notifyDice(Player p, int val) {
        for (GameObserver o : observers) {
            o.onDiceRoll(p, val);
        }
    }

    public void notifyMove(Player p, int pos) {
        for (GameObserver o : observers) {
            o.onMove(p, pos);
        }
    }

    public void notifyWin(Player p) {
        for (GameObserver o : observers) {
            o.onWin(p);
        }
    }
}
