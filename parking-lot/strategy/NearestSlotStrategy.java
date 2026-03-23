package strategy;

import model.*;
import model.enums.*;
import repository.ParkingLot;

import java.util.*;

public class NearestSlotStrategy implements SlotAllocationStrategy {

    private ParkingLot lot;

    public NearestSlotStrategy(ParkingLot lot) {
        this.lot = lot;
    }

    @Override
    public ParkingSlot allocateSlot(Vehicle vehicle, int gateId) {

        List<SlotType> allowed = getAllowed(vehicle.getType());

        for (SlotType type : allowed) {
            for (ParkingSlot slot : lot.getSlots(type)) {
                if (!slot.isOccupied()) {
                    return slot;
                }
            }
        }
        return null;
    }

    private List<SlotType> getAllowed(VehicleType type) {

        if (type == VehicleType.BIKE)
            return Arrays.asList(SlotType.SMALL, SlotType.MEDIUM, SlotType.LARGE);

        if (type == VehicleType.CAR)
            return Arrays.asList(SlotType.MEDIUM, SlotType.LARGE);

        return Arrays.asList(SlotType.LARGE);
    }
}