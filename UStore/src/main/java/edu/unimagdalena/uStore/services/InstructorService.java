
package edu.unimagdalena.uStore.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uStore.api.dto.InstructorDtos.InstructorCreateRequest;
import uStore.api.dto.InstructorDtos.InstructorResponse;
import uStore.api.dto.InstructorDtos.InstructorUpdateRequest;

public interface InstructorService{
    InstructorResponse create(InstructorCreateRequest req);
    InstructorResponse get(Long id);
    InstructorResponse getByEmail(String email);
    Page<InstructorResponse> list(Pageable pageable);
    InstructorResponse update(Long id, InstructorUpdateRequest req);

    void delete(Long id);
}
