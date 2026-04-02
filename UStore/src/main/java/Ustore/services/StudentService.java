
package Ustore.services;

import Ustore.api.dto.StudentDtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService{
    StudentResponse create(StudentCreateRequest req);
    StudentResponse get(Long id);
    StudentResponse getByEmail(String email);
    Page<StudentResponse> list(Pageable pageable);
    StudentResponse update(Long id, StudentUpdateRequest req);

    void delete(Long id);
}
