package controller;

import model.*;
import service.*;

import java.time.LocalDateTime;

public class ParkingController {

    private ParkingService parkingService;
    private BillingService billingService;
    private StatusService statusService;

    public ParkingController(ParkingService ps, BillingService bs, StatusService ss) {
        this.parkingService = ps;
        this.billingService = bs;
        this.statusService = ss;
    }

    public ParkingTicket park(Vehicle v, LocalDateTime time, int gateId) {
        return parkingService.park(v, time, gateId);
    }

    public double exit(ParkingTicket ticket, LocalDateTime time) {
        return billingService.generateBill(ticket, time).getAmount();
    }

    public void status() {
        statusService.printStatus();
    }
}