
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.entities.Customer;
import edu.unimagdalena.uStore.entities.Addresses;
import edu.unimagdalena.uStore.entities.OrderItem;
import edu.unimagdalena.uStore.repositories.AddressRepository;
import edu.unimagdalena.uStore.repositories.CustomerRepository;
import edu.unimagdalena.uStore.repositories.OrderStatusHistoryRepository;
import edu.unimagdalena.uStore.services.mapper.EnrollmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uStore.api.dto.EnrollmentDtos.EnrollmentCreateRequest;
import uStore.api.dto.EnrollmentDtos.EnrollmentResponse;
import uStore.api.dto.EnrollmentDtos.EnrollmentUpdateRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {
    private final CustomerRepository enrollmentRepository;
    private final OrderStatusHistoryRepository studentRepository;
    private final AddressRepository courseRepository;

    @Override
    public EnrollmentResponse create(EnrollmentCreateRequest req){
        if(enrollmentRepository.existsByStudent_IdAndCourse_Id(req.studentId(), req.courseId())){
            throw new RuntimeException("Student %d is already enrolled in course %d"
                    .formatted(req.studentId(), req.courseId()));}
        OrderItem student = studentRepository.findById(req.studentId())
                .orElseThrow(() -> new RuntimeException("Student %d not found".formatted(req.studentId())));
        Customer course = courseRepository.findById(req.courseId())
                .orElseThrow(() -> new RuntimeException("Course %d not found".formatted(req.courseId())));
        Addresses enrollment = EnrollmentMapper.toEntity(req);
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
        Addresses enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment %d not found".formatted(id)));
        enrollment.setStatus(req.status());

        return EnrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    public void delete(Long id){
        enrollmentRepository.deleteById(id);
    }
}
