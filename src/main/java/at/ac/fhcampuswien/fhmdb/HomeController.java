package at.ac.fhcampuswien.fhmdb;

// Import-Anweisungen

// Import-Anweisungen

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// Kommentare hinzugefügt
public class HomeController implements Initializable {
    private DatabaseManager databaseManager;
    // FXML-Elemente
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXButton sortBtn;

    @FXML
    public TextField ratingFilterField;

    public TextField releaseYearField;

    // Liste aller Filme
    public List<Movie> allMovies;

    // Liste der Filme, die im UI angezeigt werden
    public ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    // Aktueller Sortierstatus
    public SortedState sortedState;

    // Instanz der MovieAPI-Klasse
    private final MovieAPI movieAPI = new MovieAPI();
    public Button watchlistBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadMovies(); // Filme laden
        initializeLayout(); // Layout initialisieren
        try {
            // Initialisiere den DatabaseManager
            databaseManager = new DatabaseManager();
            databaseManager.init();

            // Überprüfe die Verbindung zur Datenbank
            checkDatabaseConnection();

            // Weitere Initialisierungsschritte hier...
        } catch (SQLException e) {
            e.printStackTrace();
            // Behandle den Fehler angemessen
        }
    }
    private void checkDatabaseConnection() {
        if (databaseManager != null && databaseManager.getConnectionSource() != null) {
            System.out.println("Datenbankverbindung hergestellt.");
        } else {
            System.err.println("Fehler beim Herstellen der Verbindung zur Datenbank.");
        }
    }

    // Filme laden
    public void loadMovies() {
        try {
            // Filme von der API abrufen
            List<Movie> movies = movieAPI.fetchMovies("", "", "", "");
            allMovies = movies;
            observableMovies.clear();
            observableMovies.addAll(movies);
            sortedState = sortedState.NONE;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Layout initialisieren
    public void initializeLayout() {
        // Filme der ListView hinzufügen
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell());

        // Genres zur ComboBox hinzufügen
        Object[] genres = Genre.values();
        genreComboBox.getItems().add("No filter");
        genreComboBox.getItems().addAll(genres);
        genreComboBox.setPromptText("Filter by Genre");
    }

    // Filme sortieren
    public void sortMovies(){
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            sortMovies(SortedState.ASCENDING);
        } else if (sortedState == SortedState.ASCENDING) {
            sortMovies(SortedState.DESCENDING);
        }
    }

    // Filme sortieren (überladen)
    public void sortMovies(SortedState sortDirection) {
        if (sortDirection == SortedState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getRating));
            sortedState = SortedState.ASCENDING;
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getRating).reversed());
            sortedState = SortedState.DESCENDING;
        }
    }

    // Filme nach Suchbegriff filtern
    public List<Movie> filterByQuery(List<Movie> movies, String query){
        if(query == null || query.isEmpty()) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie ->
                        movie.getTitle().toLowerCase().contains(query.toLowerCase())
                        //       movie.getDescription().toLowerCase().contains(query.toLowerCase())  // wurde nur auskommentiert, da das entfernen nicht in der Angabe stand
                )
                .toList();
    }

    // Filme nach Genre filtern
    public List<Movie> filterByGenre(List<Movie> movies, Genre genre){
        if(genre == null) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie -> movie.getGenres().contains(genre))
                .toList();
    }

    // Filme nach Bewertung filtern
    public List<Movie> filterByRating(List<Movie> movies, String rating) {
        if (rating == null || rating.isEmpty()) return movies;

        double ratingValue;
        try {
            ratingValue = Double.parseDouble(rating);
        } catch (NumberFormatException e) {
            return movies;
        }

        boolean isDecimal = rating.contains(".");

        return movies.stream()
                .filter(movie -> {
                    double movieRating = movie.getRating();
                    if (isDecimal) {
                        return movieRating >= ratingValue;
                    } else {
                        int firstDigit = (int) ratingValue;
                        return movieRating >= firstDigit;
                    }
                })
                .collect(Collectors.toList());
    }

    // Alle Filter anwenden
    public void applyAllFilters(String searchQuery, Object genre, String rating, String releaseYearStr) {
        try {
            // Filme von der API abrufen
            List<Movie> movies = movieAPI.fetchMovies(searchQuery, genre, rating, releaseYearStr);
            List<Movie> filteredMovies = filterByQuery(movies, searchQuery);

            if (!rating.isEmpty()) {
                filteredMovies = filterByRating(filteredMovies, rating);
            }

            observableMovies.clear();
            observableMovies.addAll(filteredMovies);

            System.out.println("Anzahl der gefilterten Filme: " + filteredMovies.size());

        } catch (IOException e) {
            System.err.println("Fehler beim Abrufen der Filme: " + e.getMessage());
        }
    }

    // Event-Handler für Suchbutton-Klick
    public void searchBtnClicked(ActionEvent actionEvent) {
        String searchText = searchField.getText();
        String rating = ratingFilterField.getText();
        String releaseYearStr = releaseYearField.getText();
        Object selectedGenre = genreComboBox.getValue();
        Object genre = null;

        if (selectedGenre instanceof String && "No Filter".equals(selectedGenre)) {
            genre = null;
        } else if (selectedGenre instanceof Genre) {
            genre = selectedGenre;
        }

        applyAllFilters(searchText, genre, rating, releaseYearStr);
    }

    // Event-Handler für Sortierbutton-Klick
    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }

    // Filme in jedem Genre zählen
    public Map<Genre, Long> countMoviesInEachGenre(List<Movie> movies) {
        Map<Genre, Long> genreCounts = Arrays.stream(Genre.values())
                .collect(Collectors.toMap(
                        genre -> genre,
                        genre -> movies.stream().filter(movie -> movie.getGenres().contains(genre)).count()
                ));

        genreCounts.forEach((genre, count) -> System.out.println(genre + ": " + count));

        return genreCounts;
    }

    // Den beliebtesten Schauspieler finden
    public String getMostPopularActor(List<Movie> movies) {
        return movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // Längsten Filmtitel finden
    public int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .mapToInt(movie -> movie.getTitle().length())
                .max()
                .orElse(0);
    }

    // Filme eines Regisseurs zählen
    public long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
    }

    // Filme zwischen zwei Jahren erhalten
    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> Integer.parseInt(movie.getReleaseYear()) >= startYear && Integer.parseInt(movie.getReleaseYear()) <= endYear)
                .collect(Collectors.toList());
    }



    @FXML
    private void watchlistBtnClicked(ActionEvent event) {
        try {
            // Load the watchlist view FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("watchlist.fxml"));
            Parent watchlistViewRoot = loader.load();

            // Create a new scene with the loaded FXML root node
            Scene watchlistScene = new Scene(watchlistViewRoot);

            // Get the current stage (window)
            Stage currentStage = (Stage) watchlistBtn.getScene().getWindow();
            currentStage.setWidth(820);
            currentStage.setHeight(620);

            // Set the scene of the stage to the watchlist scene
            currentStage.setScene(watchlistScene);

            // Show the stage
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential errors (e.g., FXML file not found)
        }
    }
}
