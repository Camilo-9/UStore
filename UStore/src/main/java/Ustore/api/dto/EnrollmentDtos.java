
package Ustore.api.dto;

import java.io.Serializable;
import java.time.Instant;

public class EnrollmentDtos{
    public record EnrollmentCreateRequest(String status, Instant enrolledAt, Long studentId, Long courseId) implements Serializable{}
    public record EnrollmentResponse(Long id, String status, Instant enrolledAt, Long studentId, Long courseId) implements Serializable{}
    public record EnrollmentUpdateRequest(String status) implements Serializable{}
}
