import java.util.List;

public class LiftManager {

    private final List<LiftCar> lifts;
    private final SchedulingPolicy policy;

    public LiftManager(List<LiftCar> lifts, SchedulingPolicy policy) {
        this.lifts = lifts;
        this.policy = policy;
    }

    public void submitRequest(LiftRequest request) {
        LiftCar lift = policy.selectLift(lifts, request);
        if (lift != null) {
            lift.addStop(request.getSource());
            lift.addStop(request.getDestination());
        }
    }

    public void emergencyAllStop() {
        for (LiftCar lift : lifts) {
            lift.emergencyStop();
        }
    }
}