public interface RateLimitingStrategy {
    RateLimitDecision allow(String key, RateLimitRule rule, Clock clock);
}