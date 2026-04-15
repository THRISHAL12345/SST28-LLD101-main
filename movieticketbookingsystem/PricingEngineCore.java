import java.util.*;

public class PricingEngineCore {

    List<PricingRuleContract> rules;

    public PricingEngineCore(List<PricingRuleContract> rules) {
        this.rules = rules;
    }

    public double calculate(List<SeatUnit> seats) {
        double base = seats.size() * 200;
        for (PricingRuleContract r : rules) {
            base = r.apply(base);
        }
        return base;
    }
}