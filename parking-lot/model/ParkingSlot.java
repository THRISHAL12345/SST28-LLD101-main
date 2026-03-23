package model;

import model.enums.SlotType;

public class ParkingSlot {
    private int id;
    private SlotType type;
    private boolean occupied;

    public ParkingSlot(int id, SlotType type) {
        this.id = id;
        this.type = type;
        this.occupied = false;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void occupy() {
        this.occupied = true;
    }

    public void free() {
        this.occupied = false;
    }

    public int getId() {
        return id;
    }

    public SlotType getType() {
        return type;
    }
}