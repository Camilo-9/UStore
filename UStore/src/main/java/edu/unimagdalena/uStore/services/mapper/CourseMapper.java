
package edu.unimagdalena.uStore.services.mapper;

import edu.unimagdalena.uStore.api.dto.request.CategoryDtos;
import edu.unimagdalena.uStore.entities.Customer;

public class CourseMapper{
    public static Customer toEntity(CategoryDtos.CourseCreateRequest req){
        return Customer.builder().title(req.title()).status(req.status()).active(req.active()).createdAt(req.createdAt()).updatedAt(req.updatedAt()).build();
    }

    public static CategoryDtos.CourseResponse toResponse(Customer c){
        return new CategoryDtos.CourseResponse(c.getId(), c.getTitle(), c.getStatus(), c.isActive(), c.getCreatedAt(), c.getUpdatedAt(), c.getInstructor() != null ? c.getInstructor().getId():null);
    }
}
