public class HallPanel {

    private final int floor;
    private final LiftManager manager;

    public HallPanel(int floor, LiftManager manager) {
        this.floor = floor;
        this.manager = manager;
    }

    public void pressUp() {
        manager.submitRequest(new LiftRequest(floor, floor, MoveDirection.UP));
    }

    public void pressDown() {
        manager.submitRequest(new LiftRequest(floor, floor, MoveDirection.DOWN));
    }
}