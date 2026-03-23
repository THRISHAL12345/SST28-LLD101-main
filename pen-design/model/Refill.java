package model;

public class Refill {

    private Ink ink;
    private int inkLevel;

    public Refill(Ink ink, int inkLevel) {
        this.ink = ink;
        this.inkLevel = inkLevel;
    }

    public boolean hasInk() {
        return inkLevel > 0;
    }

    public void useInk() {
        if (inkLevel > 0) {
            inkLevel--;
        }
    }

    public void refill() {
        inkLevel = 100;
    }

    public int getInkLevel() {
        return inkLevel;
    }

    public Ink getInk() {
        return ink;
    }
}