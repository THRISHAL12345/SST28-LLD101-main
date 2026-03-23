package snakeandladder.factory;

import snakeandladder.strategy.*;

public class DiceFactory {

    public static DiceStrategy getDice(String type) {
        if (type.equalsIgnoreCase("crooked")) {
            return new CrookedDice();
        }
        return new StandardDice();
    }
}
