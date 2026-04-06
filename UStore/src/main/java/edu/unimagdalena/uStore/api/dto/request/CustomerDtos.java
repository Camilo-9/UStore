
package edu.unimagdalena.uStore.api.dto.request;

import java.io.Serializable;
import java.time.Instant;

public class CustomerDtos {
    public record EnrollmentCreateRequest(String status, Instant enrolledAt, Long studentId, Long courseId) implements Serializable{}
    public record EnrollmentResponse(Long id, String status, Instant enrolledAt, Long studentId, Long courseId) implements Serializable{}
    public record EnrollmentUpdateRequest(String status) implements Serializable{}
}
