
package edu.unimagdalena.uStore.services.mapper;

import edu.unimagdalena.uStore.api.dto.LessonDtos;
import edu.unimagdalena.uStore.entities.Order;

public class LessonMapper{
    public static Order toEntity(LessonDtos.LessonCreateRequest req){
        return Order.builder().title(req.title()).orderIndex(req.orderIndex()).build();
    }

    public static LessonDtos.LessonResponse toResponse(Order l){
        return new LessonDtos.LessonResponse(l.getId(), l.getTitle(), l.getOrderIndex(), l.getCourse() != null ? l.getCourse().getId():null);
    }
}
