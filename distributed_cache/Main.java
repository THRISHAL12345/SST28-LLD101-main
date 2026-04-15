import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<CacheNode> nodes = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            nodes.add(new CacheNode(
                    2,
                    EvictionPolicyFactory.createPolicy("LRU")
            ));
        }

        DistributedCache cache = new DistributedCache(
                nodes,
                new ModuloDistributionStrategy(),
                new InMemoryDatabase()
        );

        cache.put("A", "Apple");
        cache.put("B", "Ball");

        System.out.println(cache.get("A")); ]
        System.out.println(cache.get("C"));
        System.out.println(cache.get("A"));
    }
}