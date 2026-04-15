public class LiftRequest {
    private final int source;
    private final int destination;
    private final MoveDirection direction;

    public LiftRequest(int source, int destination, MoveDirection direction) {
        this.source = source;
        this.destination = destination;
        this.direction = direction;
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public MoveDirection getDirection() {
        return direction;
    }
}