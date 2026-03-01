public class Main {
    public static void main(String[] args) {

        System.out.println("=== Transport Booking ===");

        TripRequest req =
                new TripRequest("23BCS1010",
                        new GeoPoint(12.97, 77.59),
                        new GeoPoint(12.93, 77.62));

        DistanceCalc dist = new DistanceCalculator();
        DriverAllocate alloc = new DriverAllocator();
        PaymentProcess pay = new PaymentGateway();
        FarePolicy policy = new FarePolicy();

        TransportBookingService svc =
                new TransportBookingService(dist, alloc, pay, policy);

        svc.book(req);
    }
}