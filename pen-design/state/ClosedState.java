package state;

import model.Pen;

public class ClosedState implements PenState {

    @Override
    public void write(Pen pen) {
        System.out.println("Cannot write. Pen is closed.");
    }

    @Override
    public void open(Pen pen) {
        pen.setState(new WritingState());
        System.out.println("Pen opened.");
    }

    @Override
    public void close(Pen pen) {
        System.out.println("Pen is already closed.");
    }
}