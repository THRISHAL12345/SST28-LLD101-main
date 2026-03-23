package snakeandladder.strategy;

import java.util.Random;

public class StandardDice implements DiceStrategy {
    private Random random = new Random();

    @Override
    public int roll() {
        return random.nextInt(6) + 1;
    }
}
