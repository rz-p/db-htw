package de.htw_berlin.imi.db.services;

import de.htw_berlin.imi.db.entities.Studierende;
import de.htw_berlin.imi.db.web.StudierendeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implements the DAO (data access object) pattern for BueroRaum.
 * <p>
 * Classes annotated with @Service can be injected using @Autowired
 * in other Spring components.
 * <p>
 * Classes annotated with @Slf4j have access to loggers.
 */
@Service
@Slf4j
public class StudierendeEntityService extends AbstractEntityService<Studierende> {

    private static final String FIND_ALL_QUERY = """
                SELECT
                   id
                   ,name
                   ,vorname
                   ,geburtsdatum
                   ,geburtsort
                   ,anzahl_semester
                   ,studienbeginn
                FROM uni.v_Studierende
            """;

    private static final String INSERT_BASE_QUERY = """
            INSERT INTO uni.Studierende (id, name, vorname, geburtsdatum, geburtsort, anzahl_semester, studienbeginn)
                VALUES (?, ?, ?, ?, ?, ?, ?);
            """;


    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + " WHERE ID = ";

    @Override
    public List<Studierende> findAll() {
        final List<Studierende> result = new ArrayList<>();
        try {
            final ResultSet resultSet = query(FIND_ALL_QUERY);
            while (resultSet.next()) {
                result.add(getStudierende(resultSet));
            }
        } catch (final Exception e) {
            log.error("Problem finding studierenden {}", e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<Studierende> findById(final long id) {
        try {
            final ResultSet resultSet = query(FIND_BY_ID_QUERY + id);
            if (resultSet.next()) {
                return Optional.of(getStudierende(resultSet));
            }
        } catch (final Exception e) {
            log.error("Problem finding studierende by id {}", e.getMessage());
        }
        return Optional.empty();
    }

    private Studierende getStudierende(final ResultSet resultSet) throws SQLException {
        final long id = resultSet.getInt("id");
        final Studierende entity = new Studierende(id);
        entity.setName(resultSet.getString("name"));
        entity.setVorname(resultSet.getString("vorname"));
        entity.setGeburtsdatum(resultSet.getString("geburtsdatum"));
        entity.setGeburtsort(resultSet.getString("geburtsort"));
        entity.setAnzahl_semester(resultSet.getInt("anzahl_semester"));
        entity.setStudienbeginn(resultSet.getString("studienbeginn"));
        return entity;
    }

    @Override
    public Studierende create() {
        return new Studierende(idGenerator.generate());
    }

    @Override
    public void save(final Studierende e) {
        log.debug("insert: {}", INSERT_BASE_QUERY);
        try {
            final Connection connection = getConnection();
            connection.setAutoCommit(false);
            try (final PreparedStatement basePreparedStatement = getPreparedStatement(connection, INSERT_BASE_QUERY)) {

                createBaseClassPart(e, basePreparedStatement);
                connection.commit();
            } catch (final SQLException ex) {
                log.error("Error creating studierende, aborting {}", ex.getMessage());
                connection.rollback();
                throw new RuntimeException(ex);
            }
        } catch (final SQLException ex) {
            log.error("Could not get connection.");
            throw new RuntimeException(ex);
        }
    }

    private void createBaseClassPart(final Studierende e, final PreparedStatement basePreparedStatement) throws SQLException {
        // TODO set parameters
        basePreparedStatement.setLong(1, e.getId());
        basePreparedStatement.setString(2, e.getName());
        basePreparedStatement.setString(3, e.getVorname());
        basePreparedStatement.setString(4, e.getGeburtsdatum());
        basePreparedStatement.setString(5, e.getGeburtsort());
        basePreparedStatement.setInt(6, e.getAnzahl_semester());
        basePreparedStatement.setString(7, e.getStudienbeginn());

        final int update = basePreparedStatement.executeUpdate();
        if (update != 1) {
            throw new SQLException("Could not create (room) part");
        }
    }

    public Studierende createFrom(final StudierendeDto template) {
        final Studierende studierende = create();
        studierende.setName(template.getName());
        studierende.setVorname(template.getVorname());
        studierende.setGeburtsdatum(template.getGeburtsdatum());
        // TODO initialize missing fields
        studierende.setGeburtsort(template.getGeburtsort());
        studierende.setAnzahl_semester(template.getAnzahl_semester());
        studierende.setStudienbeginn(template.getStudienbeginn());
        save(studierende);
        return studierende;
    }
}
//