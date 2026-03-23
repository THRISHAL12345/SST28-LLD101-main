package snakeandladder.strategy;

public class CrookedDice implements DiceStrategy {
    @Override
    public int roll() {
        return (int)(Math.random() * 3 + 1);
    }
}
