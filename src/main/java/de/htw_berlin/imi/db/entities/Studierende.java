package de.htw_berlin.imi.db.entities;

/**
 * Base class for all rooms.
 */
public class Studierende extends Entity {

    private long matr_nr;
    private String name;

    private String vorname;

    private String geburtsdatum;

    private String geburtsort;

    private int anzahl_semester;

    private String studienbeginn;

    public Studierende(final long id) {
        super(id);
    }

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

    public long getMatr_nr() {
        return matr_nr;
    }

    public void setMatr_nr(long matr_nr) {
        this.matr_nr = matr_nr;
    }
}
