package at.ac.fhcampuswien.fhmdb.exceptions;  // Das Paket, in dem sich die Klasse befindet

// Definiert eine benutzerdefinierte Ausnahme, die von Exception erbt
public class DatabaseException extends Exception {

    // Standardkonstruktor ohne Parameter
    public DatabaseException() {
        super();  // Ruft den Standardkonstruktor der Basisklasse Exception auf
    }

    // Konstruktor, der eine Fehlermeldung als Parameter annimmt
    public DatabaseException(String message) {
        super(message);  // Ruft den Konstruktor der Basisklasse mit der Fehlermeldung auf
    }

    // Konstruktor, der eine Ursache (eine andere Ausnahme) als Parameter annimmt
    public DatabaseException(Throwable cause) {
        super(cause);  // Ruft den Konstruktor der Basisklasse mit der Ursache auf
    }

    // Konstruktor, der sowohl eine Fehlermeldung als auch eine Ursache annimmt
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);  // Ruft den Konstruktor der Basisklasse mit der Fehlermeldung und der Ursache auf
    }
}
