package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {


    private static HomeController homeController;

    @BeforeAll
    static void init() {
        homeController = new HomeController();
    }

    @Test
    void at_initialization_allMovies_and_observableMovies_should_be_filled_and_equal() {
        homeController.loadMovies();
        assertEquals(homeController.allMovies, homeController.observableMovies);
    }



    @Test
    void query_filter_with_null_movie_list_throws_exception(){
        // given
        homeController.loadMovies();
        String query = "IfE";

        // when and then
        assertThrows(IllegalArgumentException.class, () -> homeController.filterByQuery(null, query));
    }

    @Test
    void query_filter_with_null_value_returns_unfiltered_list() {
        // given
        homeController.loadMovies();
        String query = null;

        // when
        List<Movie> actual = homeController.filterByQuery(homeController.observableMovies, query);

        // then
        assertEquals(homeController.observableMovies, actual);
    }

    @Test
    public void testGetMostPopularActor() {
        List<Movie> movies = Arrays.asList(
                new Movie("Movie 1", "Description 1", Arrays.asList(Genre.ACTION, Genre.DRAMA),
                        Arrays.asList("Actor 1", "Actor 2", "Actor 3"), "Director 1", 2000, 8.5),
                new Movie("Movie 2", "Description 2", Arrays.asList(Genre.COMEDY),
                        Arrays.asList("Actor 2", "Actor 3"), "Director 2", 2005, 7.5),
                new Movie("Movie 3", "Description 3", Arrays.asList(Genre.DRAMA),
                        Arrays.asList("Actor 1", "Actor 3"), "Director 1", 2010, 9.0)
        );

        String mostPopularActor = homeController.getMostPopularActor(movies);
        assertEquals("Actor 3", mostPopularActor);
    }

    @Test
    public void testGetLongestMovieTitle() {
        List<Movie> movies = Arrays.asList(
                new Movie("Avengers", "Description 1", null, null, null, 0, 0.0),
                new Movie("Spider-Man: Far From Home", "Description 2", null, null, null, 0, 0.0),
                new Movie("The Shawshank Redemption", "Description 3", null, null, null, 0, 0.0)
        );

        int longestTitleLength = homeController.getLongestMovieTitle(movies);
        assertEquals(25, longestTitleLength);
    }

    @Test
    public void testCountMoviesFrom() {
        // Create a list of movies with different directors
        List<Movie> movies = Arrays.asList(
                new Movie("Movie 1", "", null, null, "Director A", 0, 0.0),
                new Movie("Movie 2", "", null, null, "Director B", 0, 0.0),
                new Movie("Movie 3", "", null, null, "Director A", 0, 0.0),
                new Movie("Movie 4", "", null, null, "Director C", 0, 0.0),
                new Movie("Movie 5", "", null, null, "Director A", 0, 0.0)
        );

        // Count the number of movies directed by "Director A"
        long moviesDirectedByDirectorA = homeController.countMoviesFrom(movies, "Director A");

        // Verify that the count is correct
        assertEquals(3, moviesDirectedByDirectorA);
    }


    @Test
    void genre_filter_with_null_value_returns_unfiltered_list() {
        // given
        homeController.loadMovies();
        Genre genre = null;

        // when
        List<Movie> actual = homeController.filterByGenre(homeController.observableMovies, genre);

        // then
        assertEquals(homeController.observableMovies, actual);
    }

    /*
    @Test
    void genre_filter_returns_all_movies_containing_given_genre() {
        // given
        homeController.loadMovies();
        Genre genre = Genre.DRAMA;

        // when
        List<Movie> actual = homeController.filterByGenre(homeController.observableMovies, genre);

        // then
        int expectedCount = HomeController.countMoviesInEachGenre(homeController.allMovies, genre);
        assertEquals(expectedCount, actual.size());
    }

     */

    @Test
    void no_filtering_ui_if_empty_query_or_no_genre_is_set() {
        // given
        homeController.loadMovies();

        // when
        homeController.applyAllFilters("", null, null,null);

        // then
        assertEquals(homeController.allMovies, homeController.observableMovies);
    }


    @Test
    public void testGetMoviesBetweenYears() {
        // Create some sample movies
        List<Movie> movies = Arrays.asList(
                new Movie("Movie 1", "", null, null, "Director A", 2015, 0.0),
                new Movie("Movie 2", "", null, null, "Director B", 2016, 0.0),
                new Movie("Movie 3", "", null, null, "Director A", 2019, 0.0),
                new Movie("Movie 4", "", null, null, "Director C", 2020, 0.0),
                new Movie("Movie 5", "", null, null, "Director A", 2021, 0.0)
        );

        // Set the start and end years for filtering
        int startYear = 2015;
        int endYear = 2019;

        // Call the method to get movies between the specified years
        List<Movie> filteredMovies = new HomeController().getMoviesBetweenYears(movies, startYear, endYear);

        // Check if the filteredMovies list contains the expected movies
        assertEquals(3, filteredMovies.size()); // Expecting 3 movies between years 2005 and 2015

        // You can further assert for specific movie titles or other properties if needed
    }

    @Test
    public void testCountMoviesInEachGenre() {


        // You can manually verify the output printed on the console
    }

}