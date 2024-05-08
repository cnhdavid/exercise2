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
    // Datenbank URL, Benutzername und Passwort
    private static final String DATABASE_URL = "jdbc:h2:E:/project/Prog2exercise2/data/moviedb;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE";
    private static final String USERNAME = "prog";
    private static final String PASSWORD = "prog";

    // Verbindungsquelle zur Datenbank
    private static ConnectionSource connectionSource;

    // DAOs für die Datenbanktabellen
    private Dao<MovieEntity, Long> movieDao;
    private Dao<WatchlistMovieEntity, Long> watchlistDao;

    // Singleton-Instanz des DatabaseManager
    private static DatabaseManager instance;

    // Konstruktor für den DatabaseManager
    public DatabaseManager() {
        try {
            // Verbindung zur Datenbank herstellen
            createConnectSource();

            // DAOs für die Datenbanktabellen initialisieren
            movieDao = DaoManager.createDao(connectionSource, MovieEntity.class);
            watchlistDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);

            // Tabellen in der Datenbank erstellen, falls sie nicht existieren
            createTables();

            // Filme von der API in die Datenbank cachen
            cacheMoviesFromAPI();
        } catch (SQLException e) {
            // Fehlerbehandlung bei SQL-Verbindungsfehlern
            System.out.println(e.getMessage());
        }
    }

    // Methode zur Rückgabe der Singleton-Instanz des DatabaseManager
    public static DatabaseManager getDatabaseManager(){
        if(instance==null) instance = new DatabaseManager();
        return instance;
    }

    // Methode zur Erstellung der Verbindungsquelle zur Datenbank
    public static void createConnectSource() throws SQLException {
        connectionSource = new JdbcConnectionSource(DATABASE_URL,USERNAME,PASSWORD);
    }

    // Methode zur Rückgabe der Verbindungsquelle zur Datenbank
    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    // Methode zur Erstellung der Datenbanktabellen, falls sie nicht existieren
    private static void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, MovieEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
    }

    // Methode zur Rückgabe des DAOs für die Movie-Tabelle
    public Dao<MovieEntity, Long> getMovieDao() {
        return this.movieDao;
    }

    // Methode zur Rückgabe des DAOs für die WatchlistMovie-Tabelle
    public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        return this.watchlistDao;
    }

    // Methode zum Cachen von Filmen von der API in die Datenbank
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

            // Erfolgsmeldung ausgeben
            System.out.println("Filme von der API wurden erfolgreich in die Datenbank gecached.");
        } catch (IOException | MovieApiException | SQLException e) {
            // Fehlermeldung bei Problemen mit der API oder Datenbank
            System.err.println("Fehler beim Cachen der Filme von der API: " + e.getMessage());
        }
    }
    public List<MovieEntity> getAllMovies() throws SQLException {
        if (movieDao == null) {
            throw new IllegalStateException("DAO not initialized");
        }
        return movieDao.queryForAll();
    }
}
