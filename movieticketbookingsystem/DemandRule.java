public class DemandRule implements PricingRuleContract {
    public double apply(double price) {
        return price * 1.5;
    }
}