import java.util.*;

public class PricingService {

    private final Map<String, MenuItem> menu;

    public PricingService(Map<String, MenuItem> menu) {
        this.menu = menu;
    }

    public double computeSubtotal(List<OrderLine> lines) {
        double subtotal = 0.0;
        for (OrderLine l : lines) {
            MenuItem item = menu.get(l.itemId);
            subtotal += item.price * l.qty;
        }
        return subtotal;
    }

    public List<String> buildLineDescriptions(List<OrderLine> lines) {
        List<String> result = new ArrayList<>();

        for (OrderLine l : lines) {
            MenuItem item = menu.get(l.itemId);
            double lineTotal = item.price * l.qty;
            result.add(String.format("- %s x%d = %.2f",
                    item.name, l.qty, lineTotal));
        }

        return result;
    }
}