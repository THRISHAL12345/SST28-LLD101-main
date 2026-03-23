package service;

import model.*;
import strategy.*;
import repository.ParkingLot;

import java.time.LocalDateTime;
import java.util.UUID;

public class ParkingService {

    private SlotAllocationStrategy strategy;

    public ParkingService(ParkingLot lot) {
        this.strategy = new NearestSlotStrategy(lot);
    }

    public ParkingTicket park(Vehicle vehicle, LocalDateTime time, int gateId) {

        ParkingSlot slot = strategy.allocateSlot(vehicle, gateId);

        if (slot == null) {
            throw new RuntimeException("No slots available");
        }

        slot.occupy();

        return new ParkingTicket(
                UUID.randomUUID().toString(),
                vehicle,
                slot,
                time,
                gateId
        );
    }
}