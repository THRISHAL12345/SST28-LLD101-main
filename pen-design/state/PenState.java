package state;

import model.Pen;

public interface PenState {

    void write(Pen pen);

    void open(Pen pen);

    void close(Pen pen);
}