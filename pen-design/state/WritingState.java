package state;

import model.Pen;

public class WritingState implements PenState {

    @Override
    public void write(Pen pen) {
        if (!pen.getRefill().hasInk()) {
            System.out.println("No ink! Please refill.");
            return;
        }

        pen.getRefill().useInk();
        System.out.println("Writing... Ink left: " + pen.getRefill().getInkLevel());
    }

    @Override
    public void open(Pen pen) {
        System.out.println("Pen is already open.");
    }

    @Override
    public void close(Pen pen) {
        pen.setState(new ClosedState());
        System.out.println("Pen closed.");
    }
}