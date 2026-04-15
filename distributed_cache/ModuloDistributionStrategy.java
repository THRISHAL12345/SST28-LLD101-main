import java.util.List;

public class ModuloDistributionStrategy implements DistributionStrategy {

    @Override
    public CacheNode getNode(String key, List<CacheNode> nodes) {
        int index = Math.abs(key.hashCode()) % nodes.size();
        return nodes.get(index);
    }
}