package ru.yandex.practicum.filmorate.dao.event;

public interface EventDao {
    String GET_BY_ID_EVENT_QUERY = "SELECT * FROM events WHERE event_id = ?";
    String GET_ALL_EVENTS_QUERY = "SELECT * FROM events";
    String REMOVE_BY_ID_EVENT_QUERY = "DELETE FROM events WHERE event_id = ?";
    String REMOVE_ALL_EVENTS_QUERY = "DELETE FROM events";
    String RESTART_EVENT_ID_AFTER_REMOVAL_QUERY = "ALTER TABLE events ALTER COLUMN eventId RESTART WITH 1";
    String UPDATE_EVENT_QUERY = "UPDATE event SET timestamp = ?,user_id = ?,event_type = ?,operation = ?, entity_id = ? WHERE eventId = ?";
    String ID_EVENTS_COLUMN = "event_id";
    String TIMESTAMP_EVENTS_COLUMN = "timestamp";
    String USER_ID_EVENTS_COLUMN = "user_id";
    String EVENT_TYPE_EVENTS_COLUMN = "event_type";
    String OPERATION_EVENTS_COLUMN = "operation";
    String ENTITY_ID_EVENTS_COLUMN = "entity_id";
    String EVENTS_TABLE_NAME = "events";
}
