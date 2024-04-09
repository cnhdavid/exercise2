package at.ac.fhcampuswien.fhmdb.models;

public enum Genre {
    ACTION,
    ADVENTURE,
    ANIMATION,
    BIOGRAPHY,
    COMEDY,
    CRIME,
    DRAMA,
    DOCUMENTARY,
    FAMILY,
    FANTASY,
    HISTORY,
    HORROR,
    MUSICAL,
    MYSTERY,
    ROMANCE,
    SCIENCE_FICTION,
    SPORT,
    THRILLER,
    WAR,
    WESTERN;

    public String getName() {
        // Return the name of the enum constant
        return this.name();
    }
}
