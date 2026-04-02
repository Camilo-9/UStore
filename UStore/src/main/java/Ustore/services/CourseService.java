
package Ustore.services;

import Ustore.api.dto.CourseDtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService{
    CourseResponse create(CourseCreateRequest req);
    CourseResponse get(Long id);
    CourseResponse getByTitle(String title);
    Page<CourseResponse> list(Pageable pageable);
    CourseResponse update(Long id, CourseUpdateRequest req);

    void delete(Long id);
}
