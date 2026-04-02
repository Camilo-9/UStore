
package Ustore.services;

import Ustore.api.dto.LessonDtos.*;
import java.util.List;

public interface LessonService{
    LessonResponse create(LessonCreateRequest req);
    LessonResponse get(Long id);
    List<LessonResponse> listByCourse(Long courseId);
    LessonResponse update(Long id, LessonUpdateRequest req);

    void delete(Long id);
}
