package service;

import repository.ParkingLot;
import model.enums.SlotType;

public class StatusService {

    private ParkingLot lot;

    public StatusService(ParkingLot lot) {
        this.lot = lot;
    }

    public void printStatus() {

        for (SlotType type : SlotType.values()) {
            long free = lot.getSlots(type)
                    .stream()
                    .filter(slot -> !slot.isOccupied())
                    .count();

            System.out.println(type + " FREE: " + free);
        }
    }
}