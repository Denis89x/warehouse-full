package dev.lebenkov.warehouse.api.service;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.LocalDate;

public interface ExcelCellService {
    void createOrderCell(Row row, int columnCount, Object valueOfCell, CellStyle style, XSSFSheet sheet);

    void createDateRangeRow(LocalDate startDate, LocalDate endDate, CellStyle dateStyle, XSSFSheet sheet, Integer rowIndex);

    void createHeaderRow(CellStyle titleStyle, Integer rowIndex, XSSFSheet sheet, String title);

    void createTableHeaderRow(CellStyle tableHeaderStyle, Integer rowIndex, Integer[] columnIndexes, String[] titleColumns, XSSFSheet sheet);

    void createTableDataRow(Object[] titleRows, Integer rowCount, Integer[] columnIndexes, CellStyle tableStyle, XSSFSheet sheet);

    void createAuthorFooter(XSSFWorkbook workbook, int rowCount, XSSFFont textFont, XSSFSheet sheet);
}
