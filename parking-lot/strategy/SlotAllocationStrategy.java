package strategy;

import model.*;

public interface SlotAllocationStrategy {
    ParkingSlot allocateSlot(Vehicle vehicle, int gateId);
}