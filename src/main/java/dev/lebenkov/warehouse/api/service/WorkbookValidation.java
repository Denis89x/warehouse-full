package dev.lebenkov.warehouse.api.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface WorkbookValidation {
    void checkWorkbookWithSameName(XSSFWorkbook workbook, String sheetIndex);
}
