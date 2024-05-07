package at.ac.fhcampuswien.fhmdb.database;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:h2:mem:default";
    private static final String USERNAME = "prog";
    private static final String PASSWORD = "prog";

    private static ConnectionSource connectionSource;

    public static void init() throws DatabaseException {
        try {
            connectionSource = new JdbcConnectionSource(DATABASE_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new DatabaseException("Error initializing database connection", e);
        }
    }

    public static ConnectionSource getConnectionSource() {
        return connectionSource;
    }
}
