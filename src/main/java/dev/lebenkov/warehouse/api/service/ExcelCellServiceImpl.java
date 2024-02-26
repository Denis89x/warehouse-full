package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.ProductTypeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelCellServiceImpl implements ExcelCellService {

    private final StylizeExcelService stylizeExcelService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public void createOrderCell(Row row, int columnCount, Object valueOfCell, CellStyle style, XSSFSheet sheet) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);

        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
            cell.setCellStyle(style);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
            cell.setCellStyle(style);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
            cell.setCellStyle(style);
        } else if (valueOfCell instanceof LocalDate) {
            cell.setCellValue(((LocalDate) valueOfCell).format(DATE_FORMATTER));
            cell.setCellStyle(style);
        } else if (valueOfCell instanceof ProductTypeResponse) {
            cell.setCellValue(((ProductTypeResponse) valueOfCell).getName());
            cell.setCellStyle(style);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
    }

    @Override
    public void createDateRangeRow(LocalDate startDate, LocalDate endDate, CellStyle dateStyle, XSSFSheet sheet, Integer rowIndex) {
        Row dateRow = sheet.createRow(rowIndex);
        createOrderCell(dateRow, 0, "от " + startDate + " до " + endDate, dateStyle, sheet);
    }

    @Override
    public void createHeaderRow(CellStyle titleStyle, Integer rowIndex, XSSFSheet sheet, String title) {
        Row headerRow = sheet.createRow(rowIndex);
        createOrderCell(headerRow, 0, title, titleStyle, sheet);
    }

    @Override
    public void createTableHeaderRow(CellStyle tableHeaderStyle, Integer rowIndex, Integer[] columnIndexes, String[] titleColumns, XSSFSheet sheet) {
        Row tableHeaderRow = sheet.createRow(rowIndex);
        for (int i = 0; i < columnIndexes.length; i++) {
            log.info("title: {}, index: {}", titleColumns[i], i);
            createOrderCell(tableHeaderRow, columnIndexes[i], titleColumns[i], tableHeaderStyle, sheet);
        }
    }

    @Override
    public void createTableDataRow(Object[] titleRows, Integer rowCount, Integer[] columnIndexes, CellStyle tableStyle, XSSFSheet sheet) {
        Row row = sheet.createRow(rowCount);
        for (int i = 0; i < columnIndexes.length; i++) {
            createOrderCell(row, columnIndexes[i], titleRows[i], tableStyle, sheet);
        }
    }

    public void createAuthorFooter(XSSFWorkbook workbook, int rowCount, XSSFFont textFont, XSSFSheet sheet) {
        CellStyle authorStyle = stylizeExcelService.stylizeLabel(workbook, (byte) rowCount, (byte) rowCount, (byte) 0, (byte) 4, false, sheet);
        authorStyle.setFont(textFont);

        Row footerRow = sheet.createRow(rowCount + 1);
        createOrderCell(footerRow, 0, "Составил" + "_".repeat(20), authorStyle, sheet);
    }
}
