
package Ustore.services.mapper;

import Ustore.api.dto.InstructorProfileDtos;
import Ustore.entities.InstructorProfile;

public class InstructorProfileMapper{
    public static InstructorProfile toEntity(InstructorProfileDtos.InstructorProfileCreateRequest req){
        return InstructorProfile.builder().phone(req.phone()).bio(req.bio()).build();
    }

    public static InstructorProfileDtos.InstructorProfileResponse toResponse(InstructorProfile p){
        return new InstructorProfileDtos.InstructorProfileResponse(p.getId(), p.getPhone(), p.getBio(), p.getInstructor() != null ? p.getInstructor().getId():null);
    }
}
