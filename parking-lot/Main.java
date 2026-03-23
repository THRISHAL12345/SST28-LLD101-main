import controller.ParkingController;
import factory.VehicleFactory;
import model.*;
import model.enums.SlotType;
import repository.ParkingLot;
import service.*;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        ParkingLot lot = ParkingLot.getInstance();

        for (int i = 1; i <= 5; i++) {
            lot.addSlot(new ParkingSlot(i, SlotType.SMALL));
            lot.addSlot(new ParkingSlot(i + 10, SlotType.MEDIUM));
            lot.addSlot(new ParkingSlot(i + 20, SlotType.LARGE));
        }

        ParkingService ps = new ParkingService(lot);
        BillingService bs = new BillingService();
        StatusService ss = new StatusService(lot);

        ParkingController controller = new ParkingController(ps, bs, ss);

        Vehicle v1 = VehicleFactory.createVehicle("KA01", "bike");

        ParkingTicket ticket = controller.park(v1, LocalDateTime.now(), 1);

        controller.status();

        double amount = controller.exit(ticket, LocalDateTime.now().plusHours(3));

        System.out.println("Bill: " + amount);

        controller.status();
    }
}