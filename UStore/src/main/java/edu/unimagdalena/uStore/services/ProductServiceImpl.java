
package edu.unimagdalena.uStore.services;

import edu.unimagdalena.uStore.entities.Category;
import edu.unimagdalena.uStore.entities.Inventory;
import edu.unimagdalena.uStore.entities.Product;
import edu.unimagdalena.uStore.repositories.InventoryRepository;
import edu.unimagdalena.uStore.repositories.ProductRepository;
import edu.unimagdalena.uStore.repositories.CategoryRepository;
import edu.unimagdalena.uStore.api.dto.request.CreateProductRequest;
import edu.unimagdalena.uStore.api.dto.request.UpdateProductRequest;
import edu.unimagdalena.uStore.api.dto.request.UpdateInventoryRequest;
import edu.unimagdalena.uStore.api.dto.response.ProductResponse;
import edu.unimagdalena.uStore.exceptions.ConflictException;
import edu.unimagdalena.uStore.exceptions.ResourceNotFoundException;
import edu.unimagdalena.uStore.services.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryServiceImpl inventoryService;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
                              InventoryRepository inventoryRepository, InventoryServiceImpl inventoryService,
                              ProductMapper productMapper){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.inventoryRepository = inventoryRepository;
        this.inventoryService = inventoryService;
        this.productMapper = productMapper;
    }

    public Product getOrThrow(Long id){
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
        "Producto no encontrado con id: "+ id));
    }

    @Override
    public ProductResponse create(CreateProductRequest request){
        if(productRepository.existsBySku(request.getSku())){
            throw new ConflictException("Ya existe un producto con el SKU: "+ request.getSku());
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                            "Categoría con id: "+ request.getCategoryId()+ "no encontrada."));
        Product product = new Product();
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(category);
        Product saved = productRepository.save(product);

        Inventory inventory = new Inventory();
        inventory.setProduct(saved);
        inventory.setAvailableStock(0);
        inventory.setMinimumStock(0);
        inventoryRepository.save(inventory);

        return productMapper.toResponse(saved, inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(Long id){
        Product product = getOrThrow(id);
        Inventory inventory = inventoryRepository.findByProductId(id).orElse(null);

        return productMapper.toResponse(product, inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findAll(){
        return productRepository.findAll().stream().map(p -> {
                    Inventory inventory = inventoryRepository.findByProductId(p.getId()).orElse(null);

                    return productMapper.toResponse(p, inventory);}).toList();
    }

    @Override
    public ProductResponse update(Long id, UpdateProductRequest request){
        Product product = getOrThrow(id);

        Category category = categoryRepository.findById(request.getCategoryId())
                            .orElseThrow(() -> new ResourceNotFoundException("Categoría con id: "+
                            request.getCategoryId()+ "no encontrada."));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(category);

        Product saved = productRepository.save(product);
        Inventory inventory = inventoryRepository.findByProductId(id).orElse(null);

        return productMapper.toResponse(saved, inventory);
    }

    @Override
    public ProductResponse updateInventory(Long id, UpdateInventoryRequest request){
        Product product = getOrThrow(id);
        inventoryService.update(id, request);
        Inventory inventory = inventoryRepository.findByProductId(id).orElse(null);

        return productMapper.toResponse(product, inventory);
    }
}
