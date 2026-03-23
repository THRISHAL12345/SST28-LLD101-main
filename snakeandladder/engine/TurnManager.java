package engine;

import model.Player;
import java.util.*;

public class TurnManager {
    private Queue<Player> queue;

    public TurnManager(List<Player> players) {
        this.queue = new LinkedList<>(players);
    }

    public Player getNextPlayer() {
        Player p = queue.poll();
        queue.offer(p);
        return p;
    }
}