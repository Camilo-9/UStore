
package Ustore.services;

import Ustore.api.dto.CourseDtos.*;
import Ustore.entities.Course;
import Ustore.entities.Instructor;
import Ustore.repositories.CourseRepository;
import Ustore.repositories.InstructorRepository;
import Ustore.services.mapper.CourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService{
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    @Override
    public CourseResponse create(CourseCreateRequest req) {
        Instructor instructor = instructorRepository.findById(req.instructorId())
                .orElseThrow(() -> new RuntimeException("Instructor %d not found".formatted(req.instructorId())));
        Course course = CourseMapper.toEntity(req);
        course.setInstructor(instructor);

        return CourseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse get(Long id) {
        return courseRepository.findById(id)
                .map(CourseMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Course %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getByTitle(String title) {
        return courseRepository.findByTitle(title)
                .map(CourseMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Course with title '%s' not found".formatted(title)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponse> list(Pageable pageable) {
        return courseRepository.findAll(pageable)
                .map(CourseMapper::toResponse);
    }

    @Override
    public CourseResponse update(Long id, CourseUpdateRequest req) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course %d not found".formatted(id)));
        course.setTitle(req.title());
        course.setStatus(req.status());
        course.setActive(req.active());
        course.setUpdatedAt(req.updatedAt());

        return CourseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }
}
