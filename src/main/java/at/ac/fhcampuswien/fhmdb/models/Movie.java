package at.ac.fhcampuswien.fhmdb.models;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movie {
    private final String title;
    private final String description;
    private final List<Genre> genres;
    private static MovieAPI movieAPI;
    private final List<String> mainCast;
    private final String director;
    private final int releaseYear;


    public Movie(String title, String description, List<Genre> genres, List<String> mainCast, String director, int releaseYear) {
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.mainCast = mainCast;
        this.director = director;
        this.releaseYear = releaseYear;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Movie other)) {
            return false;
        }
        return this.title.equals(other.title) && this.description.equals(other.description) && this.genres.equals(other.genres);
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
    public List<String> getMainCast() {
        return mainCast;
    }

    // Getter-Methode für director hinzugefügt
    public String getDirector() {
        return director;
    }

    // Getter-Methode für releaseYear hinzugefügt
    public int getReleaseYear() {
        return releaseYear;
    }


    public static List<Movie> initializeMovies() throws IOException, IOException {
        List<Movie> movies = movieAPI.fetchMovies("","");

        return movies;
    }
}
