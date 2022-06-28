package de.htw_berlin.imi.db.services;

import de.htw_berlin.imi.db.entities.Seminarraum;
import de.htw_berlin.imi.db.web.SeminarraeumeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SeminarraumEntityServiceTest {

    @Autowired
    SeminarraumEntityService seminarRaumEntityService;

    @Test
    void findAll() {
        final List<Seminarraum> all = seminarRaumEntityService.findAll();
        assertThat(all).isNotEmpty();
        assertThat(all)
                .extracting(Seminarraum::getId)
                .isNotEmpty();
    }

    @Test
    void findById() {
        final Optional<Seminarraum> seminarRaumOptional = seminarRaumEntityService.findById(12);
        assertThat(seminarRaumOptional).isPresent();
        assertThat(seminarRaumOptional.get().getName()).isEqualTo("Lecture Hall#1");
    }

    @Test
    void cannotfindById() {
        final Optional<Seminarraum> seminarRaumOptional = seminarRaumEntityService.findById(0);
        assertThat(seminarRaumOptional).isNotPresent();
    }

    @Test
    void createNew() {
        final Seminarraum seminarRaum = seminarRaumEntityService.create();
        assertThat(seminarRaum).isNotNull();
        assertThat(seminarRaum.getId()).isPositive();

        final Seminarraum seminarRaum2 = seminarRaumEntityService.create();
        assertThat(seminarRaum.getId()).isLessThan(seminarRaum2.getId());
    }

    @Test
    void save() {
        final Seminarraum seminarRaum = seminarRaumEntityService.create();
        final String roomNumber = "#" + seminarRaum.getId();
        final String name = "Test Seminar " + roomNumber;
        seminarRaum.setRaumnummer(roomNumber);
        seminarRaum.setName(name);
        seminarRaum.setHoehe(3.0);
        seminarRaum.setFlaeche(110.5);
        seminarRaum.setKapazitaet(80);

        seminarRaumEntityService.save(seminarRaum);

        final Optional<Seminarraum> seminarRaumOptional = seminarRaumEntityService.findById(seminarRaum.getId());
        assertThat(seminarRaumOptional).isPresent();
        assertThat(seminarRaumOptional.get().getName()).isEqualTo(name);
        assertThat(seminarRaumOptional.get().getRaumnummer()).isEqualTo(roomNumber);
        assertThat(seminarRaumOptional.get().getHoehe()).isEqualTo(3.0);
        assertThat(seminarRaumOptional.get().getFlaeche()).isEqualTo(110.5);
        assertThat(seminarRaumOptional.get().getKapazitaet()).isEqualTo(80);
    }

    @Test
    void createFrom() {
        final SeminarraeumeDto seminarRaum = new SeminarraeumeDto();
        final String roomNumber = "#250";
        final String name = "Test Seminar " + roomNumber;
        seminarRaum.setRaumnummer(roomNumber);
        seminarRaum.setName(name);
        seminarRaum.setKapazitaet(80);
        seminarRaum.setHoehe(3.0);
        seminarRaum.setFlaeche(110.5);

        final Seminarraum from = seminarRaumEntityService.createFrom(seminarRaum);

        final Optional<Seminarraum> seminarRaumOptional = seminarRaumEntityService.findById(from.getId());
        assertThat(seminarRaumOptional).isPresent();
        assertThat(seminarRaumOptional.get().getName()).isEqualTo(name);
        assertThat(seminarRaumOptional.get().getRaumnummer()).isEqualTo(roomNumber);
        assertThat(seminarRaumOptional.get().getKapazitaet()).isEqualTo(80);
        assertThat(seminarRaumOptional.get().getFlaeche()).isEqualTo(110.5);
        assertThat(seminarRaumOptional.get().getHoehe()).isEqualTo(3.0);

    }

    @Test
    void update() {
        final Optional<Seminarraum> seminarraumOptional = seminarRaumEntityService.findById(12);
        final String name = "" + System.currentTimeMillis();
        seminarraumOptional.ifPresent(s -> {
            s.setName(name);
            seminarRaumEntityService.update(s);
        });

        final Optional<Seminarraum> reloadedOffice = seminarRaumEntityService.findById(12);
        assertThat(reloadedOffice)
                .isPresent();
        assertThat(seminarraumOptional.get().getName()).isEqualTo(name);
    }

    @Test
    void delete() {
        final Seminarraum seminarRaum = seminarRaumEntityService.create();
        final String roomNumber = "#" + seminarRaum.getId();
        final String name = "Seminar Room " + roomNumber;
        seminarRaum.setRaumnummer(roomNumber);
        seminarRaum.setName(name);
        seminarRaum.setHoehe(2.8);
        seminarRaum.setFlaeche(25.4);
        seminarRaum.setKapazitaet(2);

        seminarRaumEntityService.save(seminarRaum);
        seminarRaumEntityService.delete(seminarRaum);

        final Optional<Seminarraum> seminarraumOptional = seminarRaumEntityService.findById(seminarRaum.getId());
        assertThat(seminarraumOptional).isNotPresent();
    }

}