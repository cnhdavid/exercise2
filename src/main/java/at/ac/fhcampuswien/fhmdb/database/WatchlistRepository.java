package at.ac.fhcampuswien.fhmdb.database;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    private Dao<WatchlistMovieEntity, Integer> watchlistDao;

    public WatchlistRepository() throws SQLException {
        watchlistDao = DaoManager.createDao(DatabaseManager.getConnectionSource(), WatchlistMovieEntity.class);
    }

    public List<WatchlistMovieEntity> getAllWatchlistMovies() throws SQLException {
        return watchlistDao.queryForAll();
    }

    public void deleteAllWatchlistMovies() throws SQLException {
        watchlistDao.deleteBuilder().delete();
    }

    public void deleteWatchlistMovie(WatchlistMovieEntity movie) throws SQLException {
        watchlistDao.delete(movie);
    }

    public void addMovieToWatchlist(WatchlistMovieEntity movie) throws SQLException {
        watchlistDao.createOrUpdate(movie);
    }
}
