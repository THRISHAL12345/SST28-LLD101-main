import java.util.*;

public class TicketOrder {
    String id;
    List<SeatUnit> seats;
    double price;
    OrderStatus status;

    public TicketOrder(String id, List<SeatUnit> seats, double price) {
        this.id = id;
        this.seats = seats;
        this.price = price;
        this.status = OrderStatus.CREATED;
    }
}