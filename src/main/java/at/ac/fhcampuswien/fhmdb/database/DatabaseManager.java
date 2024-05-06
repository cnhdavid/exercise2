package at.ac.fhcampuswien.fhmdb.database;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:h2:mem:default";
    private static final String USERNAME = "prog";
    private static final String PASSWORD = "prog";

    private static ConnectionSource connectionSource;

    public static void init() throws SQLException {
        connectionSource = new JdbcConnectionSource(DATABASE_URL, USERNAME, PASSWORD);
    }

    public static ConnectionSource getConnectionSource() {
        return connectionSource;
    }
}
