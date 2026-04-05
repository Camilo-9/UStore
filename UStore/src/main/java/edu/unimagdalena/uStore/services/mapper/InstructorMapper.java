
package edu.unimagdalena.uStore.services.mapper;

import edu.unimagdalena.uStore.api.dto.InstructorDtos;
import edu.unimagdalena.uStore.entities.Product;

public class InstructorMapper{
    public static Product toEntity(InstructorDtos.InstructorCreateRequest req){
        return Product.builder().email(req.email()).fullName(req.fullName()).createdAt(req.createdAt()).updatedAt(req.updatedAt()).build();
    }

    public static InstructorDtos.InstructorResponse toResponse(Product i){
        return new InstructorDtos.InstructorResponse(i.getId(), i.getEmail(), i.getFullName(), i.getCreatedAt(), i.getUpdatedAt());
    }
}
