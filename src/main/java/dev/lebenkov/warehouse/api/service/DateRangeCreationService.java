package dev.lebenkov.warehouse.api.service;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.time.LocalDate;

public interface DateRangeCreationService {
    void createDateRangeRow(LocalDate startDate, LocalDate endDate, CellStyle dateStyle, XSSFSheet sheet, Integer rowIndex);
}
