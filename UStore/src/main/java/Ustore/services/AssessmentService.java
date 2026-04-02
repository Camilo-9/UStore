
package Ustore.services;

import Ustore.api.dto.AssessmentDtos.*;
import java.util.List;

public interface AssessmentService{
    AssessmentResponse create(AssessmentCreateRequest req);
    AssessmentResponse get(Long id);
    List<AssessmentResponse> list();

    void delete(Long id);
}
