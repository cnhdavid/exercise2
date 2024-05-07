package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.List;

public class MovieRepository {
    private Dao<MovieEntity, Integer> movieDao;

    public MovieRepository() throws DatabaseException {
        try {
            movieDao = DaoManager.createDao(DatabaseManager.getConnectionSource(), MovieEntity.class);
        } catch (SQLException e) {
            throw new DatabaseException("Error creating MovieRepository", e);
        }
    }

    public List<MovieEntity> getAllMovies() throws DatabaseException {
        try {
            return movieDao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("Error querying all movies", e);
        }
    }

    public void deleteAllMovies() throws DatabaseException {
        try {
            movieDao.deleteBuilder().delete();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting all movies", e);
        }
    }

    public void deleteMovie(MovieEntity movie) throws DatabaseException {
        try {
            movieDao.delete(movie);
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting movie", e);
        }
    }

    public void addMovie(MovieEntity movie) throws DatabaseException {
        try {
            movieDao.createOrUpdate(movie);
        } catch (SQLException e) {
            throw new DatabaseException("Error adding movie", e);
        }
    }
}
