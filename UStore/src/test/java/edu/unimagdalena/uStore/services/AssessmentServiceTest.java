
package edu.unimagdalena.uStore.services;

import uStore.api.dto.AssessmentDtos.*;
import uStore.entities.Category;
import uStore.exceptions.NotFoundException;
import uStore.repositories.AssessmentRepository;
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
class AssessmentServiceTest{
    @Mock
    AssessmentRepository repo;

    @InjectMocks
    AssessmentServiceImpl assessmentService;

    private Category assessment;

    @BeforeEach
    void setUp(){
        assessment = new Category(1L, "Quiz", 85, Instant.now(), null, null);
    }

    // ── Create ────────────────────────────────────────────────────────────────
    @Test
    void givenValidRequest_whenCreate_thenReturnAssessmentResponse() {
        AssessmentCreateRequest req = new AssessmentCreateRequest("Quiz", 85, Instant.now(), null,
                null);

        when(repo.save(any())).thenReturn(assessment);

        AssessmentResponse response = assessmentService.create(req);

        assertThat(response.type()).isEqualTo("Quiz");
        assertThat(response.score()).isEqualTo(85);
        verify(repo, times(1)).save(any());
    }

    // ── Read ──────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenGet_thenReturnAssessmentResponse(){
        when(repo.findById(1L)).thenReturn(Optional.of(assessment));

        AssessmentResponse response = assessmentService.get(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.type()).isEqualTo("Quiz");
    }

    @Test
    void givenNonExistentId_whenGet_thenThrowNotFoundException(){
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assessmentService.get(99L)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void givenAssessments_whenList_thenReturnAllAssessments(){
        when(repo.findAll()).thenReturn(List.of(assessment));

        List<AssessmentResponse> result = assessmentService.list();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).type()).isEqualTo("Quiz");
    }

    // ── Delete ────────────────────────────────────────────────────────────────
    @Test
    void givenExistingId_whenDelete_thenRepositoryDeleteIsCalled(){
        doNothing().when(repo).deleteById(1L);
        assessmentService.delete(1L);
        verify(repo, times(1)).deleteById(1L);
    }
}
