import java.util.*;

public class OrderService {

    PricingEngineCore pricing;
    PaymentHandler payment;

    public OrderService(PricingEngineCore pricing, PaymentHandler payment) {
        this.pricing = pricing;
        this.payment = payment;
    }

    public synchronized TicketOrder bookTickets(ShowSlot show, List<SeatUnit> seats, PaymentChannel mode) {

        for (SeatUnit s : seats) {
            if (s.state != SeatCondition.FREE) return null;
        }

        for (SeatUnit s : seats) {
            s.state = SeatCondition.HELD;
        }

        double price = pricing.calculate(seats);

        boolean paid = payment.pay(price, mode);

        if (!paid) {
            for (SeatUnit s : seats) s.state = SeatCondition.FREE;
            return null;
        }

        for (SeatUnit s : seats) s.state = SeatCondition.BOOKED;

        return new TicketOrder(UUID.randomUUID().toString(), seats, price);
    }
}