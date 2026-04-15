import java.util.Iterator;
import java.util.LinkedHashSet;

public class LRUEvictionPolicy<K> implements EvictionPolicy<K> {

    private final LinkedHashSet<K> set = new LinkedHashSet<>();

    @Override
    public void keyAccessed(K key) {
        set.remove(key);
        set.add(key);
    }

    @Override
    public K evictKey() {
        Iterator<K> it = set.iterator();
        K lru = it.next();
        it.remove();
        return lru;
    }
}