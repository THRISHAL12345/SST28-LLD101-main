import java.util.*;

public class PricingRuleProvider {

    public static List<PricingRuleContract> getRules() {
        return List.of(new WeekendRule(), new DemandRule());
    }
}