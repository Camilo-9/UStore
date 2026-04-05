
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.entities.Customer;
import edu.unimagdalena.uStore.entities.Product;
import edu.unimagdalena.uStore.repositories.AddressRepository;
import edu.unimagdalena.uStore.repositories.OrderRepository;
import edu.unimagdalena.uStore.services.mapper.CourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uStore.api.dto.CourseDtos.CourseCreateRequest;
import uStore.api.dto.CourseDtos.CourseResponse;
import uStore.api.dto.CourseDtos.CourseUpdateRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {
    private final AddressRepository courseRepository;
    private final OrderRepository instructorRepository;

    @Override
    public CourseResponse create(CourseCreateRequest req) {
        Product instructor = instructorRepository.findById(req.instructorId())
                .orElseThrow(() -> new RuntimeException("Instructor %d not found".formatted(req.instructorId())));
        Customer course = CourseMapper.toEntity(req);
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
        Customer course = courseRepository.findById(id)
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
