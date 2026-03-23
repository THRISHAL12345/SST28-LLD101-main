import model.*;
import model.enums.TipType;
import service.PenService;

public class Main {

    public static void main(String[] args) {

        Ink ink = new Ink("Blue");
        Refill refill = new Refill(ink, 5);
        Tip tip = new Tip(TipType.BALL);

        Pen pen = new Pen(refill, tip);
        PenService service = new PenService();

        service.write(pen);
        service.open(pen);

        for (int i = 0; i < 6; i++) {
            service.write(pen);
        }

        service.refill(pen);

        service.write(pen);

        service.close(pen);
    }
}