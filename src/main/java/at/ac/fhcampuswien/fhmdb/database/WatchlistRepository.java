package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    Dao<WatchlistMovieEntity, Long> watchlistDao;

    public WatchlistRepository() {
        this.watchlistDao = DatabaseManager.getDatabaseManager().getWatchlistDao();
    }


    public int removeFromWatchlist(String id) throws SQLException {
        return watchlistDao.delete(getMovieById(id));
    }
    public List<WatchlistMovieEntity> getWatchlist() throws SQLException {
        return watchlistDao.queryForAll();
    }

    public WatchlistMovieEntity getMovieById(String id) throws SQLException {
        List<WatchlistMovieEntity> list = getWatchlist();
        for (WatchlistMovieEntity m : list) {
            if(m.getId().equals(id)) return m;
        }
        return null;
    }
    public int addToWatchlist(WatchlistMovieEntity movie) throws SQLException {
        if(this.isInWatchlist(movie.getId())) return 0;
        System.out.println(movie);
        return watchlistDao.create(movie);
    }
    public boolean isInWatchlist (String id) throws SQLException {
        for(WatchlistMovieEntity m : getWatchlist()) if(m.getId() == id) return true;
        return false;
    }
}
