public class Main {

    public static void main(String[] args) throws InterruptedException {

        RateLimitRule rule = new RateLimitRule(5, 10000);

        Clock clock = new SystemClock();

        RateLimitingStrategy strategy = new SlidingWindowCounterStrategy();

        RateLimiter rateLimiter = new PluggableRateLimiter(strategy, rule, clock);

        InternalBusinessService service = new InternalBusinessService(
                rateLimiter,
                new ExternalProviderClient()
        );

        String key = "Tenant1";

        for (int i = 0; i < 8; i++) {
            service.processRequest(key, true);
            Thread.sleep(1000);
        }
    }
}