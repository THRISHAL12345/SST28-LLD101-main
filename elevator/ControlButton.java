public class ControlButton {
    private final ControlType type;
    private final int targetFloor;

    public ControlButton(ControlType type, int targetFloor) {
        this.type = type;
        this.targetFloor = targetFloor;
    }

    public ControlType getType() {
        return type;
    }

    public int getTargetFloor() {
        return targetFloor;
    }
}