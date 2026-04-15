import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SlidingWindowCounterStrategy implements RateLimitingStrategy {

    private final ConcurrentHashMap<String, Deque<Long>> map = new ConcurrentHashMap<>();

    @Override
    public RateLimitDecision allow(String key, RateLimitRule rule, Clock clock) {

        long now = clock.now();

        map.putIfAbsent(key, new LinkedList<>());
        Deque<Long> queue = map.get(key);

        synchronized (queue) {

            while (!queue.isEmpty() &&
                    now - queue.peekFirst() >= rule.getWindowSizeMillis()) {
                queue.pollFirst();
            }

            if (queue.size() < rule.getMaxRequests()) {
                queue.addLast(now);
                return new RateLimitDecision(true, 0);
            }

            long retryAfter = rule.getWindowSizeMillis() - (now - queue.peekFirst());
            return new RateLimitDecision(false, retryAfter);
        }
    }
}