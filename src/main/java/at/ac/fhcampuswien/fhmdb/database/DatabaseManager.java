package at.ac.fhcampuswien.fhmdb.database;
import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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

    public DatabaseManager() {
        try {
            createConnectSource();
            movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
            watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
            createTables();

            // Cache Filme von der API in die Datenbank
            cacheMoviesFromAPI();
        } catch (SQLException e) {
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

    public void cacheMoviesFromAPI() {
        try {
            // Filme von der API abrufen
            MovieAPI movieAPI = new MovieAPI();
            List<Movie> movies = movieAPI.fetchMovies("", "", "", "");

            // Filme in die Datenbank einfügen
            for (Movie movie : movies) {
                MovieEntity movieEntity = new MovieEntity(movie);
                movieDao.createOrUpdate(movieEntity);
            }

            System.out.println("Filme von der API wurden erfolgreich in die Datenbank gecached.");
        } catch (IOException | MovieApiException | SQLException e) {
            System.err.println("Fehler beim Cachen der Filme von der API: " + e.getMessage());
        }
    }

}
