package at.ac.fhcampuswien.fhmdb.interfaces;

import java.sql.SQLException;

public interface ClickEventHandler<T> {
    void onClick(T item) throws SQLException;
}
