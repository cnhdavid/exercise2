package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
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
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class HomeController implements Initializable {
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

    public List<Movie> allMovies;
    public TextField releaseYearField;
    public TextField ratingFilterField;

    protected ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    protected SortedState sortedState;
    private final MovieAPI movieAPI = new MovieAPI();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadMovies();
        initializeLayout();
    }

    public void loadMovies() {
        try {
            List<Movie> movies = movieAPI.fetchMovies("", "","", "");
            allMovies = movies;
            observableMovies.clear();
            observableMovies.addAll(movies);
            sortedState = sortedState.NONE;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initializeLayout() {
        movieListView.setItems(observableMovies);   // set the items of the listview to the observable list
        movieListView.setCellFactory(movieListView -> new MovieCell()); // apply custom cells to the listview

        Object[] genres = Genre.values();   // get all genres
        genreComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        genreComboBox.getItems().addAll(genres);    // add all genres to the combobox
        genreComboBox.setPromptText("Filter by Genre");
    }

    public void sortMovies(){
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            sortMovies(SortedState.ASCENDING);
        } else if (sortedState == SortedState.ASCENDING) {
            sortMovies(SortedState.DESCENDING);
        }
    }
    // sort movies based on sortedState
    // by default sorted state is NONE
    // afterwards it switches between ascending and descending
    public void sortMovies(SortedState sortDirection) {
        if (sortDirection == SortedState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            sortedState = SortedState.ASCENDING;
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortedState = SortedState.DESCENDING;
        }
    }

    public List<Movie> filterByQuery(List<Movie> movies, String query){
        if(query == null || query.isEmpty()) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie ->
                        movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                movie.getDescription().toLowerCase().contains(query.toLowerCase())
                )
                .toList();
    }

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

    public List<Movie> filterByRating(List<Movie> movies, String rating) {
        if (rating == null || rating.isEmpty()) return movies;

        double ratingValue;
        try {
            ratingValue = Double.parseDouble(rating);
        } catch (NumberFormatException e) {
            // Wenn das Rating keine gültige Zahl ist, gib die unveränderte Liste zurück
            return movies;
        }

        return movies.stream()
                .filter(movie -> movie.getRating() == ratingValue)
                .collect(Collectors.toList());
    }

    public void applyAllFilters(String searchQuery, Object genre, String rating, String releaseYearStr) {
        try {
            // Rufe die Filme von der API ab
            List<Movie> movies = movieAPI.fetchMovies("", genre, rating, releaseYearStr);

            // Filtere die Filme basierend auf dem Suchtext
            List<Movie> filteredMovies = filterByQuery(movies, searchQuery);

            filteredMovies = filterByRating(filteredMovies, ratingFilterField.getText());


            // Aktualisiere die ObservableList mit den gefilterten Filmen
            observableMovies.clear();
            observableMovies.addAll(filteredMovies);

            // Optional: Drucke die Anzahl der gefilterten Filme für Debugging-Zwecke
            System.out.println("Anzahl der gefilterten Filme: " + filteredMovies.size());

        } catch (IOException e) {
            System.err.println("Fehler beim Abrufen der Filme: " + e.getMessage());
            // Handle den Fehler entsprechend deiner Anwendungslogik
        }
    }


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



        // Call the applyAllFilters method with the obtained parameters
        applyAllFilters(searchText, genre, rating, releaseYearStr);
    }


    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }

    public Map<Genre, Long> countMoviesInEachGenre(List<Movie> movies) {
        Map<Genre, Long> genreCounts = Arrays.stream(Genre.values())
                .collect(Collectors.toMap(
                        genre -> genre,
                        genre -> movies.stream().filter(movie -> movie.getGenres().contains(genre)).count()
                ));

        genreCounts.forEach((genre, count) -> System.out.println(genre + ": " + count));

        return genreCounts;
    }


    public String getMostPopularActor(List<Movie> movies) {
        return movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    } //WORKS

    public int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .mapToInt(movie -> movie.getTitle().length())
                .max()
                .orElse(0);
    } //WORKS

        public long countMoviesFrom(List<Movie> movies, String director) {
            return movies.stream()
                    .filter(movie -> movie.getDirectors().contains(director))
                    .count();
        }

    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> Integer.parseInt(movie.getReleaseYear()) >= startYear && Integer.parseInt(movie.getReleaseYear()) <= endYear)
                .collect(Collectors.toList());
    }
}