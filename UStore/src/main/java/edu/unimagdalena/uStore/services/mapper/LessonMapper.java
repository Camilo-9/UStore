
package edu.unimagdalena.uStore.services.mapper;

import edu.unimagdalena.uStore.api.dto.request.CreateOrderItemRequest;
import edu.unimagdalena.uStore.entities.Order;

public class LessonMapper{
    public static Order toEntity(CreateOrderItemRequest.LessonCreateRequest req){
        return Order.builder().title(req.title()).orderIndex(req.orderIndex()).build();
    }

    public static CreateOrderItemRequest.LessonResponse toResponse(Order l){
        return new CreateOrderItemRequest.LessonResponse(l.getId(), l.getTitle(), l.getOrderIndex(), l.getCourse() != null ? l.getCourse().getId():null);
    }
}
