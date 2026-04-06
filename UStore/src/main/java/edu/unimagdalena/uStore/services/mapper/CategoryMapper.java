
package edu.unimagdalena.uStore.services.mapper;

import edu.unimagdalena.uStore.entities.Category;
import edu.unimagdalena.uStore.api.dto.response.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper{
    public CategoryResponse toResponse(Category category){
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());

        return response;
    }
}
