import java.util.List;

public class DistanceBasedPolicy implements SchedulingPolicy {

    @Override
    public LiftCar selectLift(List<LiftCar> lifts, LiftRequest request) {

        LiftCar best = null;
        int min = Integer.MAX_VALUE;

        for (LiftCar lift : lifts) {
            if (lift.getStatus() == LiftStatus.MAINTENANCE) continue;

            int dist = Math.abs(lift.getCurrentFloor() - request.getSource());
            if (dist < min) {
                min = dist;
                best = lift;
            }
        }

        return best;
    }
}