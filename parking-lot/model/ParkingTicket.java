package model;

import java.time.LocalDateTime;

public class ParkingTicket {
    private String id;
    private Vehicle vehicle;
    private ParkingSlot slot;
    private LocalDateTime entryTime;
    private int gateId;

    public ParkingTicket(String id, Vehicle vehicle, ParkingSlot slot,
                         LocalDateTime entryTime, int gateId) {
        this.id = id;
        this.vehicle = vehicle;
        this.slot = slot;
        this.entryTime = entryTime;
        this.gateId = gateId;
    }

    public ParkingSlot getSlot() {
        return slot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}