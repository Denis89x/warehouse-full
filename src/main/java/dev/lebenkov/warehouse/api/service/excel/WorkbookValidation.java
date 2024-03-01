package dev.lebenkov.warehouse.api.service.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface WorkbookValidation {
    void checkWorkbookWithSameName(XSSFWorkbook workbook, String sheetIndex);
}
