package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseExceptionTest {

    private DatabaseManager databaseManager;

    @BeforeEach
    void setUp() {
        databaseManager = new DatabaseManager();
        clearDatabase();
    }

    @Test
    void testConstructorWithMessage() {
        DatabaseException exception = new DatabaseException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    void testConstructorWithCause() {
        Throwable cause = new Throwable("Cause of the exception");
        DatabaseException exception = new DatabaseException(cause);
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new Throwable("Cause of the exception");
        DatabaseException exception = new DatabaseException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    private void clearDatabase() {
        try {
            databaseManager.getMovieDao().deleteBuilder().delete();
        } catch (SQLException e) {
            fail("SQLException while clearing database: " + e.getMessage());
        }
    }

    @Test
    public void testCacheMoviesFromAPI() {
        // Vor dem Cachen sollten keine Filme in der Datenbank sein
        try {
            List<MovieEntity> moviesBeforeCaching = databaseManager.getMovieDao().queryForAll();
            assertEquals(0, moviesBeforeCaching.size());

            // Cachen der Filme von der API
            databaseManager.cacheMoviesFromAPI();

            // Nach dem Cachen sollten Filme in der Datenbank sein
            List<MovieEntity> moviesAfterCaching = databaseManager.getMovieDao().queryForAll();
            assertNotEquals(0, moviesAfterCaching.size());

            // Hier könntest du weitere Überprüfungen hinzufügen, um sicherzustellen,
            // dass die Filme korrekt in die Datenbank eingefügt wurden
        } catch (SQLException e) {
            fail("SQLException: " + e.getMessage());
        }
    }
}
