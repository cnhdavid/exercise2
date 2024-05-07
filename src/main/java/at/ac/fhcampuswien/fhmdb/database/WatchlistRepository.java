package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    private Dao<WatchlistMovieEntity, Integer> watchlistDao;

    public WatchlistRepository() throws DatabaseException {
        try {
            watchlistDao = DaoManager.createDao(DatabaseManager.getConnectionSource(), WatchlistMovieEntity.class);
        } catch (SQLException e) {
            throw new DatabaseException("Error creating WatchlistRepository", e);
        }
    }

    public List<WatchlistMovieEntity> getAllWatchlistMovies() throws DatabaseException {
        try {
            return watchlistDao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("Error querying all watchlist movies", e);
        }
    }

    public void deleteAllWatchlistMovies() throws DatabaseException {
        try {
            watchlistDao.deleteBuilder().delete();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting all watchlist movies", e);
        }
    }

    public void deleteWatchlistMovie(WatchlistMovieEntity movie) throws DatabaseException {
        try {
            watchlistDao.delete(movie);
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting watchlist movie", e);
        }
    }

    public void addMovieToWatchlist(WatchlistMovieEntity movie) throws DatabaseException {
        try {
            watchlistDao.createOrUpdate(movie);
        } catch (SQLException e) {
            throw new DatabaseException("Error adding movie to watchlist", e);
        }
    }
}
