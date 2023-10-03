package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Data
public class Event extends Entity {
    private final LocalDateTime dateTime;
    private final User user;
    private final String eventType;
    private final String operation;
    private final Integer entityId;

    @Builder
    public Event(Integer id, LocalDateTime dateTime, User user, String eventType, String operation, Integer entityId) {
        super(id);
        this.dateTime = dateTime;
        this.user = user;
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("timestamp", dateTime.toInstant(ZoneOffset.UTC));
        values.put("user_id", user.getId());
        values.put("event_type", eventType);
        values.put("operation", operation);
        values.put("entity_id", entityId);
        return values;
    }

}
