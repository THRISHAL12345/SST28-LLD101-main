import java.util.*;

public class LiftCar {

    private final int id;
    private int currentFloor;
    private LiftStatus status;
    private final Queue<Integer> stops;
    private final LiftManager manager;
    private int currentLoad;

    public LiftCar(int id, LiftManager manager) {
        this.id = id;
        this.manager = manager;
        this.currentFloor = 0;
        this.status = LiftStatus.IDLE;
        this.stops = new LinkedList<>();
        this.currentLoad = 0;
    }

    public void addStop(int floor) {
        stops.offer(floor);
    }

    public void move() {
        if (status == LiftStatus.MAINTENANCE) return;

        if (stops.isEmpty()) {
            status = LiftStatus.IDLE;
            return;
        }

        int target = stops.peek();

        if (currentFloor < target) {
            status = LiftStatus.UP;
            currentFloor++;
        } else if (currentFloor > target) {
            status = LiftStatus.DOWN;
            currentFloor--;
        }

        if (currentFloor == target) {
            stops.poll();
        }
    }

    public void emergencyStop() {
        stops.clear();
        status = LiftStatus.IDLE;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public LiftStatus getStatus() {
        return status;
    }
}