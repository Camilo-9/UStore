
package edu.unimagdalena.uStore.api.dto.controllers;

import edu.unimagdalena.uStore.services.CategoryService;
import edu.unimagdalena.uStore.api.dto.request.CreateCategoryRequest;
import edu.unimagdalena.uStore.api.dto.response.CategoryResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController{
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody
                                                   @Valid
                                                   CreateCategoryRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll(){
        return ResponseEntity.ok(categoryService.findAll());
    }
}
