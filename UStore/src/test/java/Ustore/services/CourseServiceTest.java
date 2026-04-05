
package uStore.services;

import uStore.api.dto.CourseDtos.*;
import uStore.entities.Customer;
import uStore.entities.Instructor;
import uStore.repositories.CourseRepository;
import uStore.repositories.InstructorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest{
    @Mock
    CourseRepository courseRepository;

    @Mock
    InstructorRepository instructorRepository;

    @InjectMocks
    CourseServiceImpl courseService;

    private Instructor instructor;
    private Customer course;

    @BeforeEach
    void setUp(){
        instructor = new Instructor(1L, "instructor@test.com", "John Doe", Instant.now(),
                                    Instant.now(), null, null);
        course = new Customer(1L, "Web Programming", "Ongoing", true, Instant.now(),
                            Instant.now(), null, null, null, instructor);
    }

    // ── Create ────────────────────────────────────────────────────────────────
    @Test
    void givenValidRequest_whenCreate_thenReturnCourseResponse(){
        CourseCreateRequest req = new CourseCreateRequest("Web Programming", "Ongoing", true,
                                                          Instant.now(), Instant.now(), 1L);

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any())).thenReturn(course);

        CourseResponse response = courseService.create(req);

        assertThat(response.title()).isEqualTo("Web Programming");
        assertThat(response.status()).isEqualTo("Ongoing");
        verify(courseRepository, times(1)).save(any());
    }

    @Test
    void givenInvalidInstructorId_whenCreate_thenThrowException(){
        CourseCreateRequest req = new CourseCreateRequest("Web Programming", "Ongoing", true, Instant.now(), Instant.now(), 99L);

        when(instructorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.create(req)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    // ── Read ──────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenGet_thenReturnCourseResponse(){
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        CourseResponse response = courseService.get(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Web Programming");
    }

    @Test
    void givenNonExistentId_whenGet_thenThrowException(){
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.get(99L)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void givenCourses_whenList_thenReturnPage(){
        Page<Customer> page = new PageImpl<>(List.of(course));

        when(courseRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<CourseResponse> result = courseService.list(PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).title()).isEqualTo("Web Programming");
    }

    // ── Update ────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenUpdate_thenReturnUpdatedCourseResponse(){
        CourseUpdateRequest req = new CourseUpdateRequest("Updated Title", "Finished", false,
                                                          Instant.now());

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any())).thenReturn(course);

        CourseResponse response = courseService.update(1L, req);

        assertThat(response).isNotNull();
        verify(courseRepository, times(1)).save(any());
    }

    // ── Delete ────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenDelete_thenRepositoryDeleteIsCalled(){
        doNothing().when(courseRepository).deleteById(1L);
        courseService.delete(1L);
        verify(courseRepository, times(1)).deleteById(1L);
    }
}
