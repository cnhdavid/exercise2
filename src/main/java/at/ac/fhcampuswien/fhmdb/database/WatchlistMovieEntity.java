package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;


public class WatchlistMovieEntity extends MovieEntity {
    long dbId;

    String id;

    public WatchlistMovieEntity(){

    }
    public WatchlistMovieEntity(Movie m){
        super(m);
    }

    public static List<Movie> watchlistToMovies(List<WatchlistMovieEntity> watchlistMovieEntities){
        List<Movie> movies = new ArrayList<>();
        for(WatchlistMovieEntity m : watchlistMovieEntities) movies.add(new Movie(m));
        return movies;
    }


}
