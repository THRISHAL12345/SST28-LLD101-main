import java.util.List;

public interface DistributionStrategy {
    CacheNode getNode(String key, List<CacheNode> nodes);
}