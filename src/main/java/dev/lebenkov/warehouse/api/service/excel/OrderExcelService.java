package dev.lebenkov.warehouse.api.service.excel;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

public interface OrderExcelService {

    void generateExcelByDateRange(HttpServletResponse response, LocalDate startDate, LocalDate endDate) throws IOException;

    void generateExcelBySupplierId(HttpServletResponse response, LocalDate startDate, LocalDate endDate, Long supplierId) throws IOException;

    void generateOrderExcel(HttpServletResponse response, Long orderId) throws IOException;
}
