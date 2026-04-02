
package Ustore.services.mapper;

import Ustore.api.dto.InstructorDtos;
import Ustore.entities.Instructor;

public class InstructorMapper{
    public static Instructor toEntity(InstructorDtos.InstructorCreateRequest req){
        return Instructor.builder().email(req.email()).fullName(req.fullName()).createdAt(req.createdAt()).updatedAt(req.updatedAt()).build();
    }

    public static InstructorDtos.InstructorResponse toResponse(Instructor i){
        return new InstructorDtos.InstructorResponse(i.getId(), i.getEmail(), i.getFullName(), i.getCreatedAt(), i.getUpdatedAt());
    }
}
