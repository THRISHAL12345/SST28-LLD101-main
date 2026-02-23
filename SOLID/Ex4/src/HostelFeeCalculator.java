import java.util.*;

public class HostelFeeCalculator {

    private final FakeBookingRepo repo;
    private final FeeCalculator feeCalculator = new FeeCalculator();

    public HostelFeeCalculator(FakeBookingRepo repo) {
        this.repo = repo;
    }

    public void process(BookingRequest req) {

        RoomPricing room = RoomFactory.create(req.roomType);
        List<AddOnPricing> addOns = AddOnFactory.createAll(req.addOns);

        Money monthly = feeCalculator.calculateMonthly(room, addOns);
        Money deposit = new Money(5000.00);

        ReceiptPrinter.print(req, monthly, deposit);

        String bookingId = "H-" + (7000 + new Random(1).nextInt(1000));
        repo.save(bookingId, req, monthly, deposit);
    }
}