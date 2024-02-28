package dev.lebenkov.warehouse.api.service;

import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface FooterCreationService {
    void createAuthorFooter(XSSFWorkbook workbook, int rowCount, XSSFFont textFont, XSSFSheet sheet);
}
