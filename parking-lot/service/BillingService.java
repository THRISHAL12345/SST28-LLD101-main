package service;

import model.*;
import model.enums.SlotType;

import java.time.*;
import java.util.*;

public class BillingService {

    private Map<SlotType, Double> rates = new HashMap<>();

    public BillingService() {
        rates.put(SlotType.SMALL, 10.0);
        rates.put(SlotType.MEDIUM, 20.0);
        rates.put(SlotType.LARGE, 50.0);
    }

    public Bill generateBill(ParkingTicket ticket, LocalDateTime exitTime) {

        long hours = Duration.between(ticket.getEntryTime(), exitTime).toHours();
        if (hours == 0) hours = 1;

        double rate = rates.get(ticket.getSlot().getType());
        double amount = rate * hours;

        ticket.getSlot().free();

        return new Bill(ticket, exitTime, amount);
    }
}