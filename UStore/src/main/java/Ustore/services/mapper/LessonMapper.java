
package Ustore.services.mapper;

import Ustore.api.dto.LessonDtos;
import Ustore.entities.Lesson;

public class LessonMapper{
    public static Lesson toEntity(LessonDtos.LessonCreateRequest req){
        return Lesson.builder().title(req.title()).orderIndex(req.orderIndex()).build();
    }

    public static LessonDtos.LessonResponse toResponse(Lesson l){
        return new LessonDtos.LessonResponse(l.getId(), l.getTitle(), l.getOrderIndex(), l.getCourse() != null ? l.getCourse().getId():null);
    }
}
