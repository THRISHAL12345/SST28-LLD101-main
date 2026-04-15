public class Level {

    private final int number;
    private final HallPanel panel;

    public Level(int number, LiftManager manager) {
        this.number = number;
        this.panel = new HallPanel(number, manager);
    }

    public HallPanel getPanel() {
        return panel;
    }
}