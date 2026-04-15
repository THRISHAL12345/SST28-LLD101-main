public class PluggableRateLimiter implements RateLimiter {

    private final RateLimitingStrategy strategy;
    private final RateLimitRule rule;
    private final Clock clock;

    public PluggableRateLimiter(RateLimitingStrategy strategy,
                                RateLimitRule rule,
                                Clock clock) {
        this.strategy = strategy;
        this.rule = rule;
        this.clock = clock;
    }

    @Override
    public RateLimitDecision allow(String key) {
        return strategy.allow(key, rule, clock);
    }
}