public class LaundryAddOn implements AddOnPricing {
    @Override
    public Money monthlyCharge() {
        return new Money(500.0);
    }
}