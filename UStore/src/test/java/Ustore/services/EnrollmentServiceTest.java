
package Ustore.services;

import Ustore.api.dto.EnrollmentDtos.*;
import Ustore.entities.Course;
import Ustore.entities.Enrollment;
import Ustore.entities.Student;
import Ustore.repositories.CourseRepository;
import Ustore.repositories.EnrollmentRepository;
import Ustore.repositories.StudentRepository;
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
class EnrollmentServiceTest{
    @Mock
    EnrollmentRepository enrollmentRepository;

    @Mock
    StudentRepository studentRepository;

    @Mock
    CourseRepository courseRepository;

    @InjectMocks
    EnrollmentServiceImpl enrollmentService;

    private Student student;
    private Course course;
    private Enrollment enrollment;

    @BeforeEach
    void setUp(){
        student = new Student(1L, "test@test.com", "Camilo Alvarez", Instant.now(),
                               Instant.now(), null, null);
        course = new Course(1L, "Web Programming", "Ongoing", true, Instant.now(),
                             Instant.now(), null, null, null, null);
        enrollment = new Enrollment(1L, "Active", Instant.now(), student, course);
    }

    // ── Create ────────────────────────────────────────────────────────────────
    @Test
    void givenValidRequest_whenCreate_thenReturnEnrollmentResponse(){
        EnrollmentCreateRequest req = new EnrollmentCreateRequest("Active", Instant.now(), 1L,1L);

        when(enrollmentRepository.existsByStudent_IdAndCourse_Id(1L, 1L)).thenReturn(false);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any())).thenReturn(enrollment);

        EnrollmentResponse response = enrollmentService.create(req);

        assertThat(response.status()).isEqualTo("Active");
        assertThat(response.studentId()).isEqualTo(1L);
        assertThat(response.courseId()).isEqualTo(1L);
        verify(enrollmentRepository, times(1)).save(any());
    }

    @Test
    void givenDuplicateEnrollment_whenCreate_thenThrowException(){
        EnrollmentCreateRequest req = new EnrollmentCreateRequest("Active", Instant.now(), 1L, 1L);

        when(enrollmentRepository.existsByStudent_IdAndCourse_Id(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> enrollmentService.create(req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("already enrolled");
    }

    @Test
    void givenInvalidStudentId_whenCreate_thenThrowException(){
        EnrollmentCreateRequest req = new EnrollmentCreateRequest("Active", Instant.now(), 99L, 1L);

        when(enrollmentRepository.existsByStudent_IdAndCourse_Id(99L, 1L)).thenReturn(false);
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.create(req)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void givenInvalidCourseId_whenCreate_thenThrowException(){
        EnrollmentCreateRequest req = new EnrollmentCreateRequest("Active", Instant.now(), 1L, 99L);

        when(enrollmentRepository.existsByStudent_IdAndCourse_Id(1L, 99L)).thenReturn(false);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.create(req)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    // ── Read ──────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenGet_thenReturnEnrollmentResponse(){
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));

        EnrollmentResponse response = enrollmentService.get(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.status()).isEqualTo("Active");
    }

    @Test
    void givenNonExistentId_whenGet_thenThrowException(){
        when(enrollmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.get(99L)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void givenStudentId_whenListByStudent_thenReturnEnrollments(){
        when(enrollmentRepository.findByStudent_Id(1L)).thenReturn(List.of(enrollment));

        List<EnrollmentResponse> result = enrollmentService.listByStudent(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).studentId()).isEqualTo(1L);
    }

    @Test
    void givenCourseId_whenListByCourse_thenReturnEnrollments(){
        when(enrollmentRepository.findByCourse_Id(1L)).thenReturn(List.of(enrollment));

        List<EnrollmentResponse> result = enrollmentService.listByCourse(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).courseId()).isEqualTo(1L);
    }

    // ── Update ────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenUpdate_thenReturnUpdatedEnrollmentResponse(){
        EnrollmentUpdateRequest req = new EnrollmentUpdateRequest("Completed");

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(any())).thenReturn(enrollment);

        EnrollmentResponse response = enrollmentService.update(1L, req);

        assertThat(response).isNotNull();
        verify(enrollmentRepository, times(1)).save(any());
    }

    @Test
    void givenNonExistentId_whenUpdate_thenThrowException(){
        EnrollmentUpdateRequest req = new EnrollmentUpdateRequest("Completed");

        when(enrollmentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> enrollmentService.update(99L, req)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    // ── Delete ────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenDelete_thenRepositoryDeleteIsCalled(){
        doNothing().when(enrollmentRepository).deleteById(1L);
        enrollmentService.delete(1L);
        verify(enrollmentRepository, times(1)).deleteById(1L);
    }
}
