package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        homeController = new HomeController();
    }

    // Überprüft, ob bei Initialisierung alle Filme geladen und sowohl observableMovies als auch allMovies gefüllt sind und gleich viele Elemente enthalten.
    @Test
    void at_initialization_allMovies_and_observableMovies_should_be_filled_and_equal() throws IOException {
        homeController.loadMovies();
        List<Movie> allMovies = homeController.observableMovies;
        assertEquals(allMovies.size(), homeController.allMovies.size());
    }

    // Testet, ob eine Sortierung in aufsteigender Reihenfolge erfolgt, wenn zuvor noch keine Sortierung angewendet wurde.
    @Test
    void if_not_yet_sorted_sort_is_applied_in_ascending_order() throws IOException {
        homeController.loadMovies();
        homeController.sortedState = SortedState.NONE;
        homeController.sortMovies();
        assertTrue(isSortedAscending(homeController.observableMovies));
    }

    // Überprüft, ob nach einer vorherigen aufsteigenden Sortierung eine absteigende Sortierung erfolgt.
    @Test
    void if_last_sort_ascending_next_sort_should_be_descending() throws IOException {
        homeController.loadMovies();
        homeController.sortedState = SortedState.ASCENDING;
        homeController.sortMovies();
        assertTrue(isSortedDescending(homeController.observableMovies));
    }

    // Testet, ob nach einer vorherigen absteigenden Sortierung eine aufsteigende Sortierung erfolgt.
    @Test
    void if_last_sort_descending_next_sort_should_be_ascending() throws IOException {
        homeController.loadMovies();
        homeController.sortedState = SortedState.DESCENDING;
        homeController.sortMovies();
        assertTrue(isSortedAscending(homeController.observableMovies));
    }

    // Überprüft, ob die Abfrage korrekt ignoriert, ob die Groß- oder Kleinschreibung übereinstimmt.
    @Test
    void query_filter_matches_with_lower_and_uppercase_letters() throws IOException {
        homeController.loadMovies();
        String query = "IfE";
        List<Movie> filteredMovies = homeController.filterByQuery(homeController.observableMovies, query);
        assertEquals(2, filteredMovies.size());
    }

    // Testet, ob eine Ausnahme ausgelöst wird, wenn die Filmliste null ist.
    @Test
    void query_filter_with_null_movie_list_throws_exception(){
        assertThrows(IllegalArgumentException.class, () -> homeController.filterByQuery(null, "query"));
    }

    // Überprüft, ob bei einer null-Abfrage die ungefilterte Filmliste zurückgegeben wird.
    @Test
    void query_filter_with_null_value_returns_unfiltered_list() throws IOException {
        homeController.loadMovies();
        List<Movie> filteredMovies = homeController.filterByQuery(homeController.observableMovies, null);
        assertEquals(homeController.observableMovies.size(), filteredMovies.size());
    }

    // Testet, ob bei einem null-Genre die ungefilterte Filmliste zurückgegeben wird.
    @Test
    void genre_filter_with_null_value_returns_unfiltered_list() throws IOException {
        homeController.loadMovies();
        List<Movie> filteredMovies = homeController.filterByGenre(homeController.observableMovies, null);
        assertEquals(homeController.observableMovies.size(), filteredMovies.size());
    }

    // Überprüft, ob alle Filme mit einem bestimmten Genre korrekt gefiltert werden.
    @Test
    void genre_filter_returns_all_movies_containing_given_genre() throws IOException {
        homeController.loadMovies();
        List<Movie> filteredMovies = homeController.filterByGenre(homeController.observableMovies, Genre.DRAMA);
        assertTrue(filteredMovies.stream().allMatch(movie -> movie.getGenres().contains(Genre.DRAMA)));
    }

    // Testet, ob keine Filterung erfolgt, wenn keine Abfrage oder kein Genre festgelegt ist.
    @Test
    void no_filtering_ui_if_empty_query_or_no_genre_is_set() throws IOException {
        homeController.loadMovies();
        homeController.applyAllFilters("", null, "", "");
        assertEquals(homeController.allMovies.size(), homeController.observableMovies.size());
    }

    // Überprüft, ob alle Filter korrekt auf die Filme angewendet werden.
    @Test
    void apply_all_filters_should_filter_movies_correctly() {

        String searchQuery = "Action";
        Genre genre = Genre.ACTION;
        String rating = "7.5";
        String releaseYearStr = "2000";

        homeController.applyAllFilters(searchQuery, genre, rating, releaseYearStr);
        List<Movie> filteredMovies = homeController.observableMovies;
        assertTrue(filteredMovies.stream().allMatch(movie ->
                movie.getTitle().toLowerCase().contains(searchQuery.toLowerCase()) &&
                        movie.getGenres().contains(genre) &&
                        movie.getRating() >= Double.parseDouble(rating) &&
                        movie.getReleaseYear().equals(releaseYearStr)
        ));
    }

    // Testet, ob die Anzahl der Filme eines bestimmten Regisseurs korrekt gezählt wird.
    @Test
    void count_movies_from_specific_director() throws IOException {
        homeController.loadMovies();
        long count = homeController.countMoviesFrom(homeController.observableMovies, "Steven Spielberg");
        assertEquals(2, count);
    }

    // Testet, ob bei einer Filterung nach Bewertung eine leere Liste zurückgegeben wird, wenn keine Filme den Kriterien entsprechen.
    @Test
    void filter_by_rating_should_return_empty_list_if_no_movies_meet_the_criteria() throws IOException {

        homeController.loadMovies();
        String rating = "10.0";

        List<Movie> filteredMovies = homeController.filterByRating(homeController.observableMovies, rating);
        assertTrue(filteredMovies.isEmpty());
    }

    // Testet, ob bei einer null- oder leeren Bewertung alle Filme zurückgegeben werden.
    @Test
    void filter_by_rating_should_return_all_movies_if_rating_is_null_or_empty() throws IOException {

        homeController.loadMovies();

        List<Movie> filteredMoviesWithNullRating = homeController.filterByRating(homeController.observableMovies, null);
        List<Movie> filteredMoviesWithEmptyRating = homeController.filterByRating(homeController.observableMovies, "");

        assertEquals(homeController.observableMovies.size(), filteredMoviesWithNullRating.size());
        assertEquals(homeController.observableMovies.size(), filteredMoviesWithEmptyRating.size());
    }

    // Überprüft, ob die Filterung nach Erscheinungsjahr korrekt Filme im angegebenen Bereich zurückgibt.
    @Test
    void filter_by_release_year_should_return_movies_within_specified_range() throws IOException {

        homeController.loadMovies();
        int startYear = 2000;
        int endYear = 2010;

        List<Movie> filteredMovies = homeController.getMoviesBetweenYears(homeController.observableMovies, startYear, endYear);

        for (Movie movie : filteredMovies) {
            int releaseYear = Integer.parseInt(movie.getReleaseYear());
            assertTrue(releaseYear >= startYear && releaseYear <= endYear);
        }
    }

    // Testet, ob bei einer Filterung nach Erscheinungsjahr eine leere Liste zurückgegeben wird, wenn keine Filme den Kriterien entsprechen.
    @Test
    void filter_by_release_year_should_return_empty_list_if_no_movies_meet_the_criteria() throws IOException {

        homeController.loadMovies();
        int startYear = 2025;
        int endYear = 2030;

        List<Movie> filteredMovies = homeController.getMoviesBetweenYears(homeController.observableMovies, startYear, endYear);

        assertTrue(filteredMovies.isEmpty());
    }

    // Überprüft, ob der am häufigsten vorkommende Schauspieler korrekt ermittelt wird.
    @Test
    void get_most_popular_actor_returns_correct_result() throws IOException {

        homeController.loadMovies();


        String mostPopularActor = homeController.getMostPopularActor(homeController.observableMovies);

        assertEquals("Leonardo DiCaprio", mostPopularActor);
    }


    // Testet, ob die Länge des längsten Filmtitels korrekt zurückgegeben wird.
    @Test
    void get_longest_movie_title_returns_correct_length() throws IOException {

        homeController.loadMovies();

        int longestTitleLength = homeController.getLongestMovieTitle(homeController.observableMovies);

        assertEquals(46, longestTitleLength);
    }


    // Helper method to check if the movies are sorted in ascending order by rating
    private boolean isSortedAscending(List<Movie> movies) {
        for (int i = 0; i < movies.size() - 1; i++) {
            if (movies.get(i).getRating() > movies.get(i + 1).getRating()) {
                return false;
            }
        }
        return true;
    }

    // Helper method to check if the movies are sorted in descending order by rating
    private boolean isSortedDescending(List<Movie> movies) {
        for (int i = 0; i < movies.size() - 1; i++) {
            if (movies.get(i).getRating() < movies.get(i + 1).getRating()) {
                return false;
            }
        }
        return true;
    }
}
