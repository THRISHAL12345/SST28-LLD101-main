public class AdminManager {

    CinemaSystem system;

    public AdminManager(CinemaSystem system) {
        this.system = system;
    }

    public void addMovie(Film film) {
        system.films.put(film.id, film);
    }

    public void addTheatre(Venue v) {
        system.venues.put(v.id, v);
    }

    public void addShow(ShowSlot show) {
        system.shows.put(show.id, show);
    }
}