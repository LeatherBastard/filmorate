package ru.yandex.practicum.filmorate.dao.event;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Repository
public class EventDaoImpl implements EventDao, EventStorage {
    private static final String EVENT_ID_NOT_FOUND_MESSAGE = "Event with id %d was not found!";
    private final JdbcTemplate jdbcTemplate;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public EventDaoImpl(JdbcTemplate jdbcTemplate, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    @Override
    public List<Event> getAll() {
        return jdbcTemplate.query(GET_ALL_EVENTS_QUERY, this::mapRowToEvent);
    }

    @Override
    public Event getById(Integer id) {
        if (!validateEntityId(id)) {
            throw new EntityNotFoundException(EVENT_ID_NOT_FOUND_MESSAGE, id);
        }
        Event event = jdbcTemplate.queryForObject(GET_BY_ID_EVENT_QUERY, this::mapRowToEvent, id);
        if (event == null) {
            throw new EntityNotFoundException(EVENT_ID_NOT_FOUND_MESSAGE, id);
        }
        return event;
    }

    @Override
    public Event add(Event event) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(EVENTS_TABLE_NAME)
                .usingGeneratedKeyColumns(ID_EVENTS_COLUMN);
        Integer eventId = simpleJdbcInsert.executeAndReturnKey(event.toMap()).intValue();
        event = new Event(eventId, event.getDateTime(), event.getUser(), event.getEventType(), event.getOperation(), event.getEntityId());
        return event;
    }

    @Override
    public Event update(Event entity) {
        return null;
    }

    @Override
    public void removeAll() {
        jdbcTemplate.update(REMOVE_ALL_EVENTS_QUERY);
        jdbcTemplate.execute(RESTART_EVENT_ID_AFTER_REMOVAL_QUERY);
    }

    private Event mapRowToEvent(ResultSet rs, int rowNum) throws SQLException {
        return new Event(rs.getInt(ID_EVENTS_COLUMN),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(rs.getInt(TIMESTAMP_EVENTS_COLUMN)), ZoneOffset.UTC),
                userStorage.getById(rs.getInt(USER_ID_EVENTS_COLUMN)),
                rs.getString(EVENT_TYPE_EVENTS_COLUMN),
                rs.getString(OPERATION_EVENTS_COLUMN),
                rs.getInt(ENTITY_ID_EVENTS_COLUMN));
    }
}
