
package edu.unimagdalena.uStore.api.dto;

import java.io.Serializable;
import java.time.Instant;

public class CourseDtos{
    public record CourseCreateRequest(String title, String status, boolean active, Instant createdAt, Instant updatedAt, Long instructorId) implements Serializable{}
    public record CourseUpdateRequest(String title, String status, Boolean active, Instant updatedAt) implements Serializable{}
    public record CourseResponse(Long id, String title, String status, boolean active, Instant createdAt, Instant updatedAt, Long instructorId) implements Serializable{}
}
