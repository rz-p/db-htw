package de.htw_berlin.imi.db.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MatrikelnummerGeneratorTest {

    @Autowired
    MatrikelnummerGenerator matrikelnummerGenerator;

    @Test
    void getNewId() {
        final long newId = matrikelnummerGenerator.generate();
        assertThat(newId).isGreaterThan(50000);
    }
}