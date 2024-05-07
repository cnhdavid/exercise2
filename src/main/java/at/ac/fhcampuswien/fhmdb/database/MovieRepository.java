package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.List;

public class MovieRepository {
    private Dao<MovieEntity, Long> movieDao;

    public MovieRepository() {
        this.movieDao = DatabaseManager.getDatabaseManager().getMovieDao();
    }
    public int addAllMovies(List<Movie> movies) throws SQLException {
        return movieDao.create(MovieEntity.fromMovies(movies));
    }
    public int removeAll() throws SQLException {
        return movieDao.delete(movieDao.queryForAll());
    }

    public List<MovieEntity> getAllMovies() throws SQLException, DatabaseException {
        List<MovieEntity> movieEntities = movieDao.queryForAll();
        if (movieEntities.isEmpty()) throw new DatabaseException(new RuntimeException());
        return movieDao.queryForAll();
    }

    public MovieEntity getMovie(long dbId) throws SQLException {
        return movieDao.queryForId(dbId);
    }
}

