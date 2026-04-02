
package Ustore.api.dto;

import java.io.Serializable;

public class LessonDtos{
    public record LessonCreateRequest(String title, int orderIndex, Long courseId) implements Serializable{}
    public record LessonResponse(Long id, String title, int orderIndex, Long courseId) implements Serializable{}
    public record LessonUpdateRequest(String title, int orderIndex) implements Serializable{}
}
