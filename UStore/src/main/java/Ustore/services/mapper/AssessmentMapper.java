
package Ustore.services.mapper;

import Ustore.api.dto.AssessmentDtos;
import Ustore.entities.Assessment;

public class AssessmentMapper{
    public static Assessment toEntity(AssessmentDtos.AssessmentCreateRequest req){
        return Assessment.builder().type(req.type()).score(req.score()).takenAt(req.takenAt()).build();
    }

    public static AssessmentDtos.AssessmentResponse toResponse(Assessment a){
        return new AssessmentDtos.AssessmentResponse(a.getId(), a.getType(), a.getScore(), a.getTakenAt(), a.getStudent() != null ? a.getStudent().getId():null, a.getCourse() != null ? a.getCourse().getId():null);
    }
}
