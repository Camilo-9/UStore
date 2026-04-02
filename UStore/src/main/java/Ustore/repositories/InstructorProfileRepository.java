
package Ustore.repositories;

import Ustore.entities.InstructorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InstructorProfileRepository extends JpaRepository<InstructorProfile, Long>{
    Optional<InstructorProfile> findByInstructor_Id(Long instructorId);
}
