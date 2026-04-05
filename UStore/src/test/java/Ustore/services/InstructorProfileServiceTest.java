
package uStore.services;

import uStore.api.dto.InstructorProfileDtos.*;
import uStore.entities.Instructor;
import uStore.entities.InstructorProfile;
import uStore.repositories.InstructorProfileRepository;
import uStore.repositories.InstructorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorProfileServiceTest{
    @Mock
    InstructorProfileRepository instructorProfileRepository;

    @Mock
    InstructorRepository instructorRepository;

    @InjectMocks
    InstructorProfileServiceImpl instructorProfileService;

    private Instructor instructor;
    private InstructorProfile profile;

    @BeforeEach
    void setUp(){
        instructor = new Instructor(1L, "instructor@test.com", "John Doe", Instant.now(),
                Instant.now(), null, null);
        profile = new InstructorProfile(1L, "3001234567", "Experienced developer", instructor);
    }

    // ── Create ────────────────────────────────────────────────────────────────
    @Test
    void givenValidRequest_whenCreate_thenReturnInstructorProfileResponse(){
        InstructorProfileCreateRequest req = new InstructorProfileCreateRequest("3001234567", "Experienced developer", 1L);

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(instructorProfileRepository.save(any())).thenReturn(profile);

        InstructorProfileResponse response = instructorProfileService.create(req);

        assertThat(response.phone()).isEqualTo("3001234567");
        assertThat(response.bio()).isEqualTo("Experienced developer");
        assertThat(response.instructorId()).isEqualTo(1L);
        verify(instructorProfileRepository, times(1)).save(any());
    }

    @Test
    void givenInvalidInstructorId_whenCreate_thenThrowException(){
        InstructorProfileCreateRequest req = new InstructorProfileCreateRequest("3001234567", "Experienced developer", 99L);

        when(instructorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> instructorProfileService.create(req)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    // ── Read ──────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenGet_thenReturnInstructorProfileResponse(){
        when(instructorProfileRepository.findById(1L)).thenReturn(Optional.of(profile));

        InstructorProfileResponse response = instructorProfileService.get(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.phone()).isEqualTo("3001234567");
    }

    @Test
    void givenNonExistentId_whenGet_thenThrowException(){
        when(instructorProfileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> instructorProfileService.get(99L)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void givenInstructorId_whenGetByInstructor_thenReturnInstructorProfileResponse(){
        when(instructorProfileRepository.findByInstructor_Id(1L)).thenReturn(Optional.of(profile));

        InstructorProfileResponse response = instructorProfileService.getByInstructor(1L);

        assertThat(response.instructorId()).isEqualTo(1L);
        assertThat(response.phone()).isEqualTo("3001234567");
    }

    @Test
    void givenNonExistentInstructorId_whenGetByInstructor_thenThrowException(){
        when(instructorProfileRepository.findByInstructor_Id(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> instructorProfileService.getByInstructor(99L))
                .isInstanceOf(RuntimeException.class).hasMessageContaining("99");
    }

    // ── Update ────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenUpdate_thenReturnUpdatedInstructorProfileResponse(){
        InstructorProfileUpdateRequest req = new InstructorProfileUpdateRequest("3009876543",
                                                                                "Senior developer");

        when(instructorProfileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(instructorProfileRepository.save(any())).thenReturn(profile);

        InstructorProfileResponse response = instructorProfileService.update(1L, req);

        assertThat(response).isNotNull();
        verify(instructorProfileRepository, times(1)).save(any());
    }

    @Test
    void givenNonExistentId_whenUpdate_thenThrowException(){
        InstructorProfileUpdateRequest req = new InstructorProfileUpdateRequest("3009876543",
                                                                                "Senior developer");

        when(instructorProfileRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> instructorProfileService.update(99L, req))
                .isInstanceOf(RuntimeException.class).hasMessageContaining("99");
    }

    // ── Delete ────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenDelete_thenRepositoryDeleteIsCalled(){
        doNothing().when(instructorProfileRepository).deleteById(1L);
        instructorProfileService.delete(1L);
        verify(instructorProfileRepository, times(1)).deleteById(1L);
    }
}
