package at.ac.fhcampuswien.fhmdb.database;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.table.TableUtils;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:h2:mem:default";
    private static final String USERNAME = "prog";
    private static final String PASSWORD = "prog";

    private static ConnectionSource connectionSource;
    private Dao<MovieEntity, Long> movieDao;
    private Dao<WatchlistMovieEntity, Long> watchlistDao;
    private static DatabaseManager instance;

    public static DatabaseManager getDatabaseManager(){
        if(instance==null) instance = new DatabaseManager();
        return instance;
    }

    public static void init() throws DatabaseException {
        try {
            connectionSource = new JdbcConnectionSource(DATABASE_URL, USERNAME, PASSWORD);
            TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
        } catch (SQLException e) {
            throw new DatabaseException("Error initializing database connection", e);
        }
    }

    public static ConnectionSource getConnectionSource() {
        return connectionSource;
    }
    public Dao<MovieEntity, Long> getMovieDao() {
        return this.movieDao;
    }

    public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        return this.watchlistDao;
    }
}

