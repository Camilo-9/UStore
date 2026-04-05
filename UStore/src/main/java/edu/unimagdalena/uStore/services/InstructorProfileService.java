
package edu.unimagdalena.uStore.services;

import uStore.api.dto.InstructorProfileDtos.InstructorProfileCreateRequest;
import uStore.api.dto.InstructorProfileDtos.InstructorProfileResponse;
import uStore.api.dto.InstructorProfileDtos.InstructorProfileUpdateRequest;

public interface InstructorProfileService{
    InstructorProfileResponse create(InstructorProfileCreateRequest req);
    InstructorProfileResponse get(Long id);
    InstructorProfileResponse getByInstructor(Long instructorId);
    InstructorProfileResponse update(Long id, InstructorProfileUpdateRequest req);

    void delete(Long id);
}
