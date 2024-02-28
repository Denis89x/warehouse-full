package dev.lebenkov.warehouse.api.service;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface StylizeExcelService {
    CellStyle stylizeWorkbook(XSSFWorkbook workbook, boolean isHeader);

    CellStyle stylizeLabel(Workbook workbook, byte startRow, byte endRow, byte startColumn, byte endColumn, boolean isLabel, XSSFSheet sheet);

    CellStyle stylizeDateRow(Workbook workbook, byte startRow, byte endRow, byte startColumn, byte endColumn, XSSFSheet sheet);

    CellStyle stylizeTitleRow(XSSFWorkbook workbook, byte startRow, byte endRow, byte startColumn, byte endColumn, XSSFSheet sheet);
}
