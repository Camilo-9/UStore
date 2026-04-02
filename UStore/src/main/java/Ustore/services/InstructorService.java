
package Ustore.services;

import Ustore.api.dto.InstructorDtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InstructorService{
    InstructorResponse create(InstructorCreateRequest req);
    InstructorResponse get(Long id);
    InstructorResponse getByEmail(String email);
    Page<InstructorResponse> list(Pageable pageable);
    InstructorResponse update(Long id, InstructorUpdateRequest req);

    void delete(Long id);
}
