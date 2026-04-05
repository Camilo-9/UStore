
package edu.unimagdalena.uStore.services;

import uStore.api.dto.AssessmentDtos.AssessmentCreateRequest;
import uStore.api.dto.AssessmentDtos.AssessmentResponse;

import java.util.List;

public interface AssessmentService{
    AssessmentResponse create(AssessmentCreateRequest req);
    AssessmentResponse get(Long id);
    List<AssessmentResponse> list();

    void delete(Long id);
}
