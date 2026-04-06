
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.entities.Category;
import edu.unimagdalena.uStore.repositories.CategoryRepository;
import edu.unimagdalena.uStore.api.dto.response.CategoryResponse;
import edu.unimagdalena.uStore.api.dto.request.CreateCategoryRequest;
import edu.unimagdalena.uStore.exceptions.ConflictException;
import edu.unimagdalena.uStore.services.mapper.CategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper){
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
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

        return categoryMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll(){
        return categoryRepository.findAll().stream().map(categoryMapper::toResponse).toList();
    }
}
