package at.ac.fhcampuswien.fhmdb.database;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "movies")
public class MovieEntity {
    @DatabaseField(generatedId = true)
    private long dbId;
    @DatabaseField
    private String id;
    @DatabaseField
    private String title;
    @DatabaseField
    private String description;
    @DatabaseField
    private List<Genre> genres;
    @DatabaseField
    private int releaseYear;
    @DatabaseField
    private String imgUrl;
    @DatabaseField
    private int lengthInMinutes; // in minutes
    @DatabaseField
    private double rating; // 0-10

    public MovieEntity(){

    }

    public MovieEntity(Movie g) {
        id = g.getId();
        title = g.getTitle();
        description = g.getDescription();
        genres = g.getGenres();
        releaseYear = Integer.parseInt(g.getReleaseYear());
        imgUrl = g.getImgUrl();
        lengthInMinutes = g.getLengthInMinutes();
        rating = g.getRating();
    }

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public List<Genre> getGenres() {
        return genres;
    }
    public int getReleaseYear() {
        return releaseYear;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public int getLengthInMinutes() {
        return lengthInMinutes;
    }
    public double getRating() {
        return rating;
    }

    private String genresToString(List<Genre> genres) {
        return String.join(",",genres.toString());
    }

    public static List<MovieEntity> fromMovies(List<Movie> movies) {
        List<MovieEntity> movieEntities = new ArrayList<>();
        for (Movie m : movies) movieEntities.add(new MovieEntity(m));

        return movieEntities;
    }

    public static List<Movie> toMovies(List<MovieEntity> movieEntities) {
        List<Movie> movies = new ArrayList<>();
        for(MovieEntity m : movieEntities) movies.add(new Movie(m));
        return movies;
    }

}
