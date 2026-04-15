import java.util.HashMap;
import java.util.Map;

public class InMemoryDatabase implements Database {

    private final Map<String, String> storage = new HashMap<>();

    @Override
    public String get(String key) {
        System.out.println("DB HIT for key: " + key);
        return storage.getOrDefault(key, "Value_" + key);
    }

    @Override
    public void put(String key, String value) {
        storage.put(key, value);
    }
}