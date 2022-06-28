package de.htw_berlin.imi.db.services;

import de.htw_berlin.imi.db.entities.Studierende;
import de.htw_berlin.imi.db.web.StudierendeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StudierendeEntityServiceTest {

    @Autowired
    StudierendeEntityService studierendeEntityService;

    @Test
    void findAll() {
        final List<Studierende> all = studierendeEntityService.findAll();
        assertThat(all).isNotEmpty();
        assertThat(all)
                .extracting(Studierende::getId)
                .isNotEmpty();
    }

    @Test
    void findById() {
        final Optional<Studierende> studierendeOptional = studierendeEntityService.findById(26830);
        assertThat(studierendeOptional).isPresent();
        assertThat(studierendeOptional.get().getName()).isEqualTo("Babbage");
    }

    @Test
    void cannotfindById() {
        final Optional<Studierende> studierendeOptional = studierendeEntityService.findById(0);
        assertThat(studierendeOptional).isNotPresent();
    }

    @Test
    void createNew() {
        final Studierende studierende = studierendeEntityService.create();
        assertThat(studierende).isNotNull();
        assertThat(studierende.getId()).isPositive();

        final Studierende studierende2 = studierendeEntityService.create();
        assertThat(studierende.getId()).isLessThan(studierende2.getId());
    }

    @Test
    void save() {
        final Studierende studierende = studierendeEntityService.create();
        final String studienbeginn = "WS 2020";
        studierende.setName("Potter");
        studierende.setVorname("Harry");
        studierende.setGeburtsdatum("24.03.1984");
        studierende.setGeburtsort("London");
        studierende.setAnzahl_semester(2);
        studierende.setStudienbeginn(studienbeginn);

        studierendeEntityService.save(studierende);

        final Optional<Studierende> studierendeOptional = studierendeEntityService.findById(studierende.getId());
        assertThat(studierendeOptional).isPresent();
        assertThat(studierendeOptional.get().getName()).isEqualTo("Potter");
        assertThat(studierendeOptional.get().getVorname()).isEqualTo("Harry");
        assertThat(studierendeOptional.get().getGeburtsdatum()).isEqualTo("24.03.1984");
        assertThat(studierendeOptional.get().getGeburtsort()).isEqualTo("London");
        //assertThat(studierendeOptional.get().getAnzahl_semester().isEqualTo(2));
        //assertThat(studierendeOptional.get().getStudienbeginn().isEqualTo("WS 2020"));
    }

    @Test
    void createFrom() {
        final StudierendeDto studierende = new StudierendeDto();
        studierende.setName("Potter");
        studierende.setVorname("Harry");
        studierende.setGeburtsdatum("24.03.1984");
        studierende.setGeburtsort("London");
        studierende.setAnzahl_semester(2);
        studierende.setStudienbeginn("WS 2020");

        final Studierende from = studierendeEntityService.createFrom(studierende);

        final Optional<Studierende> studierendeOptional = studierendeEntityService.findById(from.getId());
        assertThat(studierendeOptional).isPresent();
        assertThat(studierendeOptional.get().getName()).isEqualTo("Potter");
        assertThat(studierendeOptional.get().getVorname()).isEqualTo("Harry");
        assertThat(studierendeOptional.get().getGeburtsdatum()).isEqualTo("24.03.1984");
        assertThat(studierendeOptional.get().getGeburtsort()).isEqualTo("London");
        //assertThat(studierendeOptional.get().getAnzahl_semester().isEqualTo(2));
        //assertThat(studierendeOptional.get().getStudienbeginn().isEqualTo("SS 2020"));

    }

}