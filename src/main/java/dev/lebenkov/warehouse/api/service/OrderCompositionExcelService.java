package dev.lebenkov.warehouse.api.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

public interface OrderCompositionExcelService {
    void generateDeliveryNoteExcel(HttpServletResponse response, LocalDate startDate, LocalDate endDate) throws IOException;
}