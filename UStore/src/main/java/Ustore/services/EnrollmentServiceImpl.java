
package Ustore.services;

import Ustore.api.dto.EnrollmentDtos.*;
import Ustore.entities.Course;
import Ustore.entities.Enrollment;
import Ustore.entities.Student;
import Ustore.repositories.CourseRepository;
import Ustore.repositories.EnrollmentRepository;
import Ustore.repositories.StudentRepository;
import Ustore.services.mapper.EnrollmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService{
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    public EnrollmentResponse create(EnrollmentCreateRequest req){
        if(enrollmentRepository.existsByStudent_IdAndCourse_Id(req.studentId(), req.courseId())){
            throw new RuntimeException("Student %d is already enrolled in course %d"
                    .formatted(req.studentId(), req.courseId()));}
        Student student = studentRepository.findById(req.studentId())
                .orElseThrow(() -> new RuntimeException("Student %d not found".formatted(req.studentId())));
        Course course = courseRepository.findById(req.courseId())
                .orElseThrow(() -> new RuntimeException("Course %d not found".formatted(req.courseId())));
        Enrollment enrollment = EnrollmentMapper.toEntity(req);
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return EnrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponse get(Long id){
        return enrollmentRepository.findById(id).map(EnrollmentMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Enrollment %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> listByStudent(Long studentId){
        return enrollmentRepository.findByStudent_Id(studentId).stream().map(EnrollmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> listByCourse(Long courseId){
        return enrollmentRepository.findByCourse_Id(courseId).stream().map(EnrollmentMapper::toResponse)
                .toList();
    }

    @Override
    public EnrollmentResponse update(Long id, EnrollmentUpdateRequest req){
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment %d not found".formatted(id)));
        enrollment.setStatus(req.status());

        return EnrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    public void delete(Long id){
        enrollmentRepository.deleteById(id);
    }
}
