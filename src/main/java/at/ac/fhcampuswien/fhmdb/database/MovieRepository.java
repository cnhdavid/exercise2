package at.ac.fhcampuswien.fhmdb.database;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;

import java.sql.SQLException;
import java.util.List;

public class MovieRepository {
    private Dao<MovieEntity, Integer> movieDao;

    public MovieRepository() throws SQLException {
        movieDao = DaoManager.createDao(DatabaseManager.getConnectionSource(), MovieEntity.class);
    }

    public List<MovieEntity> getAllMovies() throws SQLException {
        return movieDao.queryForAll();
    }

    public void deleteAllMovies() throws SQLException {
        movieDao.deleteBuilder().delete();
    }

    public void deleteMovie(MovieEntity movie) throws SQLException {
        movieDao.delete(movie);
    }

    public void addMovie(MovieEntity movie) throws SQLException {
        movieDao.createOrUpdate(movie);
    }
}
