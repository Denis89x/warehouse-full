package dev.lebenkov.warehouse.api.service.excel;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

public interface ProductChartExcel {
    void generateProductChartExcel(HttpServletResponse response, LocalDate startDate, LocalDate endDate) throws IOException;
}
