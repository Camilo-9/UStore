
package edu.unimagdalena.uStore.controllers;

import edu.unimagdalena.uStore.api.dto.controllers.OrderController;
import edu.unimagdalena.uStore.api.dto.response.OrderResponse;
import edu.unimagdalena.uStore.exceptions.BusinessException;
import edu.unimagdalena.uStore.exceptions.ResourceNotFoundException;
import edu.unimagdalena.uStore.security.jwt.JwtAuthenticationFilter;
import edu.unimagdalena.uStore.services.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest{
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void debeCrearPedido() throws Exception{
        OrderResponse response = new OrderResponse();
        response.setId(1L);

        when(orderService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "customerId": 1,
                  "addressId": 1,
                  "items": [
                    {"productId": 2, "quantity": 4},
                    {"productId": 3, "quantity": 6}
                  ]
                }
            """)).andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void noDebeCrearPedidoSinItems() throws Exception{
        when(orderService.create(any())).thenThrow(new BusinessException("El pedido debe contener al menos un ítem."));

        mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "customerId": 1,
                  "addressId": 4,
                  "items": []
                }
            """)).andExpect(status().isBadRequest());
    }

    @Test
    void debeRetornarErrorPorStockInsuficienteAlPagar() throws Exception{
        when(orderService.pay(1L)).thenThrow(new BusinessException("Stock insuficiente"));

        mockMvc.perform(put("/api/orders/1/pay")).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void debePagarPedido() throws Exception{
        OrderResponse response = new OrderResponse();
        response.setId(1L);

        when(orderService.pay(1L)).thenReturn(response);

        mockMvc.perform(put("/api/orders/1/pay")).andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void debeEnviarPedidoExitosamente() throws Exception{
        OrderResponse response = new OrderResponse();
        response.setId(1L);

        when(orderService.ship(1L)).thenReturn(response);

        mockMvc.perform(put("/api/orders/1/ship")).andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void noDebeEnviarPedidoNoPagado() throws Exception{
        when(orderService.ship(1L)).thenThrow(new BusinessException("Estado inválido"));

        mockMvc.perform(put("/api/orders/1/ship")).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void debeRetornar404SiPedidoNoExiste() throws Exception{
        when(orderService.findById(1L)).thenThrow(new ResourceNotFoundException("No existe"));

        mockMvc.perform(get("/api/orders/1")).andExpect(status().isNotFound());
    }
}
