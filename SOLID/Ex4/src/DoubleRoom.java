public class DoubleRoom implements RoomPricing {
    @Override
    public Money monthlyCharge() {
        return new Money(15000.0);
    }
}