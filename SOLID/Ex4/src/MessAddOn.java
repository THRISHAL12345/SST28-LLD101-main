public class MessAddOn implements AddOnPricing {
    @Override
    public Money monthlyCharge() {
        return new Money(1000.0);
    }
}