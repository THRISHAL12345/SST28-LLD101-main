package service;

import model.Pen;

public class PenService {

    public void write(Pen pen) {
        pen.write();
    }

    public void open(Pen pen) {
        pen.start();
    }

    public void close(Pen pen) {
        pen.close();
    }

    public void refill(Pen pen) {
        pen.refill();
    }
}