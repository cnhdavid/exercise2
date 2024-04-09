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

    public void applyAllFilters(String searchQuery, Object genre, String rating, String releaseYearStr) {
        try {
            List<Movie> movies = movieAPI.fetchMovies(searchQuery,  genre, rating, releaseYearStr);

            observableMovies.clear();
            observableMovies.addAll(movies);

            // Count movies in each genre and print the results

        } catch (IOException e) {
            System.err.println("Error fetching movies: " + e.getMessage());
            // Handle the error or throw an exception as per your application's logic
        }
    }


    public void searchBtnClicked(ActionEvent actionEvent) {
        String searchText = searchField.getText();
        Object genre = (Genre) genreComboBox.getValue();
        String rating = ratingFilterField.getText();
        String releaseYearStr = releaseYearField.getText();



        // Call the applyAllFilters method with the obtained parameters
        applyAllFilters(searchText, genre, rating, releaseYearStr);
    }


    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }

    private void countMoviesInEachGenre(List<Movie> movies) {
        Map<Genre, Long> genreCounts = Arrays.stream(Genre.values())
                .collect(Collectors.toMap(
                        genre -> genre,
                        genre -> movies.stream().filter(movie -> movie.getGenres().contains(genre)).count()
                ));

        genreCounts.forEach((genre, count) -> System.out.println(genre + ": " + count));
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