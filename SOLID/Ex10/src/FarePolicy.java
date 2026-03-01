public class FarePolicy {

    public double fare(double km) {
        double f = 50.0 + km * 6.6666666667;
        return Math.round(f * 100.0) / 100.0;
    }
}