
package edu.unimagdalena.uStore.controllers;

import edu.unimagdalena.uStore.api.dto.controllers.ReportController;
import edu.unimagdalena.uStore.exceptions.BusinessException;
import edu.unimagdalena.uStore.services.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(ReportController.class)
class ReportControllerTest{
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReportService reportService;

    @Test
    void debeObtenerProductosMasVendidos() throws Exception{
        when(reportService.findBestSellingProducts(any(), any())).thenReturn(List.of());

        mockMvc.perform(get("/api/reports/best-selling-products")
               .param("startDate", "2024-01-01T00:00:00")
               .param("endDate", "2024-12-31T23:59:59")).andExpect(status().isOk());
    }

    @Test
    void debeObtenerIngresoMensual() throws Exception{
        when(reportService.findMonthlyIncome()).thenReturn(List.of());

        mockMvc.perform(get("/api/reports/monthly-income")).andExpect(status().isOk());
    }

    @Test
    void debeObtenerMejoresClientes() throws Exception{
        when(reportService.findTopCustomers()).thenReturn(List.of());

        mockMvc.perform(get("/api/reports/top-customers")).andExpect(status().isOk());
    }

    @Test
    void debeRetornarErrorSiServicioFalla() throws Exception{
        when(reportService.findBestSellingProducts(any(), any())).thenThrow(new BusinessException(
                                                                            "Error en reporte"));

        mockMvc.perform(get("/api/reports/best-selling-products")
               .param("startDate", "2024-01-01T00:00:00")
               .param("endDate", "2024-12-31T23:59:59")).andExpect(status()
               .isUnprocessableEntity());
    }
}
