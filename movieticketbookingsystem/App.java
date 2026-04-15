import java.util.*;

public class App {

    public static void main(String[] args) {

        CinemaSystem system = new CinemaSystem();

        AdminManager admin = new AdminManager(system);

        Film film = new Film("1", "Movie");
        admin.addMovie(film);

        Venue venue = new Venue("V1", "Bangalore");
        admin.addTheatre(venue);

        PricingEngineCore pricing = new PricingEngineCore(PricingRuleProvider.getRules());

        OrderService booking = new OrderService(pricing, new PaymentHandler());

        List<SeatUnit> seats = List.of(new SeatUnit("S1", SeatCategory.GOLD));

        ShowSlot show = new ShowSlot();

        booking.bookTickets(show, seats, PaymentChannel.UPI);
    }
}