package at.ac.fhcampuswien.fhmdb.models;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Movie {
    private String id;
    private String title;
    private final List<Genre> genres;
    private final String releaseYear;
    private String description;
    private String imgUrl;
    private int lengthInMinutes;
    private List<String> directors;
    private List<String> writers;
    private List<String> mainCast;
    private double rating;

    @Override
    public String toString() {
        return this.id;
    }


    public Movie(String title, String description, List<Genre> genres, List<String> mainCast, String director, int releaseYear, double rating) {
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.mainCast = mainCast;
        this.directors = Collections.singletonList(director);
        this.releaseYear = String.valueOf(releaseYear);
        this.rating = rating;
    }

    public Movie(MovieEntity movieEntity){
        this.id=movieEntity.getId();
        this.title=movieEntity.getTitle();
        this.description=movieEntity.getDescription();
        this.genres=stringToGenres(movieEntity.getGenres());
        this.releaseYear= String.valueOf(movieEntity.getReleaseYear());
        this.imgUrl=movieEntity.getImgUrl();
        this.lengthInMinutes=movieEntity.getLengthInMinutes();
        this.rating=movieEntity.getRating();
    }

    private List<Genre> stringToGenres(String string){
        List<String> list = List.of(string.substring(1,string.length()-1).split(", "));
        List<Genre> genres = new ArrayList<>();
        for(String s : list) genres.add(Genre.valueOf(s));
        return genres;
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

    public List<String> getDirector() {
        return directors;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public List getDirectors() {
        return directors;
    }

    public List getWriters() {
        return  writers;
    }

    public double getRating() {
        return rating;
    }
    public String getId() {
        return id;
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
    public static List<Movie> initializeMovies() throws IOException, IOException, MovieApiException {
        MovieAPI movieAPI = new MovieAPI();

        List<Movie> movies = movieAPI.fetchMovies("","","", "");

        return movies;
    }
}

