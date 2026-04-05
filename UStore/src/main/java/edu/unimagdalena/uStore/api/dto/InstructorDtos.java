
package edu.unimagdalena.uStore.api.dto;

import java.io.Serializable;
import java.time.Instant;

public class InstructorDtos{
    public record InstructorCreateRequest(String email, String fullName, Instant createdAt, Instant updatedAt) implements Serializable{}
    public record InstructorResponse(Long id, String email, String fullName, Instant createdAt, Instant updatedAt) implements Serializable{}
    public record InstructorUpdateRequest(String fullName, Instant updatedAt) implements Serializable{}
}
