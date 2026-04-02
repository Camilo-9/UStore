
package Ustore.services;

import Ustore.api.dto.InstructorDtos.*;
import Ustore.entities.Instructor;
import Ustore.repositories.InstructorRepository;
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
class InstructorServiceTest{
    @Mock
    InstructorRepository instructorRepository;

    @InjectMocks
    InstructorServiceImpl instructorService;

    private Instructor instructor;

    @BeforeEach
    void setUp(){
        instructor = new Instructor(1L, "instructor@test.com", "John Doe", Instant.now(),
                                    Instant.now(), null, null);
    }

    // ── Create ────────────────────────────────────────────────────────────────
    @Test
    void givenValidRequest_whenCreate_thenReturnInstructorResponse(){
        InstructorCreateRequest req = new InstructorCreateRequest("instructor@test.com", "John Doe",
                                                                  Instant.now(), Instant.now());

        when(instructorRepository.existsByEmail(req.email())).thenReturn(false);
        when(instructorRepository.save(any())).thenReturn(instructor);

        InstructorResponse response = instructorService.create(req);

        assertThat(response.email()).isEqualTo("instructor@test.com");
        assertThat(response.fullName()).isEqualTo("John Doe");
        verify(instructorRepository, times(1)).save(any());
    }

    @Test
    void givenDuplicateEmail_whenCreate_thenThrowException(){
        InstructorCreateRequest req = new InstructorCreateRequest("instructor@test.com", "John Doe",
                                                                   Instant.now(), Instant.now());

        when(instructorRepository.existsByEmail(req.email())).thenReturn(true);
        assertThatThrownBy(() -> instructorService.create(req)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("already exists");
    }

    // ── Read ──────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenGet_thenReturnInstructorResponse(){
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        InstructorResponse response = instructorService.get(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("instructor@test.com");
    }

    @Test
    void givenNonExistentId_whenGet_thenThrowException(){
        when(instructorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> instructorService.get(99L)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void givenExistingEmail_whenGetByEmail_thenReturnInstructorResponse(){
        when(instructorRepository.findByEmail("instructor@test.com")).thenReturn(Optional.of(instructor));

        InstructorResponse response = instructorService.getByEmail("instructor@test.com");

        assertThat(response.email()).isEqualTo("instructor@test.com");
    }

    @Test
    void givenNonExistentEmail_whenGetByEmail_thenThrowException(){
        when(instructorRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> instructorService.getByEmail("unknown@test.com"))
                .isInstanceOf(RuntimeException.class).hasMessageContaining("unknown@test.com");
    }

    @Test
    void givenInstructors_whenList_thenReturnPage(){
        Page<Instructor> page = new PageImpl<>(List.of(instructor));
        when(instructorRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<InstructorResponse> result = instructorService.list(PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).email()).isEqualTo("instructor@test.com");
    }

    // ── Update ────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenUpdate_thenReturnUpdatedInstructorResponse(){
        InstructorUpdateRequest req = new InstructorUpdateRequest("Jane Doe", Instant.now());

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(instructorRepository.save(any())).thenReturn(instructor);

        InstructorResponse response = instructorService.update(1L, req);

        assertThat(response).isNotNull();
        verify(instructorRepository, times(1)).save(any());
    }

    @Test
    void givenNonExistentId_whenUpdate_thenThrowException(){
        InstructorUpdateRequest req = new InstructorUpdateRequest("Jane Doe", Instant.now());

        when(instructorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> instructorService.update(99L, req)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    // ── Delete ────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenDelete_thenRepositoryDeleteIsCalled(){
        doNothing().when(instructorRepository).deleteById(1L);
        instructorService.delete(1L);
        verify(instructorRepository, times(1)).deleteById(1L);
    }
}
