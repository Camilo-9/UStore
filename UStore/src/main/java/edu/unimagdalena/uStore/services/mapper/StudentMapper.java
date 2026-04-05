
package edu.unimagdalena.uStore.services.mapper;

import edu.unimagdalena.uStore.api.dto.StudentDtos;
import edu.unimagdalena.uStore.entities.OrderItem;

public class StudentMapper{
    public static OrderItem toEntity(StudentDtos.StudentCreateRequest req){
        return OrderItem.builder().email(req.email()).fullName(req.fullName()).createdAt(req.createdAt()).updatedAt(req.updatedAt()).build();
    }

    public static StudentDtos.StudentResponse toResponse(OrderItem s){
        return new StudentDtos.StudentResponse(s.getId(), s.getEmail(), s.getFullName(), s.getCreatedAt(), s.getUpdatedAt());
    }
}
