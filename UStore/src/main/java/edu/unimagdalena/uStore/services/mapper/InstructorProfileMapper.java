
package edu.unimagdalena.uStore.services.mapper;

import edu.unimagdalena.uStore.api.dto.request.CreateOrderRequest;
import edu.unimagdalena.uStore.entities.Inventory;

public class InstructorProfileMapper{
    public static Inventory toEntity(CreateOrderRequest.InstructorProfileCreateRequest req){
        return Inventory.builder().phone(req.phone()).bio(req.bio()).build();
    }

    public static CreateOrderRequest.InstructorProfileResponse toResponse(Inventory p){
        return new CreateOrderRequest.InstructorProfileResponse(p.getId(), p.getPhone(), p.getBio(), p.getInstructor() != null ? p.getInstructor().getId():null);
    }
}
