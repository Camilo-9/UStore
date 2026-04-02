
package Ustore.services.mapper;

import Ustore.api.dto.CourseDtos;
import Ustore.entities.Course;

public class CourseMapper{
    public static Course toEntity(CourseDtos.CourseCreateRequest req){
        return Course.builder().title(req.title()).status(req.status()).active(req.active()).createdAt(req.createdAt()).updatedAt(req.updatedAt()).build();
    }

    public static CourseDtos.CourseResponse toResponse(Course c){
        return new CourseDtos.CourseResponse(c.getId(), c.getTitle(), c.getStatus(), c.isActive(), c.getCreatedAt(), c.getUpdatedAt(), c.getInstructor() != null ? c.getInstructor().getId():null);
    }
}
