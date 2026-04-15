public class SeasonRule implements PricingRuleContract {
    public double apply(double price) {
        return price * 1.1;
    }
}