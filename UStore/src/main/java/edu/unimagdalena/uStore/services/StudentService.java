
package edu.unimagdalena.uStore.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uStore.api.dto.StudentDtos.StudentCreateRequest;
import uStore.api.dto.StudentDtos.StudentResponse;
import uStore.api.dto.StudentDtos.StudentUpdateRequest;

public interface StudentService{
    StudentResponse create(StudentCreateRequest req);
    StudentResponse get(Long id);
    StudentResponse getByEmail(String email);
    Page<StudentResponse> list(Pageable pageable);
    StudentResponse update(Long id, StudentUpdateRequest req);

    void delete(Long id);
}
