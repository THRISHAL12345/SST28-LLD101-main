public class TripleRoom implements RoomPricing {
    @Override
    public Money monthlyCharge() {
        return new Money(12000.0);
    }
}