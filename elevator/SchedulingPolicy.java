import java.util.List;

public interface SchedulingPolicy {
    LiftCar selectLift(List<LiftCar> lifts, LiftRequest request);
}