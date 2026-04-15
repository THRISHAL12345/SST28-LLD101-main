import java.util.*;

public class Structure {
    private final List<LiftCar> lifts;
    private final List<Level> levels;
    private final LiftManager manager;

    public Structure(int liftCount, int floorCount, SchedulingPolicy policy) {
        this.lifts = new ArrayList<>();
        this.levels = new ArrayList<>();
        this.manager = new LiftManager(lifts, policy);

        for (int i = 0; i < liftCount; i++) {
            lifts.add(new LiftCar(i, manager));
        }

        for (int i = 0; i < floorCount; i++) {
            levels.add(new Level(i, manager));
        }
    }

    public LiftManager getManager() {
        return manager;
    }
}