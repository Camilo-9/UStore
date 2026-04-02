
package Ustore.services.mapper;

import Ustore.api.dto.EnrollmentDtos;
import Ustore.entities.Enrollment;

public class EnrollmentMapper{
    public static Enrollment toEntity(EnrollmentDtos.EnrollmentCreateRequest req){
        return Enrollment.builder().status(req.status()).enrolledAt(req.enrolledAt()).build();
    }

    public static EnrollmentDtos.EnrollmentResponse toResponse(Enrollment e){
        return new EnrollmentDtos.EnrollmentResponse(e.getId(), e.getStatus(), e.getEnrolledAt(), e.getStudent() != null ? e.getStudent().getId():null, e.getCourse() != null ? e.getCourse().getId():null);
    }
}
