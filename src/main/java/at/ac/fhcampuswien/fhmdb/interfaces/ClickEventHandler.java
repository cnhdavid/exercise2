package at.ac.fhcampuswien.fhmdb.interfaces;

import java.sql.SQLException;

// Funktionale Schnittstelle für Event-Handler bei Klick-Ereignissen
@FunctionalInterface
public interface ClickEventHandler<T> {
    // Methode, die aufgerufen wird, wenn ein Element geklickt wird
    // Das Element, auf das geklickt wurde, wird als Parameter übergeben
    void onClick(T item) throws SQLException;
}
