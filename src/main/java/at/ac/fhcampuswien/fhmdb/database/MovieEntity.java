package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

// Datenbanktabelle für Filme
@DatabaseTable(tableName = "movies")
public class MovieEntity {
    @DatabaseField(generatedId = true)
    private long dbId; // Datenbank-ID
    @DatabaseField
    private String id; // ID des Films
    @DatabaseField
    private String title; // Titel des Films
    @DatabaseField
    private String description; // Beschreibung des Films
    @DatabaseField
    private String genres; // Genres des Films (als String gespeichert)
    @DatabaseField
    private int releaseYear; // Veröffentlichungsjahr des Films
    @DatabaseField
    private String imgUrl; // URL des Filmplakats
    @DatabaseField
    private int lengthInMinutes; // Länge des Films in Minuten
    @DatabaseField
    private double rating; // Bewertung des Films (Skala von 0 bis 10)

    // Standardkonstruktor
    public MovieEntity() {

    }

    // Konstruktor, der ein Movie-Objekt entgegennimmt und die Werte in die entsprechenden Felder setzt
    public MovieEntity(Movie g) {
        id = g.getId();
        title = g.getTitle();
        description = g.getDescription();
        genres = genresToString(g.getGenres());
        releaseYear = Integer.parseInt(g.getReleaseYear());
        imgUrl = g.getImgUrl();
        lengthInMinutes = g.getLengthInMinutes();
        rating = g.getRating();
    }

    // Getter-Methoden für die Felder des MovieEntity-Objekts
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenres() {
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

    // Methode, die eine Liste von Genres in einen String umwandelt
    private String genresToString(List<Genre> genres) {
        return String.join(",", genres.toString());
    }

    // Methode zur Konvertierung einer Liste von Movie-Objekten in eine Liste von MovieEntity-Objekten
    public static List<MovieEntity> fromMovies(List<Movie> movies) {
        List<MovieEntity> movieEntities = new ArrayList<>();
        for (Movie m : movies) movieEntities.add(new MovieEntity(m));

        return movieEntities;
    }

    // Methode zur Konvertierung einer Liste von MovieEntity-Objekten in eine Liste von Movie-Objekten
    public static List<Movie> toMovies(List<MovieEntity> movieEntities) {
        List<Movie> movies = new ArrayList<>();
        for (MovieEntity m : movieEntities) movies.add(new Movie(m));
        return movies;
    }

    // Überschriebene toString-Methode, die den Titel und die Beschreibung des Films zurückgibt
    @Override
    public String toString() {
        // Beispiel, um Titel und Beschreibung zurückzugeben
        // Du kannst andere Attribute wählen oder hinzufügen, die du anzeigen möchtest
        return getTitle() + " - " + getDescription();
    }
}
