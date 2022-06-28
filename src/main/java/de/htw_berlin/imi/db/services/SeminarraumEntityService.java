package de.htw_berlin.imi.db.services;

import de.htw_berlin.imi.db.entities.Seminarraum;
import de.htw_berlin.imi.db.web.SeminarraeumeDto;
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
public class SeminarraumEntityService extends AbstractEntityService<Seminarraum> {

    private static final String FIND_ALL_QUERY = """
                SELECT
                   id
                   ,name
                   ,raumnummer
                   ,flaeche
                   ,hoehe
                   ,kapazitaet
                   ,stockwerk_id
                FROM uni.v_Seminarraeume
            """;

    private static final String INSERT_BASE_QUERY = """
            INSERT INTO uni.Raeume (id, name, raumnummer, flaeche, raumhoehe)
                VALUES (?, ?, ?, ?, ?);
            """;

    private static final String INSERT_WORK_ROOM = """
            INSERT INTO uni.Arbeitsraeume (id, kapazitaet)
                VALUES (?, ?);
            """;

    private static final String INSERT_SEMINAR_ROOM = """
            INSERT INTO uni.Seminarraeume (id)
                VALUES (?);
            """;

    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + " WHERE ID = ";

    //TODO: Add Query for delete
    private static final String DELETE_BASE_QUERY = """
            DELETE FROM uni.Raeume WHERE id = ?;
            """;

    private static final String DELETE_WORK_QUERY = """
            DELETE FROM uni.Arbeitsraeume WHERE id = ?;
            """;

    private static final String DELETE_SEMINAR_QUERY = """
            DELETE FROM uni.Seminarraeume WHERE id = ?;
            """;


    //TODO: Add query for update

    private static final String UPDATE_BASE_QUERY = """
            UPDATE uni.Raeume
            SET name = ?, raumnummer = ?, flaeche = ?, raumhoehe = ?
            WHERE id = ?;
            """;

