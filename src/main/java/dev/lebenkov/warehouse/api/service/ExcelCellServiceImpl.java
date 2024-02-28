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
import java.util.List;

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
        } else if (valueOfCell instanceof List<?> list) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 1, j = 0; j < list.size(); i++, j++) {
                stringBuilder.append(list.get(j).toString());
                if (i % 3 == 0 && i < list.size()) {
                    stringBuilder.append("\n");
                } else {
                    stringBuilder.append(", ");
                }
            }

            String valueAsString = stringBuilder.toString();

            cell.setCellValue(valueAsString.substring(0, valueAsString.length() - 2));
            cell.setCellStyle(style);

            CellStyle multiLineCellStyle = stylizeExcelService.stylizeWorkbook((XSSFWorkbook) cell.getSheet().getWorkbook(), false);

            multiLineCellStyle.setWrapText(true);
            cell.setCellStyle(multiLineCellStyle);
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
            createOrderCell(tableHeaderRow, columnIndexes[i], titleColumns[i], tableHeaderStyle, sheet);
        }
    }

    @Override
    public void createExcelHeaderInfo(XSSFWorkbook workbook, String[] titleColumns, XSSFSheet sheet) {
        for (int i = 0; i < titleColumns.length; i++) {
            Row row = sheet.createRow(i);
            CellStyle style = stylizeExcelService.stylizeLabel(workbook, (byte) i, (byte) i, (byte) 0, (byte) 7, false, sheet);

            createOrderCell(row, 0, titleColumns[i], style, sheet);
        }
    }

    @Override
    public void createTableDataRow(Object[] titleRows, Integer rowCount, Integer[] columnIndexes, CellStyle tableStyle, XSSFSheet sheet) {
        Row row = sheet.createRow(rowCount);
        for (int i = 0; i < columnIndexes.length; i++) {
            createOrderCell(row, columnIndexes[i], titleRows[i], tableStyle, sheet);
        }
    }

    @Override
    public void createAuthorFooter(XSSFWorkbook workbook, int rowCount, XSSFFont textFont, XSSFSheet sheet) {
        CellStyle authorStyle = stylizeExcelService.stylizeLabel(workbook, (byte) rowCount, (byte) rowCount, (byte) 0, (byte) 4, false, sheet);
        authorStyle.setFont(textFont);

        Row footerRow = sheet.createRow(rowCount + 1);
        createOrderCell(footerRow, 0, "Составил" + "_".repeat(20), authorStyle, sheet);
    }

    @Override
    public void createOrderExcelInfo(XSSFWorkbook workbook, Integer[] rowIndexes, String[] columnTitles, XSSFSheet sheet) {
        CellStyle style = stylizeExcelService.stylizeLabel(workbook, (byte) 0, (byte) 0, (byte) 0, (byte) 7, false, sheet);

        for (int i = 0; i < rowIndexes.length; i++) {
            Row row = sheet.createRow(rowIndexes[i]);

            createOrderCell(row, 0, columnTitles[i], style, sheet);
        }
    }
}