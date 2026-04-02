
package Ustore.services;

import Ustore.api.dto.LessonDtos.*;
import Ustore.entities.Course;
import Ustore.entities.Lesson;
import Ustore.repositories.CourseRepository;
import Ustore.repositories.LessonRepository;
import Ustore.services.mapper.LessonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonServiceImpl implements LessonService{
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Override
    public LessonResponse create(LessonCreateRequest req){
        Course course = courseRepository.findById(req.courseId())
                .orElseThrow(() -> new RuntimeException("Course %d not found".formatted(req.courseId())));
        Lesson lesson = LessonMapper.toEntity(req);
        lesson.setCourse(course);

        return LessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Override
    @Transactional(readOnly = true)
    public LessonResponse get(Long id){
        return lessonRepository.findById(id).map(LessonMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Lesson %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> listByCourse(Long courseId){
        return lessonRepository.findByCourse_IdOrderByOrderIndexAsc(courseId)
                .stream().map(LessonMapper::toResponse).toList();
    }

    @Override
    public LessonResponse update(Long id, LessonUpdateRequest req){
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson %d not found".formatted(id)));
        lesson.setTitle(req.title());
        lesson.setOrderIndex(req.orderIndex());

        return LessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Override
    public void delete(Long id){
        lessonRepository.deleteById(id);
    }
}
