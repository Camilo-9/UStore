
package edu.unimagdalena.uStore.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uStore.api.dto.CourseDtos.CourseCreateRequest;
import uStore.api.dto.CourseDtos.CourseResponse;
import uStore.api.dto.CourseDtos.CourseUpdateRequest;

public interface CourseService{
    CourseResponse create(CourseCreateRequest req);
    CourseResponse get(Long id);
    CourseResponse getByTitle(String title);
    Page<CourseResponse> list(Pageable pageable);
    CourseResponse update(Long id, CourseUpdateRequest req);

    void delete(Long id);
}
