
package edu.unimagdalena.uStore.api.dto.controllers;

import edu.unimagdalena.uStore.services.ProductService;
import edu.unimagdalena.uStore.api.dto.request.CreateProductRequest;
import edu.unimagdalena.uStore.api.dto.request.UpdateInventoryRequest;
import edu.unimagdalena.uStore.api.dto.request.UpdateProductRequest;
import edu.unimagdalena.uStore.api.dto.response.InventoryResponse;
import edu.unimagdalena.uStore.api.dto.response.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController{
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody
                                                  @Valid
                                                  CreateProductRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable
                                                   Long id){

        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll(){
        return ResponseEntity.ok(productService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable
                                                  Long id,

                                                  @RequestBody
                                                  @Valid
                                                  UpdateProductRequest request){

        return ResponseEntity.ok(productService.update(id, request));
    }

    @PutMapping("/{id}/inventory")
    public ResponseEntity<InventoryResponse> updateInventory(@PathVariable
                                                             Long id,

                                                             @RequestBody
                                                             @Valid
                                                             UpdateInventoryRequest request){

        return ResponseEntity.ok(productService.updateInventory(id, request).getInventory());
    }
}
