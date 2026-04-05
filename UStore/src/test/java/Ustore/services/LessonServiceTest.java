
package uStore.services;

import uStore.api.dto.LessonDtos.*;
import uStore.entities.Customer;
import uStore.entities.Lesson;
import uStore.repositories.CourseRepository;
import uStore.repositories.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest{
    @Mock
    LessonRepository lessonRepository;

    @Mock
    CourseRepository courseRepository;

    @InjectMocks
    LessonServiceImpl lessonService;

    private Customer course;
    private Lesson lesson;

    @BeforeEach
    void setUp(){
        course = new Customer(1L, "Web Programming", "Ongoing", true, Instant.now(),
                            Instant.now(), null, null, null, null);
        lesson = new Lesson(1L, "Introduction", 1, course);
    }

    // ── Create ────────────────────────────────────────────────────────────────
    @Test
    void givenValidRequest_whenCreate_thenReturnLessonResponse(){
        LessonCreateRequest req = new LessonCreateRequest("Introduction", 1, 1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.save(any())).thenReturn(lesson);

        LessonResponse response = lessonService.create(req);

        assertThat(response.title()).isEqualTo("Introduction");
        assertThat(response.orderIndex()).isEqualTo(1);
        assertThat(response.courseId()).isEqualTo(1L);
        verify(lessonRepository, times(1)).save(any());
    }

    @Test
    void givenInvalidCourseId_whenCreate_thenThrowException(){
        LessonCreateRequest req = new LessonCreateRequest("Introduction", 1, 99L);

        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.create(req)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    // ── Read ──────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenGet_thenReturnLessonResponse(){
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        LessonResponse response = lessonService.get(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Introduction");
    }

    @Test
    void givenNonExistentId_whenGet_thenThrowException(){
        when(lessonRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.get(99L)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void givenCourseId_whenListByCourse_thenReturnLessonsOrdered(){
        Lesson lesson2 = new Lesson(2L, "Variables", 2, course);
        when(lessonRepository.findByCourse_IdOrderByOrderIndexAsc(1L)).thenReturn(List.of(lesson, lesson2));

        List<LessonResponse> result = lessonService.listByCourse(1L);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).orderIndex()).isEqualTo(1);
        assertThat(result.get(1).orderIndex()).isEqualTo(2);
    }

    // ── Update ────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenUpdate_thenReturnUpdatedLessonResponse(){
        LessonUpdateRequest req = new LessonUpdateRequest("Updated Introduction", 2);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(any())).thenReturn(lesson);

        LessonResponse response = lessonService.update(1L, req);

        assertThat(response).isNotNull();
        verify(lessonRepository, times(1)).save(any());
    }

    @Test
    void givenNonExistentId_whenUpdate_thenThrowException(){
        LessonUpdateRequest req = new LessonUpdateRequest("Updated Introduction", 2);

        when(lessonRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.update(99L, req)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    // ── Delete ────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenDelete_thenRepositoryDeleteIsCalled(){
        doNothing().when(lessonRepository).deleteById(1L);
        lessonService.delete(1L);
        verify(lessonRepository, times(1)).deleteById(1L);
    }
}