    @Override
    public List<Seminarraum> findAll() {
        final List<Seminarraum> result = new ArrayList<>();
        try {
            final ResultSet resultSet = query(FIND_ALL_QUERY, false);
            while (resultSet.next()) {
                result.add(getSeminarRaum(resultSet));
            }
        } catch (final Exception e) {
            log.error("Problem finding seminarraeume {}", e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<Seminarraum> findById(final long id) {
        try {
            final ResultSet resultSet = query(FIND_BY_ID_QUERY + id, false);
            if (resultSet.next()) {
                return Optional.of(getSeminarRaum(resultSet));
            }
        } catch (final Exception e) {
            log.error("Problem finding seminarraum by id {}", e.getMessage());
        }
        return Optional.empty();
    }

    private Seminarraum getSeminarRaum(final ResultSet resultSet) throws SQLException {
        final long id = resultSet.getInt("id");
        final Seminarraum entity = new Seminarraum(id);
        entity.setName(resultSet.getString("name"));
        entity.setFlaeche(resultSet.getDouble("flaeche"));
        entity.setHoehe(resultSet.getDouble("hoehe"));
        entity.setKapazitaet(resultSet.getInt("kapazitaet"));
        entity.setRaumnummer(resultSet.getString("raumnummer"));
        return entity;
    }

    @Override
    public Seminarraum create() {
        return new Seminarraum(idGenerator.generate());
    }

    @Override
    public void save(final Seminarraum e) {
        log.debug("insert: {}", INSERT_BASE_QUERY);
        try {
            final Connection connection = getConnection(false);
            connection.setAutoCommit(false);
            try (final PreparedStatement basePreparedStatement = getPreparedStatement(connection, INSERT_BASE_QUERY);
                 final PreparedStatement workPreparedStatement = getPreparedStatement(connection, INSERT_WORK_ROOM);
                 final PreparedStatement seminarPreparedStatement = getPreparedStatement(connection, INSERT_SEMINAR_ROOM)) {

                createBaseClassPart(e, basePreparedStatement);
                createWorkRoomPart(e, workPreparedStatement);
                createOfficePart(e, seminarPreparedStatement);
                connection.commit();
            } catch (final SQLException ex) {
                log.error("Error creating seminarraum, aborting {}", ex.getMessage());
                connection.rollback();
                throw new RuntimeException(ex);
            }
        } catch (final SQLException ex) {
            log.error("Could not get connection.");
            throw new RuntimeException(ex);
        }
    }

    private void createOfficePart(final Seminarraum e, final PreparedStatement officePreparedStatement) throws SQLException {
        officePreparedStatement.setLong(1, e.getId());
        final int update = officePreparedStatement.executeUpdate();

        if (update != 1) {
            throw new SQLException("Could not create (office) part");
        }
    }

    private void createWorkRoomPart(final Seminarraum e, final PreparedStatement workPreparedStatement) throws SQLException {

        workPreparedStatement.setLong(1, e.getId());
        workPreparedStatement.setInt(2, e.getKapazitaet());
        final int update = workPreparedStatement.executeUpdate();

        if (update != 1) {
            throw new SQLException("Could not create (work room) part");
        }
    }

    private void createBaseClassPart(final Seminarraum e, final PreparedStatement basePreparedStatement) throws SQLException {

        basePreparedStatement.setLong(1, e.getId());
        basePreparedStatement.setString(2, e.getName());
        basePreparedStatement.setString(3, e.getRaumnummer());
        basePreparedStatement.setDouble(4, e.getFlaeche());
        basePreparedStatement.setDouble(5, e.getHoehe());

        final int update = basePreparedStatement.executeUpdate();
        if (update != 1) {
            throw new SQLException("Could not create (room) part");
        }
    }

    public Seminarraum createFrom(final SeminarraeumeDto template) {
        final Seminarraum seminarRaum = create();
        seminarRaum.setName(template.getName());
        seminarRaum.setRaumnummer(template.getRaumnummer());
        seminarRaum.setKapazitaet(template.getKapazitaet());
        seminarRaum.setFlaeche(template.getFlaeche());
        seminarRaum.setHoehe(template.getHoehe());
        save(seminarRaum);
        return seminarRaum;
    }

    @Override
    public void update(Seminarraum e) {
        log.debug("update: {}", e);
        try {
            final Connection connection = getConnection(false);
            connection.setAutoCommit(false);
            try (final PreparedStatement updateSeminarPreparedStatement = getPreparedStatement(connection, UPDATE_BASE_QUERY);
            ) {

                updateSeminarPreparedStatement(e, updateSeminarPreparedStatement);
                connection.commit();
            } catch (final SQLException ex) {
                log.error("Error creating office, aborting {}", ex.getMessage());
                connection.rollback();
                throw new RuntimeException(ex);
            }
        } catch (final SQLException ex) {
            log.error("Could not get connection.");
            throw new RuntimeException(ex);
        }
    }

    private void updateSeminarPreparedStatement(Seminarraum e, PreparedStatement updateSeminarPreparedStatement) throws SQLException {

        updateSeminarPreparedStatement.setString(1, e.getName());
        updateSeminarPreparedStatement.setString(2, e.getRaumnummer());
        updateSeminarPreparedStatement.setDouble(3, e.getFlaeche());
        updateSeminarPreparedStatement.setDouble(4, e.getHoehe());
        updateSeminarPreparedStatement.setLong(5, e.getId());

        final int update = updateSeminarPreparedStatement.executeUpdate();
        if (update != 1) {
            throw new SQLException("Could not update (room) part");
        }

    }

    @Override
    public void delete(Seminarraum e) {
        log.debug("delete: {}", DELETE_BASE_QUERY);
        try {
            final Connection connection = getConnection(false);
            connection.setAutoCommit(false);
            try (final PreparedStatement baseDelPreparedStatement = getPreparedStatement(connection, DELETE_BASE_QUERY);
                 final PreparedStatement workDelPreparedStatement = getPreparedStatement(connection, DELETE_WORK_QUERY);
                 final PreparedStatement seminarDelPreparedStatement = getPreparedStatement(connection, DELETE_SEMINAR_QUERY)) {

                baseDelPreparedStatement(e, baseDelPreparedStatement);
                workDelPreparedStatement(e, workDelPreparedStatement);
                seminarDelPreparedStatement(e, seminarDelPreparedStatement);
                connection.commit();
            } catch (final SQLException ex) {
                log.error("Error creating seminar roo, aborting {}", ex.getMessage());
                connection.rollback();
                throw new RuntimeException(ex);
            }

        } catch (final SQLException ex) {
            log.error("Could not get connection.");
            throw new RuntimeException(ex);
        }
    }

    private void workDelPreparedStatement(final Seminarraum e, PreparedStatement workDelPreparedStatement) throws SQLException {
        workDelPreparedStatement.setLong(1, e.getId());
        final int update = workDelPreparedStatement.executeUpdate();

        if (update != 1) {
            throw new SQLException("Could not delete (work room) part");
        }
    }

    private void baseDelPreparedStatement(final Seminarraum e, PreparedStatement baseDelPreparedStatement) throws SQLException {
        baseDelPreparedStatement.setLong(1, e.getId());
        final int update = baseDelPreparedStatement.executeUpdate();

        if (update != 1) {
            throw new SQLException("Could not delete (room) part");
        }
    }

    private void seminarDelPreparedStatement(final Seminarraum e, PreparedStatement seminarDelPreparedStatement) throws SQLException {
        seminarDelPreparedStatement.setLong(1, e.getId());
        final int update = seminarDelPreparedStatement.executeUpdate();

        if (update != 1) {
            throw new SQLException("Could not delete (office) part");
        }
    }


}
//