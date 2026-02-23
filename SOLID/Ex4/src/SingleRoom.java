public class SingleRoom implements RoomPricing {
    @Override
    public Money monthlyCharge() {
        return new Money(14000.0);
    }
}