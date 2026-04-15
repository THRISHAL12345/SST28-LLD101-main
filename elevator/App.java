public class App {

    public static void main(String[] args) {

        Structure structure = new Structure(
                3,
                10,
                new DistanceBasedPolicy()
        );

        LiftManager manager = structure.getManager();

        manager.submitRequest(new LiftRequest(2, 7, MoveDirection.UP));
        manager.submitRequest(new LiftRequest(5, 1, MoveDirection.DOWN));

        for (int i = 0; i < 10; i++) {
            for (LiftCar lift : manager.lifts) {
                lift.move();
            }
        }

        manager.emergencyAllStop();
    }
}