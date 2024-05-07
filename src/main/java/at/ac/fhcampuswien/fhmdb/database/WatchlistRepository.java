package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

// Klasse zur Interaktion mit der Watchlist-Tabelle in der Datenbank
public class WatchlistRepository {
    Dao<WatchlistMovieEntity, Long> watchlistDao; // DAO für die Watchlist-Tabelle

    // Konstruktor, der den DAO für die Watchlist initialisiert
    public WatchlistRepository() {
        this.watchlistDao = DatabaseManager.getDatabaseManager().getWatchlistDao();
    }

    // Methode zum Entfernen eines Films aus der Watchlist anhand seiner ID
    public int removeFromWatchlist(String id) throws SQLException {
        return watchlistDao.delete(getMovieById(id));
    }

    // Methode zum Abrufen aller Filme aus der Watchlist
    public List<WatchlistMovieEntity> getWatchlist() throws SQLException {
        return watchlistDao.queryForAll();
    }

    // Methode zum Abrufen eines Films aus der Watchlist anhand seiner ID
    public WatchlistMovieEntity getMovieById(String id) throws SQLException {
        List<WatchlistMovieEntity> list = getWatchlist();
        for (WatchlistMovieEntity m : list) {
            if (m.getId().equals(id)) return m;
        }
        return null;
    }

    // Methode zum Hinzufügen eines Films zur Watchlist
    public int addToWatchlist(WatchlistMovieEntity movie) throws SQLException {
        // Überprüfen, ob der Film bereits in der Watchlist vorhanden ist
        if (this.isInWatchlist(movie.getId())) return 0; // Wenn ja, gibt 0 zurück
        else {
            return watchlistDao.create(movie); // Ansonsten wird der Film zur Watchlist hinzugefügt
        }
    }

    // Methode zur Überprüfung, ob ein Film bereits in der Watchlist vorhanden ist
    public boolean isInWatchlist(String id) throws SQLException {
        for (WatchlistMovieEntity m : getWatchlist()) {
            if (m.getId().equals(id)) return true;
        }
        return false;
    }
}
