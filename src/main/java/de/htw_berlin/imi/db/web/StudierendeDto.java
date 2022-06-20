package de.htw_berlin.imi.db.web;

/**
 * Data transfer object (DTO) class:
 * decouples representation from the entity class.
 * <p>
 * NB: we cannot create Studierende objects without an id.
 * Objects of this class simply hold field values
 */
public class StudierendeDto {


    private String name;

    private String vorname;

    private String geburtsdatum;

    private String geburtsort;

    private int anzahl_semester;

    private String studienbeginn;


    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(String geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public String getGeburtsort() {
        return geburtsort;
    }

    public void setGeburtsort(String geburtsort) {
        this.geburtsort = geburtsort;
    }

    public int getAnzahl_semester() {
        return anzahl_semester;
    }

    public void setAnzahl_semester(int anzahl_semester) {
        this.anzahl_semester = anzahl_semester;
    }

    public String getStudienbeginn() {
        return studienbeginn;
    }

    public void setStudienbeginn(String studienbeginn) {
        this.studienbeginn = studienbeginn;
    }
}
