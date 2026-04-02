
package Ustore.repositories;

import Ustore.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>{
    Optional<Course> findByTitle(String type);
    List<Course> findByStatus(String status);
    List<Course> findByActive(boolean active);
    List<Course> findByInstructor_Id(Long instructorId);
}
