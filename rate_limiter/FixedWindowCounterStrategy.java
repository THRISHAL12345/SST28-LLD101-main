import java.util.concurrent.ConcurrentHashMap;

public class FixedWindowCounterStrategy implements RateLimitingStrategy {

    private static class Counter {
        int count;
        long windowStart;
    }

    private final ConcurrentHashMap<String, Counter> map = new ConcurrentHashMap<>();

    @Override
    public RateLimitDecision allow(String key, RateLimitRule rule, Clock clock) {

        long now = clock.now();

        map.putIfAbsent(key, new Counter());
        Counter counter = map.get(key);

        synchronized (counter) {
            if (now - counter.windowStart >= rule.getWindowSizeMillis()) {
                counter.windowStart = now;
                counter.count = 0;
            }

            if (counter.count < rule.getMaxRequests()) {
                counter.count++;
                return new RateLimitDecision(true, 0);
            }

            long retryAfter = rule.getWindowSizeMillis() - (now - counter.windowStart);
            return new RateLimitDecision(false, retryAfter);
        }
    }
}