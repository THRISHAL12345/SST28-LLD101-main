import java.util.*;

public class EventCoordinator {

    List<ObserverContract> observers = new ArrayList<>();

    public void add(ObserverContract o) {
        observers.add(o);
    }

    public void notifyAll(String msg) {
        for (ObserverContract o : observers) {
            o.update(msg);
        }
    }
}