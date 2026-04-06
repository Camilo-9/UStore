
package edu.unimagdalena.uStore.services.mapper;

import edu.unimagdalena.uStore.api.dto.request.CreateCustomerRequest;
import edu.unimagdalena.uStore.entities.Product;

public class InstructorMapper{
    public static Product toEntity(CreateCustomerRequest.InstructorCreateRequest req){
        return Product.builder().email(req.email()).fullName(req.fullName()).createdAt(req.createdAt()).updatedAt(req.updatedAt()).build();
    }

    public static CreateCustomerRequest.InstructorResponse toResponse(Product i){
        return new CreateCustomerRequest.InstructorResponse(i.getId(), i.getEmail(), i.getFullName(), i.getCreatedAt(), i.getUpdatedAt());
    }
}
