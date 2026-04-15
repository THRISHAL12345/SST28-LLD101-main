public class SeatUnit {
    String id;
    SeatCategory type;
    SeatCondition state;

    public SeatUnit(String id, SeatCategory type) {
        this.id = id;
        this.type = type;
        this.state = SeatCondition.FREE;
    }
}