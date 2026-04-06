
package edu.unimagdalena.uStore.services.mapper;

import edu.unimagdalena.uStore.api.dto.request.CreateCategoryRequest;
import edu.unimagdalena.uStore.entities.Category;

public class AssessmentMapper{
    public static Category toEntity(CreateCategoryRequest.AssessmentCreateRequest req){
        return Category.builder().type(req.type()).score(req.score()).takenAt(req.takenAt()).build();
    }

    public static CreateCategoryRequest.AssessmentResponse toResponse(Category a){
        return new CreateCategoryRequest.AssessmentResponse(a.getId(), a.getType(), a.getScore(), a.getTakenAt(), a.getStudent() != null ? a.getStudent().getId():null, a.getCourse() != null ? a.getCourse().getId():null);
    }
}
