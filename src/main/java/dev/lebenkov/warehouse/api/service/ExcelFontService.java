package dev.lebenkov.warehouse.api.service;

import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface ExcelFontService {
    XSSFFont createTextFont(XSSFWorkbook workbook);
}
