package snakeandladder.observer;

import snakeandladder.model.Player;

public interface GameObserver {
    void onDiceRoll(Player player, int diceValue);
    void onMove(Player player, int newPosition);
    void onWin(Player player);
}
