
package Ustore.repositories;

import Ustore.entities.Assessment;
import Ustore.entities.Course;
import Ustore.entities.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

class AssessmentRepositoryTest extends AbstractRepositoryIT{
    @Autowired
    AssessmentRepository assessmentRepository;

    @Autowired
    TestEntityManager em;

    private Assessment assessment;

    @BeforeEach
    void setUp(){
        //Creamos Student y Course mínimos para satisfacer las relaciones
        Student student = em.persist(Student.builder()
                .email("test@test.com")
                .fullName("Camilo Alvarez")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());
        Course course = em.persist(Course.builder()
                .title("Web Programming")
                .status("Ongoing")
                .active(true)
                .build());
        em.flush();

        assessment = Assessment.builder()
                .type("Quiz")
                .score(85)
                .takenAt(Instant.now())
                .student(student)
                .course(course)
                .build();
    }

    //CREATE
    @Test
    void givenAssessment_whenSave_thenIdIsGenerated(){
        Assessment saved = assessmentRepository.save(assessment);

        assertThat(saved.getId()).isPositive();
        assertThat(saved.getType()).isEqualTo("Quiz");
        assertThat(saved.getScore()).isEqualTo(85);
    }

    //READ
    @Test
    void givenSavedAssessment_whenFindById_thenReturnAssessment(){
        Assessment saved = assessmentRepository.save(assessment);

        Optional<Assessment> found = assessmentRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getType()).isEqualTo("Quiz");
    }

    @Test
    void givenNonExistentId_whenFindById_thenReturnEmpty(){
        Optional<Assessment> found = assessmentRepository.findById(999L);

        assertThat(found).isEmpty();
    }

    @Test
    void givenAssessmentWithType_whenFindByType_thenReturnAssessment(){
        assessmentRepository.save(assessment);

        Optional<Assessment> found = assessmentRepository.findByType("Quiz");

        assertThat(found).isPresent();
        assertThat(found.get().getType()).isEqualTo("Quiz");
    }

    @Test
    void givenNonExistentType_whenFindByType_thenReturnEmpty(){
        assessmentRepository.save(assessment);

        Optional<Assessment> found = assessmentRepository.findByType("Project");

        assertThat(found).isEmpty();
    }

    @Test
    void givenMultipleAssessments_whenFindByIdIn_thenReturnOnlyRequested(){
        Assessment a1 = assessmentRepository.save(assessment);
        Assessment a2 = assessmentRepository.save(
                        Assessment.builder()
                        .type("Presentation")
                        .score(70)
                        .takenAt(Instant.now())
                        .student(assessment.getStudent())
                        .course(assessment.getCourse())
                        .build()
        );
        assessmentRepository.save(
                        Assessment.builder()
                        .type("Homework")
                        .score(60)
                        .takenAt(Instant.now())
                        .student(assessment.getStudent())
                        .course(assessment.getCourse())
                        .build()
        );

        List<Assessment> found = assessmentRepository.findByIdIn(List.of(a1.getId(), a2.getId()));

        assertThat(found).hasSize(2);
        assertThat(found).extracting(Assessment::getType).containsExactlyInAnyOrder("Quiz", "Presentation");
    }

    //UPDATE
    @Test
    void givenSavedAssessment_whenUpdate_thenChangesArePersisted(){
        Assessment saved = assessmentRepository.save(assessment);

        saved.setScore(95);
        saved.setType("Exam");
        assessmentRepository.save(saved);

        Assessment updated = assessmentRepository.findById(saved.getId()).get();
        assertThat(updated.getScore()).isEqualTo(95);
        assertThat(updated.getType()).isEqualTo("Exam");
    }

    //DELETE
    @Test
    void givenSavedAssessment_whenDelete_thenCannotBeFound(){
        Assessment saved = assessmentRepository.save(assessment);

        long id = saved.getId();

        assessmentRepository.deleteById(id);

        assertThat(assessmentRepository.findById(id)).isEmpty();
    }
}
