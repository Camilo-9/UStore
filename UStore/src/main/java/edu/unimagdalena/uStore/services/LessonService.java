
package edu.unimagdalena.uStore.services;

import uStore.api.dto.LessonDtos.LessonCreateRequest;
import uStore.api.dto.LessonDtos.LessonResponse;
import uStore.api.dto.LessonDtos.LessonUpdateRequest;

import java.util.List;

public interface LessonService{
    LessonResponse create(LessonCreateRequest req);
    LessonResponse get(Long id);
    List<LessonResponse> listByCourse(Long courseId);
    LessonResponse update(Long id, LessonUpdateRequest req);

    void delete(Long id);
}
