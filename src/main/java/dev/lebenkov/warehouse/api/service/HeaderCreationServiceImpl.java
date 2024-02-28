package dev.lebenkov.warehouse.api.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HeaderCreationServiceImpl implements HeaderCreationService {

    CellCreationService cellCreationService;
    StylizeExcelService stylizeExcelService;

    @Override
    public void createHeaderRow(CellStyle titleStyle, Integer rowIndex, XSSFSheet sheet, String title) {
        Row headerRow = sheet.createRow(rowIndex);
        cellCreationService.createCell(headerRow, 0, title, titleStyle, sheet);
    }

    @Override
    public void createTableHeaderRow(CellStyle tableHeaderStyle, Integer rowIndex, Integer[] columnIndexes, String[] titleColumns, XSSFSheet sheet) {
        Row tableHeaderRow = sheet.createRow(rowIndex);
        for (int i = 0; i < columnIndexes.length; i++) {
            cellCreationService.createCell(tableHeaderRow, columnIndexes[i], titleColumns[i], tableHeaderStyle, sheet);
        }
    }

    @Override
    public void createExcelHeaderInfo(XSSFWorkbook workbook, String[] titleColumns, XSSFSheet sheet) {
        for (int i = 0; i < titleColumns.length; i++) {
            Row row = sheet.createRow(i);
            CellStyle style = stylizeExcelService.stylizeLabel(workbook, (byte) i, (byte) i, (byte) 0, (byte) 7, false, sheet);

            cellCreationService.createCell(row, 0, titleColumns[i], style, sheet);
        }
    }
}
