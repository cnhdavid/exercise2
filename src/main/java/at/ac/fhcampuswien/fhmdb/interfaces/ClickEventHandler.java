package at.ac.fhcampuswien.fhmdb.interfaces;

import java.sql.SQLException;
@FunctionalInterface
public interface ClickEventHandler<T> {
    void onClick(T item) throws SQLException;
}
