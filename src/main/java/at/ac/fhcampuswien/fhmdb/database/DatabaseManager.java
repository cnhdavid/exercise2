package at.ac.fhcampuswien.fhmdb.database;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
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

    public DatabaseManager(){
        try {
            createConnectSource();
            movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
            watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
            createTables();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public static DatabaseManager getDatabaseManager(){
        if(instance==null) instance = new DatabaseManager();
        return instance;
    }

    public static void createConnectSource() throws SQLException {
        connectionSource = new JdbcConnectionSource(DATABASE_URL,USERNAME,PASSWORD);
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    private static void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
    }

    public Dao<MovieEntity, Long> getMovieDao() {
        return this.movieDao;
    }

    public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        return this.watchlistDao;
    }
}
