public class InternalBusinessService {

    private final RateLimiter rateLimiter;
    private final ExternalProviderClient client;

    public InternalBusinessService(RateLimiter rateLimiter,
                                   ExternalProviderClient client) {
        this.rateLimiter = rateLimiter;
        this.client = client;
    }

    public void processRequest(String key, boolean needsExternalCall) {

        System.out.println("Processing request for key: " + key);

        if (!needsExternalCall) {
            System.out.println("No external call needed");
            return;
        }

        RateLimitDecision decision = rateLimiter.allow(key);

        if (decision.isAllowed()) {
            client.callExternalService(key);
        } else {
            System.out.println("Rate limited ❌ Retry after: "
                    + decision.getRetryAfterMillis() + " ms");
        }
    }
}