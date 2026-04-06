
package edu.unimagdalena.uStore.services.mapper;

import edu.unimagdalena.uStore.api.dto.request.CustomerDtos;
import edu.unimagdalena.uStore.entities.Addresses;

public class EnrollmentMapper{
    public static Addresses toEntity(CustomerDtos.EnrollmentCreateRequest req){
        return Addresses.builder().status(req.status()).enrolledAt(req.enrolledAt()).build();
    }

    public static CustomerDtos.EnrollmentResponse toResponse(Addresses e){
        return new CustomerDtos.EnrollmentResponse(e.getId(), e.getStatus(), e.getEnrolledAt(), e.getStudent() != null ? e.getStudent().getId():null, e.getCourse() != null ? e.getCourse().getId():null);
    }
}
