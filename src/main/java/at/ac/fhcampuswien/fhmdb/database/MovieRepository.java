package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.List;

// Klasse zur Interaktion mit der Movie-Tabelle in der Datenbank
public class MovieRepository {
    private Dao<MovieEntity, Long> movieDao; // DAO für die Movie-Tabelle

    // Konstruktor, der den DAO für Movies initialisiert
    public MovieRepository() {
        this.movieDao = DatabaseManager.getDatabaseManager().getMovieDao();
    }

    // Methode zum Hinzufügen einer Liste von Filmen zur Datenbank
    public int addAllMovies(List<Movie> movies) throws SQLException {
        return movieDao.create(MovieEntity.fromMovies(movies));
    }

    // Methode zum Entfernen aller Filme aus der Datenbank
    public int removeAll() throws SQLException {
        return movieDao.delete(movieDao.queryForAll());
    }

    // Methode zum Abrufen aller Filme aus der Datenbank
    public List<MovieEntity> getAllMovies() throws SQLException, DatabaseException {
        List<MovieEntity> movieEntities = movieDao.queryForAll();
        if (movieEntities.isEmpty()) throw new DatabaseException(new RuntimeException());
        return movieDao.queryForAll();
    }

    // Methode zum Abrufen eines Films anhand seiner Datenbank-ID
    public MovieEntity getMovie(long dbId) throws SQLException {
        return movieDao.queryForId(dbId);
    }
}
