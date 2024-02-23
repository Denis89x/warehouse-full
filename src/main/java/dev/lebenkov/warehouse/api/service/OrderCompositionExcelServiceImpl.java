package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.model.OrderComposition;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class OrderCompositionExcelServiceImpl implements OrderCompositionExcelService {

    private XSSFSheet sheet;

    private final OrderCompositionQueryService orderCompositionQueryService;
    private final StylizeExcelService stylizeExcelService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final byte ZERO_COLUMN_INDEX = 0;

    public OrderCompositionExcelServiceImpl(OrderCompositionQueryService orderCompositionQueryService, StylizeExcelService stylizeExcelService) {
        this.orderCompositionQueryService = orderCompositionQueryService;
        this.stylizeExcelService = stylizeExcelService;
    }

    private XSSFSheet createSheet(XSSFWorkbook workbook, String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index != -1) {
            workbook.removeSheetAt(index);
        }
        return workbook.createSheet(sheetName);
    }

    /*private void writeDeliveryNoteExcel(XSSFWorkbook workbook, LocalDate startDate, LocalDate endDate) {
        List<OrderComposition> orderCompositions = orderCompositionQueryService.findOrderCompositionAndDateBetween(startDate, endDate);

        int index = workbook.getSheetIndex("Delivery Note");

        if (index != -1) {
            workbook.removeSheetAt(index);
        }

        sheet = workbook.createSheet("Delivery Note");

        Row dateRow = sheet.createRow(2);
        Row headerRow = sheet.createRow(0);
        Row tableHeaderRow = sheet.createRow(3);

        CellStyle dateStyle = stylizeLabel(workbook, (byte) 2, (byte) 2, (byte) 0, (byte) 4, false);
        CellStyle titleStyle = stylizeLabel(workbook, (byte) 0, (byte) 0, (byte) 0, (byte) 4, true);
        CellStyle tableStyle = stylizeExcelService.stylizeWorkbook(workbook);
        CellStyle tableHeaderStyle = stylizeExcelService.stylizeWorkbook(workbook);

        XSSFFont titleFont = workbook.createFont();
        XSSFFont textFont = workbook.createFont();
        XSSFFont tableHeaderFont = workbook.createFont();

        titleFont.setBold(true);
        titleFont.setFontHeight(13);

        tableHeaderFont.setBold(true);
        tableHeaderFont.setFontHeight(13);

        textFont.setFontHeight(12);

        tableStyle.setFont(textFont);
        titleStyle.setFont(titleFont);
        tableHeaderStyle.setFont(tableHeaderFont);

        createOrderCell(dateRow, 0, "от " + startDate + " до " + endDate, dateStyle);
        createOrderCell(headerRow, 0, "ТОВАРНАЯ НАКЛАДНАЯ", titleStyle);

        createOrderCell(tableHeaderRow, 0, "№", tableHeaderStyle);
        createOrderCell(tableHeaderRow, 1, "Наименование", tableHeaderStyle);
        createOrderCell(tableHeaderRow, 2, "Количество", tableHeaderStyle);
        createOrderCell(tableHeaderRow, 3, "Цена", tableHeaderStyle);
        createOrderCell(tableHeaderRow, 4, "Сумма", tableHeaderStyle);

        int rowCount = 4;
        int rowId = 1;

        for (OrderComposition orderComposition : orderCompositions) {
            Row row = sheet.createRow(rowCount++);

            createOrderCell(row, 0, rowId, tableStyle);
            createOrderCell(row, 1, orderComposition.getProduct().getTitle(), tableStyle);
            createOrderCell(row, 2, orderComposition.getQuantity(), tableStyle);
            createOrderCell(row, 3, orderComposition.getProduct().getCost(), tableStyle);
            createOrderCell(row, 4, (orderComposition.getProduct().getCost() * orderComposition.getQuantity()), tableStyle);

            rowId++;
        }

        rowCount += 1;
        CellStyle authorStyle = stylizeLabel(workbook, (byte) rowCount, (byte) rowCount, (byte) 0, (byte) 4, false);
        authorStyle.setFont(textFont);

        Row footerRow = sheet.createRow(rowCount);

        createOrderCell(footerRow, ZERO_COLUMN_INDEX, "Составил" + "_".repeat(20), authorStyle);
    }*/

    private void createOrderCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
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
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
    }

    private CellStyle stylizeLabel(Workbook workbook, byte startRow, byte endRow, byte startColumn, byte endColumn, boolean isLabel) {
        CellStyle style = workbook.createCellStyle();

        if (isLabel) {
            style.setAlignment(HorizontalAlignment.CENTER);
        }

        style.setVerticalAlignment(VerticalAlignment.CENTER);

        sheet.addMergedRegion(new CellRangeAddress(
                startRow, // начальная строка
                endRow, // конечная строка
                startColumn, // начальная колонка
                endColumn  // конечная колонка
        ));

        return style;
    }

    private void writeDeliveryNoteExcel(XSSFWorkbook workbook, LocalDate startDate, LocalDate endDate) {
        List<OrderComposition> orderCompositions = getOrderCompositions(startDate, endDate);

        createDeliveryNoteSheet(workbook, startDate, endDate);

        int rowCount = 4;
        int rowId = 1;

        CellStyle tableStyle = stylizeExcelService.stylizeWorkbook(workbook);
        XSSFFont textFont = workbook.createFont();
        textFont.setFontHeight(12);
        tableStyle.setFont(textFont);

        for (OrderComposition orderComposition : orderCompositions) {
            createOrderRow(orderComposition, rowCount++, rowId++, tableStyle);
        }

        createAuthorFooter(workbook, rowCount, textFont);
    }

    private List<OrderComposition> getOrderCompositions(LocalDate startDate, LocalDate endDate) {
        return orderCompositionQueryService.findOrderCompositionAndDateBetween(startDate, endDate);
    }

    private void createDeliveryNoteSheet(XSSFWorkbook workbook, LocalDate startDate, LocalDate endDate) {
        int index = workbook.getSheetIndex("Delivery Note");
        if (index != -1) {
            workbook.removeSheetAt(index);
        }
        sheet = workbook.createSheet("Delivery Note");

        CellStyle dateStyle = stylizeLabel(workbook, (byte) 2, (byte) 2, (byte) 0, (byte) 4, false);
        CellStyle titleStyle = stylizeLabel(workbook, (byte) 0, (byte) 0, (byte) 0, (byte) 4, true);

        CellStyle tableHeaderStyle = stylizeExcelService.stylizeWorkbook(workbook);

        XSSFFont titleFont = workbook.createFont();

        XSSFFont tableHeaderFont = workbook.createFont();

        titleFont.setBold(true);
        titleFont.setFontHeight(13);

        tableHeaderFont.setBold(true);
        tableHeaderFont.setFontHeight(13);

        titleStyle.setFont(titleFont);
        tableHeaderStyle.setFont(tableHeaderFont);

        createTitleRow(startDate, endDate, dateStyle);
        createHeaderRow(titleStyle);
        createTableHeaderRow(tableHeaderStyle);
    }

    private void createTitleRow(LocalDate startDate, LocalDate endDate, CellStyle dateStyle) {
        Row dateRow = sheet.createRow(2);
        createOrderCell(dateRow, 0, "от " + startDate + " до " + endDate, dateStyle);
    }

    private void createHeaderRow(CellStyle titleStyle) {
        Row headerRow = sheet.createRow(0);
        createOrderCell(headerRow, 0, "ТОВАРНАЯ НАКЛАДНАЯ", titleStyle);
    }

    private void createTableHeaderRow(CellStyle tableHeaderStyle) {
        Row tableHeaderRow = sheet.createRow(3);
        createOrderCell(tableHeaderRow, 0, "№", tableHeaderStyle);
        createOrderCell(tableHeaderRow, 1, "Наименование", tableHeaderStyle);
        createOrderCell(tableHeaderRow, 2, "Количество", tableHeaderStyle);
        createOrderCell(tableHeaderRow, 3, "Цена", tableHeaderStyle);
        createOrderCell(tableHeaderRow, 4, "Сумма", tableHeaderStyle);
    }

    private void createOrderRow(OrderComposition orderComposition, int rowCount, int rowId, CellStyle tableStyle) {
        Row row = sheet.createRow(rowCount);
        createOrderCell(row, 0, rowId, tableStyle);
        createOrderCell(row, 1, orderComposition.getProduct().getTitle(), tableStyle);
        createOrderCell(row, 2, orderComposition.getQuantity(), tableStyle);
        createOrderCell(row, 3, orderComposition.getProduct().getCost(), tableStyle);
        createOrderCell(row, 4, (orderComposition.getProduct().getCost() * orderComposition.getQuantity()), tableStyle);
    }

    private void createAuthorFooter(XSSFWorkbook workbook, int rowCount, XSSFFont textFont) {
        CellStyle authorStyle = stylizeLabel(workbook, (byte) rowCount, (byte) rowCount, (byte) 0, (byte) 4, false);
        authorStyle.setFont(textFont);

        Row footerRow = sheet.createRow(rowCount + 1);
        createOrderCell(footerRow, ZERO_COLUMN_INDEX, "Составил" + "_".repeat(20), authorStyle);
    }

    @Override
    public void generateDeliveryNoteExcel(HttpServletResponse response, LocalDate startDate, LocalDate endDate) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        writeDeliveryNoteExcel(workbook, startDate, endDate);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=example.xlsx");

        ServletOutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}