
package Ustore.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "instructor_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstructorProfile{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String bio;

    @OneToOne(optional = false)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;
}
