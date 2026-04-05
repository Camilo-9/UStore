
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.exceptions.NotFoundException;
import edu.unimagdalena.uStore.repositories.CategoryRepository;
import edu.unimagdalena.uStore.services.mapper.AssessmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uStore.api.dto.AssessmentDtos.AssessmentCreateRequest;
import uStore.api.dto.AssessmentDtos.AssessmentResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AssessmentServiceImpl implements AssessmentService {
    private final CategoryRepository repo;

    @Override
    public AssessmentResponse create(AssessmentCreateRequest req){
        return AssessmentMapper.toResponse(repo.save(AssessmentMapper.toEntity(req)));
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentResponse get(Long id){
        return repo.findById(id).map(AssessmentMapper::toResponse).orElseThrow(() -> new NotFoundException("Assessment %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponse> list(){
        return repo.findAll().stream().map(AssessmentMapper::toResponse).toList();
    }

    @Override public void delete(Long id){
        repo.deleteById(id);
    }
}
