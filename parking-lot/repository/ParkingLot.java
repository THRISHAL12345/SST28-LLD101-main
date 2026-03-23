package repository;

import model.ParkingSlot;
import model.enums.SlotType;

import java.util.*;

public class ParkingLot {

    private static ParkingLot instance;
    private Map<SlotType, List<ParkingSlot>> slots;

    private ParkingLot() {
        slots = new HashMap<>();
        for (SlotType type : SlotType.values()) {
            slots.put(type, new ArrayList<>());
        }
    }

    public static ParkingLot getInstance() {
        if (instance == null) {
            instance = new ParkingLot();
        }
        return instance;
    }

    public void addSlot(ParkingSlot slot) {
        slots.get(slot.getType()).add(slot);
    }

    public List<ParkingSlot> getSlots(SlotType type) {
        return slots.get(type);
    }

    public Map<SlotType, List<ParkingSlot>> getAllSlots() {
        return slots;
    }
}