package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

// Klasse zur Repräsentation von Filmen in der Watchlist
public class WatchlistMovieEntity extends MovieEntity {
    // Felder für die Datenbank-ID und die ID des Films
    // (wenn diese Felder nicht benötigt werden, können sie entfernt werden)
    long dbId;
    String id;

    // Standardkonstruktor
    public WatchlistMovieEntity() {

    }

    // Konstruktor, der ein Movie-Objekt entgegennimmt und die Werte in die entsprechenden Felder setzt
    public WatchlistMovieEntity(Movie m) {
        super(m);
    }

    // Methode zur Konvertierung einer Liste von WatchlistMovieEntity-Objekten in eine Liste von Movie-Objekten
    public static List<Movie> watchlistToMovies(List<WatchlistMovieEntity> watchlistMovieEntities) {
        List<Movie> movies = new ArrayList<>();
        for (WatchlistMovieEntity m : watchlistMovieEntities) movies.add(new Movie(m));
        return movies;
    }
}
