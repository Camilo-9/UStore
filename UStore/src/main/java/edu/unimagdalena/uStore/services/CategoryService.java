
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.api.dto.request.CreateCategoryRequest;
import edu.unimagdalena.uStore.api.dto.response.CategoryResponse;
import java.util.List;

public interface CategoryService{
    CategoryResponse create(CreateCategoryRequest request);
    List<CategoryResponse> findAll();
}
