
package edu.unimagdalena.uStore.services;

import uStore.api.dto.StudentDtos.*;
import uStore.entities.Student;
import uStore.repositories.StudentRepository;
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
class StudentServiceTest{
    @Mock
    StudentRepository studentRepository;

    @InjectMocks
    StudentServiceImpl studentService;

    private Student student;

    @BeforeEach
    void setUp(){
        student = new Student(1L, "student@test.com", "Camilo Alvarez", Instant.now(),
                              Instant.now(), null, null);
    }

    // ── Create ────────────────────────────────────────────────────────────────
    @Test
    void givenValidRequest_whenCreate_thenReturnStudentResponse(){
        StudentCreateRequest req = new StudentCreateRequest("student@test.com", "Camilo Alvarez",
                                                            Instant.now(), Instant.now());

        when(studentRepository.existsByEmail(req.email())).thenReturn(false);
        when(studentRepository.save(any())).thenReturn(student);

        StudentResponse response = studentService.create(req);

        assertThat(response.email()).isEqualTo("student@test.com");
        assertThat(response.fullName()).isEqualTo("Camilo Alvarez");
        verify(studentRepository, times(1)).save(any());
    }

    @Test
    void givenDuplicateEmail_whenCreate_thenThrowException(){
        StudentCreateRequest req = new StudentCreateRequest("student@test.com", "Camilo Alvarez",
                                                            Instant.now(), Instant.now());

        when(studentRepository.existsByEmail(req.email())).thenReturn(true);
        assertThatThrownBy(() -> studentService.create(req)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("already exists");
    }

    // ── Read ──────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenGet_thenReturnStudentResponse(){
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentResponse response = studentService.get(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("student@test.com");
    }

    @Test
    void givenNonExistentId_whenGet_thenThrowException(){
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.get(99L)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void givenExistingEmail_whenGetByEmail_thenReturnStudentResponse(){
        when(studentRepository.findByEmail("student@test.com")).thenReturn(Optional.of(student));

        StudentResponse response = studentService.getByEmail("student@test.com");

        assertThat(response.email()).isEqualTo("student@test.com");
    }

    @Test
    void givenNonExistentEmail_whenGetByEmail_thenThrowException(){
        when(studentRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> studentService.getByEmail("unknown@test.com"))
                .isInstanceOf(RuntimeException.class).hasMessageContaining("unknown@test.com");
    }

    @Test
    void givenStudents_whenList_thenReturnPage(){
        Page<Student> page = new PageImpl<>(List.of(student));

        when(studentRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<StudentResponse> result = studentService.list(PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).email()).isEqualTo("student@test.com");
    }

    // ── Update ────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenUpdate_thenReturnUpdatedStudentResponse(){
        StudentUpdateRequest req = new StudentUpdateRequest("Updated Name", Instant.now());

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any())).thenReturn(student);

        StudentResponse response = studentService.update(1L, req);

        assertThat(response).isNotNull();
        verify(studentRepository, times(1)).save(any());
    }

    @Test
    void givenNonExistentId_whenUpdate_thenThrowException(){
        StudentUpdateRequest req = new StudentUpdateRequest("Updated Name", Instant.now());

        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.update(99L, req)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    // ── Delete ────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenDelete_thenRepositoryDeleteIsCalled(){
        doNothing().when(studentRepository).deleteById(1L);
        studentService.delete(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }
}
