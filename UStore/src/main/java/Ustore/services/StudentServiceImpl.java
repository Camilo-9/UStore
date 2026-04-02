
package Ustore.services;

import Ustore.api.dto.StudentDtos.*;
import Ustore.entities.Student;
import Ustore.repositories.StudentRepository;
import Ustore.services.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentRepository;

    @Override
    public StudentResponse create(StudentCreateRequest req){
        if(studentRepository.existsByEmail(req.email())){
            throw new RuntimeException("Student with email '%s' already exists".formatted(req.email()));
        }

        return StudentMapper.toResponse(studentRepository.save(StudentMapper.toEntity(req)));
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse get(Long id){
        return studentRepository.findById(id).map(StudentMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Student %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getByEmail(String email){
        return studentRepository.findByEmail(email).map(StudentMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Student with email '%s' not found".formatted(email)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponse> list(Pageable pageable){
        return studentRepository.findAll(pageable).map(StudentMapper::toResponse);
    }

    @Override
    public StudentResponse update(Long id, StudentUpdateRequest req){
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student %d not found".formatted(id)));
        student.setFullName(req.fullName());
        student.setUpdatedAt(req.updatedAt());

        return StudentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    public void delete(Long id){
        studentRepository.deleteById(id);
    }
}
