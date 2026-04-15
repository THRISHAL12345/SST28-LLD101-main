import java.util.HashMap;
import java.util.Map;

public class CacheNode {

    private final int capacity;
    private final Map<String, String> cache;
    private final EvictionPolicy<String> evictionPolicy;

    public CacheNode(int capacity, EvictionPolicy<String> evictionPolicy) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.evictionPolicy = evictionPolicy;
    }

    public String get(String key) {
        if (!cache.containsKey(key)) return null;

        evictionPolicy.keyAccessed(key);
        return cache.get(key);
    }

    public void put(String key, String value) {
        if (cache.containsKey(key)) {
            cache.put(key, value);
            evictionPolicy.keyAccessed(key);
            return;
        }

        if (cache.size() >= capacity) {
            String evictKey = evictionPolicy.evictKey();
            cache.remove(evictKey);
        }

        cache.put(key, value);
        evictionPolicy.keyAccessed(key);
    }
}