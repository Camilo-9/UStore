
package Ustore.services;

import Ustore.api.dto.InstructorProfileDtos.*;

public interface InstructorProfileService{
    InstructorProfileResponse create(InstructorProfileCreateRequest req);
    InstructorProfileResponse get(Long id);
    InstructorProfileResponse getByInstructor(Long instructorId);
    InstructorProfileResponse update(Long id, InstructorProfileUpdateRequest req);

    void delete(Long id);
}
