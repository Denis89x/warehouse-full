package dev.lebenkov.warehouse.api.service.excel;

import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface ExcelFontService {
    XSSFFont createTextFont(XSSFWorkbook workbook);
}
