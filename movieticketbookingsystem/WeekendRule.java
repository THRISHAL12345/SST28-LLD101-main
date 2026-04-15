public class WeekendRule implements PricingRuleContract {
    public double apply(double price) {
        return price * 1.2;
    }
}