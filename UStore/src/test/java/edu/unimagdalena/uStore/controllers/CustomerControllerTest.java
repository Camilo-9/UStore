
package edu.unimagdalena.uStore.controllers;

import edu.unimagdalena.uStore.api.dto.controllers.CustomerController;
import edu.unimagdalena.uStore.services.CustomerService;
import edu.unimagdalena.uStore.api.dto.response.CustomerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest{
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void debeCrearCliente() throws Exception{
        CustomerResponse response = new CustomerResponse();
        response.setId(1L);

        when(customerService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "firstName": "Camilo",
                      "lastName": "Alvarez",
                      "email": "camilo@test.com",
                      "phone": "3206482158"
                    }
                """)).andExpect(status().isCreated()).andExpect(jsonPath("$.id")
                     .value(1L));
    }
}
