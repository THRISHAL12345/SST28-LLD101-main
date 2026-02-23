public class DeluxeRoom implements RoomPricing {
    @Override
    public Money monthlyCharge() {
        return new Money(16000.0);
    }
}