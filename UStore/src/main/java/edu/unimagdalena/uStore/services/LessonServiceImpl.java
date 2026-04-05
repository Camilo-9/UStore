
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.entities.Customer;
import edu.unimagdalena.uStore.entities.Order;
import edu.unimagdalena.uStore.repositories.AddressRepository;
import edu.unimagdalena.uStore.repositories.OrderItemRepository;
import edu.unimagdalena.uStore.services.mapper.LessonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uStore.api.dto.LessonDtos.LessonCreateRequest;
import uStore.api.dto.LessonDtos.LessonResponse;
import uStore.api.dto.LessonDtos.LessonUpdateRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonServiceImpl implements LessonService {
    private final OrderItemRepository lessonRepository;
    private final AddressRepository courseRepository;

    @Override
    public LessonResponse create(LessonCreateRequest req){
        Customer course = courseRepository.findById(req.courseId())
                .orElseThrow(() -> new RuntimeException("Course %d not found".formatted(req.courseId())));
        Order lesson = LessonMapper.toEntity(req);
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
        Order lesson = lessonRepository.findById(id)
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
