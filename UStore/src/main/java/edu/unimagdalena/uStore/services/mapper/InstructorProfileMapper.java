
package edu.unimagdalena.uStore.services.mapper;

import edu.unimagdalena.uStore.api.dto.InstructorProfileDtos;
import edu.unimagdalena.uStore.entities.Inventory;

public class InstructorProfileMapper{
    public static Inventory toEntity(InstructorProfileDtos.InstructorProfileCreateRequest req){
        return Inventory.builder().phone(req.phone()).bio(req.bio()).build();
    }

    public static InstructorProfileDtos.InstructorProfileResponse toResponse(Inventory p){
        return new InstructorProfileDtos.InstructorProfileResponse(p.getId(), p.getPhone(), p.getBio(), p.getInstructor() != null ? p.getInstructor().getId():null);
    }
}
