
package edu.unimagdalena.uStore.controllers;

import edu.unimagdalena.uStore.api.dto.controllers.OrderController;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.unimagdalena.uStore.api.dto.controllers.OrderController;
import edu.unimagdalena.uStore.services.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;
}
