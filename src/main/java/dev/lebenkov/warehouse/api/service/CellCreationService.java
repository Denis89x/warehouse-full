package dev.lebenkov.warehouse.api.service;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public interface CellCreationService {
    void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style, XSSFSheet sheet);
    void createTableDataRow(Object[] titleRows, Integer rowCount, Integer[] columnIndexes, CellStyle tableStyle, XSSFSheet sheet);
}
