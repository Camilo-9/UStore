
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.entities.Product;
import edu.unimagdalena.uStore.entities.Inventory;
import edu.unimagdalena.uStore.repositories.InventoryRepository;
import edu.unimagdalena.uStore.repositories.OrderRepository;
import edu.unimagdalena.uStore.services.mapper.InstructorProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uStore.api.dto.InstructorProfileDtos.InstructorProfileCreateRequest;
import uStore.api.dto.InstructorProfileDtos.InstructorProfileResponse;
import uStore.api.dto.InstructorProfileDtos.InstructorProfileUpdateRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class InstructorProfileServiceImpl implements InstructorProfileService {
    private final InventoryRepository instructorProfileRepository;
    private final OrderRepository instructorRepository;

    @Override
    public InstructorProfileResponse create(InstructorProfileCreateRequest req){
        Product instructor = instructorRepository.findById(req.instructorId())
                .orElseThrow(() -> new RuntimeException("Instructor %d not found".formatted(req.instructorId())));
        Inventory profile = InstructorProfileMapper.toEntity(req);
        profile.setInstructor(instructor);

        return InstructorProfileMapper.toResponse(instructorProfileRepository.save(profile));
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorProfileResponse get(Long id){
        return instructorProfileRepository.findById(id).map(InstructorProfileMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("InstructorProfile %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorProfileResponse getByInstructor(Long instructorId){
        return instructorProfileRepository.findByInstructor_Id(instructorId)
                .map(InstructorProfileMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Profile for instructor %d not found".formatted(instructorId)));
    }

    @Override
    public InstructorProfileResponse update(Long id, InstructorProfileUpdateRequest req){
        Inventory profile = instructorProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("InstructorProfile %d not found".formatted(id)));
        profile.setPhone(req.phone());
        profile.setBio(req.bio());

        return InstructorProfileMapper.toResponse(instructorProfileRepository.save(profile));
    }

    @Override
    public void delete(Long id) {
        instructorProfileRepository.deleteById(id);
    }
}
