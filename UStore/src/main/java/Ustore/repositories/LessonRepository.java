
package Ustore.repositories;

import Ustore.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long>{
    List<Lesson> findByCourse_Id(Long courseId);
    List<Lesson> findByCourse_IdOrderByOrderIndexAsc(Long courseId);
}
