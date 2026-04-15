import java.util.*;

public class SearchEngine {

    CinemaSystem system;

    public SearchEngine(CinemaSystem system) {
        this.system = system;
    }

    public List<Venue> showTheatre(String city) {
        List<Venue> res = new ArrayList<>();
        for (Venue v : system.venues.values()) {
            if (v.city.equals(city)) res.add(v);
        }
        return res;
    }

    public List<Film> showMovies(String city) {
        return new ArrayList<>(system.films.values());
    }
}