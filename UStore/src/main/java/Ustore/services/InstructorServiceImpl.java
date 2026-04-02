
package Ustore.services;

import Ustore.api.dto.InstructorDtos.*;
import Ustore.entities.Instructor;
import Ustore.repositories.InstructorRepository;
import Ustore.services.mapper.InstructorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InstructorServiceImpl implements InstructorService{
    private final InstructorRepository instructorRepository;

    @Override
    public InstructorResponse create(InstructorCreateRequest req){
        if(instructorRepository.existsByEmail(req.email())){
            throw new RuntimeException("Instructor with email '%s' already exists".formatted(req.email()));
        }

        return InstructorMapper.toResponse(instructorRepository.save(InstructorMapper.toEntity(req)));
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorResponse get(Long id){
        return instructorRepository.findById(id).map(InstructorMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Instructor %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorResponse getByEmail(String email){
        return instructorRepository.findByEmail(email).map(InstructorMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Instructor with email '%s' not found".formatted(email)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstructorResponse> list(Pageable pageable){
        return instructorRepository.findAll(pageable).map(InstructorMapper::toResponse);
    }

    @Override
    public InstructorResponse update(Long id, InstructorUpdateRequest req){
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor %d not found".formatted(id)));
        instructor.setFullName(req.fullName());
        instructor.setUpdatedAt(req.updatedAt());

        return InstructorMapper.toResponse(instructorRepository.save(instructor));
    }

    @Override
    public void delete(Long id){
        instructorRepository.deleteById(id);
    }
}
