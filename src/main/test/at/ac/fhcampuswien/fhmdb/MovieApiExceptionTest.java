package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieApiExceptionTest {

    @Test
    void testConstructorWithMessage() {
        MovieApiException exception = new MovieApiException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    void testConstructorWithCause() {
        Throwable cause = new Throwable("Cause of the exception");
        MovieApiException exception = new MovieApiException(cause);
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new Throwable("Cause of the exception");
        MovieApiException exception = new MovieApiException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
