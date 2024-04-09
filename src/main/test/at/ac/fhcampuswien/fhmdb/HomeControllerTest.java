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

}