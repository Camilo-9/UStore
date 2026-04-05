
package edu.unimagdalena.uStore.api.dto;

import java.io.Serializable;
import java.time.Instant;

public class StudentDtos{
    public record StudentCreateRequest(String email, String fullName, Instant createdAt, Instant updatedAt) implements Serializable{}
    public record StudentResponse(Long id, String email, String fullName, Instant createdAt, Instant updatedAt) implements Serializable{}
    public record StudentUpdateRequest(String fullName, Instant updatedAt) implements Serializable{}
}
