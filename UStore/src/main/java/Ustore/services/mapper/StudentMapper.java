
package Ustore.services.mapper;

import Ustore.api.dto.StudentDtos;
import Ustore.entities.Student;

public class StudentMapper{
    public static Student toEntity(StudentDtos.StudentCreateRequest req){
        return Student.builder().email(req.email()).fullName(req.fullName()).createdAt(req.createdAt()).updatedAt(req.updatedAt()).build();
    }

    public static StudentDtos.StudentResponse toResponse(Student s){
        return new StudentDtos.StudentResponse(s.getId(), s.getEmail(), s.getFullName(), s.getCreatedAt(), s.getUpdatedAt());
    }
}
