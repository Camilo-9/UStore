
package edu.unimagdalena.uStore.api.dto;

import java.io.Serializable;
import java.time.Instant;

public class AssessmentDtos{
    public record AssessmentCreateRequest(String type, int score, Instant takenAt, Long studentId, Long courseId) implements Serializable{}
    public record AssessmentResponse(Long id, String type, int score, Instant takenAt, Long studentId, Long courseId) implements Serializable{}
}
