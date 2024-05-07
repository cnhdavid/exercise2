package at.ac.fhcampuswien.fhmdb.models;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Klasse zur Repräsentation von Filmen
public class Movie {
    private String id; // ID des Films
    private String title; // Titel des Films
    private final List<Genre> genres; // Liste der Genres des Films
    private final String releaseYear; // Veröffentlichungsjahr des Films
    private String description; // Beschreibung des Films
    private String imgUrl; // URL des Filmplakats
    private int lengthInMinutes; // Länge des Films in Minuten
    private List<String> directors; // Liste der Regisseure des Films
    private List<String> writers; // Liste der Drehbuchautoren des Films
    private List<String> mainCast; // Liste der Hauptdarsteller des Films
    private double rating; // Bewertung des Films

    // Standardkonstruktor
    public Movie() {
        this.genres = new ArrayList<>();
        this.releaseYear = "";
    }

    // Konstruktor zur manuellen Erstellung eines Films
    public Movie(String title, String description, List<Genre> genres, List<String> mainCast, String director, int releaseYear, double rating) {
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.mainCast = mainCast;
        this.directors = Collections.singletonList(director);
        this.releaseYear = String.valueOf(releaseYear);
        this.rating = rating;
    }

    // Konstruktor, der ein MovieEntity-Objekt entgegennimmt und die Werte in die entsprechenden Felder setzt
    public Movie(MovieEntity movieEntity) {
        this.id = movieEntity.getId();
        this.title = movieEntity.getTitle();
        this.description = movieEntity.getDescription();
        this.genres = stringToGenres(movieEntity.getGenres());
        this.releaseYear = String.valueOf(movieEntity.getReleaseYear());
        this.imgUrl = movieEntity.getImgUrl();
        this.lengthInMinutes = movieEntity.getLengthInMinutes();
        this.rating = movieEntity.getRating();
    }

    // Methode zum Konvertieren einer Zeichenkette von Genres in eine Liste von Genre-Objekten
    private List<Genre> stringToGenres(String string) {
        List<String> list = List.of(string.substring(1, string.length() - 1).split(", "));
        List<Genre> genres = new ArrayList<>();
        for (String s : list) genres.add(Genre.valueOf(s));
        return genres;
    }

    // Überschriebene Methode zum Vergleichen von zwei Movie-Objekten
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Movie other)) {
            return false;
        }
        return this.title.equals(other.title) && this.description.equals(other.description) && this.genres.equals(other.genres);
    }

    // Getter-Methoden für die Felder des Movie-Objekts
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

    public List<String> getMainCast() {
        return mainCast;
    }

    public List<String> getDirectors() {
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

    public List<String> getWriters() {
        return writers;
    }

    public double getRating() {
        return rating;
    }

    // Methode zum Konvertieren einer Liste von Movie-Objekten in eine Liste von MovieEntity-Objekten
    public static List<MovieEntity> fromMovies(List<Movie> movies) {
        List<MovieEntity> movieEntities = new ArrayList<>();
        for (Movie m : movies) movieEntities.add(new MovieEntity(m));

        return movieEntities;
    }

    // Methode zum Konvertieren einer Liste von MovieEntity-Objekten in eine Liste von Movie-Objekten
    public static List<Movie> toMovies(List<MovieEntity> movieEntities) {
        List<Movie> movies = new ArrayList<>();
        for (MovieEntity m : movieEntities) movies.add(new Movie(m));
        return movies;
    }

    // Methode zum Initialisieren einer Liste von Filmen über die MovieAPI
    public static List<Movie> initializeMovies() throws IOException, MovieApiException {
        MovieAPI movieAPI = new MovieAPI();
        return movieAPI.fetchMovies("", "", "", "");
    }

    // Überschriebene toString-Methode, die die ID des Films zurückgibt
    @Override
    public String toString() {
        return this.id;
    }
}
