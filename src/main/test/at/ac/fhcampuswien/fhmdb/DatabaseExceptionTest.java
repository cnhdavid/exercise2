package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseExceptionTest {

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
}

