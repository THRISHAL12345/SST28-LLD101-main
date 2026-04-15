public class RateLimitRule {

    private final int maxRequests;
    private final long windowSizeMillis;

    public RateLimitRule(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public long getWindowSizeMillis() {
        return windowSizeMillis;
    }
}