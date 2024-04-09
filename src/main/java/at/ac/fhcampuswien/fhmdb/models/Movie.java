package at.ac.fhcampuswien.fhmdb.models;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Movie {
    private String id;
    private String title;
    private List<Genre> genres;
    private final String releaseYear;
    private String description;
    private String imgUrl;
    private int lengthInMinutes;
    private List<String> directors;
    private List<String> writers;
    private List<String> mainCast;
    private double rating;


    public Movie(String title, String description, List<Genre> genres, List<String> mainCast, String director, int releaseYear, double rating) {
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.mainCast = mainCast;
        this.directors = Collections.singletonList(director);
        this.releaseYear = String.valueOf(releaseYear);
        this.rating = rating;
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
    public List<String> getDirector() {
        return this.directors;
    }

    // Getter-Methode für releaseYear hinzugefügt
    public String getReleaseYear() {
        return releaseYear;
    }


    public static List<Movie> initializeMovies() throws IOException, IOException {
        MovieAPI movieAPI = new MovieAPI();

        List<Movie> movies = movieAPI.fetchMovies("","","", "");

        return movies;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public int getLengthInMinutes() {
        return this.lengthInMinutes;
    }

    public List getDirectors() {
        return this.directors;
    }

    public List getWriters() {
        return  this.writers;
    }

    public double getRating() {
        return this.rating;
    }
}
