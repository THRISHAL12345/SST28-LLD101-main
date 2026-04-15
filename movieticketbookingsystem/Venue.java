import java.util.*;

public class Venue {
    String id;
    String city;
    List<Hall> halls = new ArrayList<>();

    public Venue(String id, String city) {
        this.id = id;
        this.city = city;
    }
}