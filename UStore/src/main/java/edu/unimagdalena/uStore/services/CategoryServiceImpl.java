
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.entities.Category;
import edu.unimagdalena.uStore.repositories.CategoryRepository;
import edu.unimagdalena.uStore.api.dto.response.CategoryResponse;
import edu.unimagdalena.uStore.api.dto.request.CreateCategoryRequest;
import edu.unimagdalena.uStore.exceptions.ConflictException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    private CategoryResponse toResponse(Category category){
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());

        return response;
    }

    @Override
    public CategoryResponse create(CreateCategoryRequest request){
        if(categoryRepository.existsByName(request.getName())){
            throw new ConflictException("Ya existe una categoría con el nombre: "+ request.getName());
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        Category saved = categoryRepository.save(category);

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll(){
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }
}
