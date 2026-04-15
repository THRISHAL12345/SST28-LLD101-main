public class EvictionPolicyFactory {

    public static EvictionPolicy<String> createPolicy(String type) {
        if ("LRU".equalsIgnoreCase(type)) {
            return new LRUEvictionPolicy<>();
        }
        throw new IllegalArgumentException("Unknown eviction policy: " + type);
    }
}