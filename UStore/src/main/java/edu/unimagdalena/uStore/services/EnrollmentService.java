
package edu.unimagdalena.uStore.services;

import uStore.api.dto.EnrollmentDtos.EnrollmentCreateRequest;
import uStore.api.dto.EnrollmentDtos.EnrollmentResponse;
import uStore.api.dto.EnrollmentDtos.EnrollmentUpdateRequest;

import java.util.List;

public interface EnrollmentService{
    EnrollmentResponse create(EnrollmentCreateRequest req);
    EnrollmentResponse get(Long id);
    List<EnrollmentResponse> listByStudent(Long studentId);
    List<EnrollmentResponse> listByCourse(Long courseId);
    EnrollmentResponse update(Long id, EnrollmentUpdateRequest req);

    void delete(Long id);
}
