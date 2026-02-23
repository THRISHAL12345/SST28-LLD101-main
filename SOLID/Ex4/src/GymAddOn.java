public class GymAddOn implements AddOnPricing {
    @Override
    public Money monthlyCharge() {
        return new Money(300.0);
    }
}