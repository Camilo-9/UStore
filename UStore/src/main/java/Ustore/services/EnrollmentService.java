
package Ustore.services;

import Ustore.api.dto.EnrollmentDtos.*;
import java.util.List;

public interface EnrollmentService{
    EnrollmentResponse create(EnrollmentCreateRequest req);
    EnrollmentResponse get(Long id);
    List<EnrollmentResponse> listByStudent(Long studentId);
    List<EnrollmentResponse> listByCourse(Long courseId);
    EnrollmentResponse update(Long id, EnrollmentUpdateRequest req);

    void delete(Long id);
}
