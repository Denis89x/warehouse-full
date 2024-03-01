package dev.lebenkov.warehouse.api.service.excel;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface OrderExcelInfoCreationService {
    void createOrderExcelInfo(XSSFWorkbook workbook, Integer[] rowIndexes, String[] columnTitles, XSSFSheet sheet);
}
