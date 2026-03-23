package model;

import state.*;

public class Pen {

    private Refill refill;
    private Tip tip;
    private PenState state;

    public Pen(Refill refill, Tip tip) {
        this.refill = refill;
        this.tip = tip;
        this.state = new ClosedState();
    }

    public void start() {
        state.open(this);
    }

    public void write() {
        state.write(this);
    }

    public void close() {
        state.close(this);
    }

    public void refill() {
        refill.refill();
        System.out.println("Pen refilled successfully.");
    }

    public Refill getRefill() {
        return refill;
    }

    public Tip getTip() {
        return tip;
    }

    public void setState(PenState state) {
        this.state = state;
    }
}