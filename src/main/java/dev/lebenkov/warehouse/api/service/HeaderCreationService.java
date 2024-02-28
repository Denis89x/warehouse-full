package dev.lebenkov.warehouse.api.service;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface HeaderCreationService {
    void createHeaderRow(CellStyle titleStyle, Integer rowIndex, XSSFSheet sheet, String title);
    void createTableHeaderRow(CellStyle tableHeaderStyle, Integer rowIndex, Integer[] columnIndexes, String[] titleColumns, XSSFSheet sheet);
    void createExcelHeaderInfo(XSSFWorkbook workbook, String[] titleColumns, XSSFSheet sheet);
}
