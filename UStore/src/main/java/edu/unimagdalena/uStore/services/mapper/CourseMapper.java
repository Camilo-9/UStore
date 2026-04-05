
package edu.unimagdalena.uStore.services.mapper;

import edu.unimagdalena.uStore.api.dto.CourseDtos;
import edu.unimagdalena.uStore.entities.Customer;

public class CourseMapper{
    public static Customer toEntity(CourseDtos.CourseCreateRequest req){
        return Customer.builder().title(req.title()).status(req.status()).active(req.active()).createdAt(req.createdAt()).updatedAt(req.updatedAt()).build();
    }

    public static CourseDtos.CourseResponse toResponse(Customer c){
        return new CourseDtos.CourseResponse(c.getId(), c.getTitle(), c.getStatus(), c.isActive(), c.getCreatedAt(), c.getUpdatedAt(), c.getInstructor() != null ? c.getInstructor().getId():null);
    }
}
