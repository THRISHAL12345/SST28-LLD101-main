import java.util.List;

public class QueueBasedPolicy implements SchedulingPolicy {

    @Override
    public LiftCar selectLift(List<LiftCar> lifts, LiftRequest request) {
        for (LiftCar lift : lifts) {
            if (lift.getStatus() != LiftStatus.MAINTENANCE) {
                return lift;
            }
        }
        return null;
    }
}