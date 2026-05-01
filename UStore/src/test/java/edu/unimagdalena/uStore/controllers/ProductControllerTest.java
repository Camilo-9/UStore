
package edu.unimagdalena.uStore.controllers;

import edu.unimagdalena.uStore.api.dto.controllers.ProductController;
import edu.unimagdalena.uStore.api.dto.response.ProductResponse;
import edu.unimagdalena.uStore.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest{
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void debeCrearProducto() throws Exception{
        ProductResponse response = new ProductResponse();
        response.setId(1L);

        when(productService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "sku": "ABC123",
                  "name": "Galletas",
                  "description": "Deliciosas galletas para acompañar con leche",
                  "price": 100,
                  "categoryId": 1
                }
            """)).andExpect(status().isCreated());
    }

    @Test
    void noDebeCrearProductoConPrecioInvalido() throws Exception{
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "sku": "ABC123",
                  "name": "Jabón para manos",
                  "description": "Fragancia a lavanda que deja una piel suave",
                  "price": -100,
                  "categoryId": 2
                }
            """)).andExpect(status().isBadRequest());
    }
}
