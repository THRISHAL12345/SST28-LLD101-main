package snakeandladder.observer;

import snakeandladder.model.Player;

public class LoggerObserver implements GameObserver {

    @Override
    public void onDiceRoll(Player player, int diceValue) {
        System.out.println(player.getName() + " rolled " + diceValue);
    }

    @Override
    public void onMove(Player player, int pos) {
        System.out.println(player.getName() + " moved to " + pos);
    }

    @Override
    public void onWin(Player player) {
        System.out.println(player.getName() + " WON!");
    }
}
