package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.OrderResponse;
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
public class OrderExcelServiceImpl implements OrderExcelService {

    private final OrderQueryService orderQueryService;

    private XSSFSheet sheet;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final byte ZERO_COLUMN_INDEX = 0;

    private static final byte ORDER_ID_COLUMN_INDEX = 0;
    private static final byte SUPPLIER_COLUMN_INDEX = 1;
    private static final byte ACCOUNT_COLUMN_INDEX = 2;
    private static final byte STORE_COLUMN_INDEX = 3;
    private static final byte ORDER_TYPE_COLUMN_INDEX = 4;
    private static final byte DATE_COLUMN_INDEX = 5;
    private static final byte AMOUNT_COLUMN_INDEX = 6;
    private static final byte PRODUCTS_COLUMN_INDEX = 7;

    private static final int PRODUCTS_COLUMN_WIDTH = 10000;

    public OrderExcelServiceImpl(OrderQueryService orderQueryService) {
        this.orderQueryService = orderQueryService;
    }

    private void writeOrderHeader(XSSFWorkbook workbook, LocalDate startDate, LocalDate endDate) {
        int index = workbook.getSheetIndex("Order");

        if (index != -1) {
            workbook.removeSheetAt(index);
        }

        sheet = workbook.createSheet("Order");

        Row reportHeaderRow = sheet.createRow(0);
        Row periodRow = sheet.createRow(1);
        Row titleRow = sheet.createRow(2);
        Row addressRow = sheet.createRow(3);

        Row fieldsRow = sheet.createRow(5);

        CellStyle tableStyle = stylizeWorkbook(workbook);
        CellStyle textStyle = stylizeTextInfo(workbook);
        CellStyle reportHeaderRowStyle = stylizeLabel(workbook, (byte) 0, (byte) 0, true);
        CellStyle periodStyle = stylizeLabel(workbook, (byte) 1, (byte) 1, false);
        CellStyle titleStyle = stylizeLabel(workbook, (byte) 2, (byte) 2, false);
        CellStyle addressStyle = stylizeLabel(workbook, (byte) 3, (byte) 3, false);

        XSSFFont tableHeaderFont = workbook.createFont();
        XSSFFont textFont = workbook.createFont();

        tableHeaderFont.setBold(true);
        tableHeaderFont.setFontHeight(12);

        textFont.setFontHeight(12);

        tableStyle.setFont(tableHeaderFont);
        textStyle.setFont(tableHeaderFont);
        reportHeaderRowStyle.setFont(textFont);

        createOrderCell(reportHeaderRow, ZERO_COLUMN_INDEX, "Отчет о движении продуктов", reportHeaderRowStyle);
        createOrderCell(periodRow, ZERO_COLUMN_INDEX, "Период: " + startDate + " - " + endDate, periodStyle);
        createOrderCell(titleRow, ZERO_COLUMN_INDEX, "Складской учёт", titleStyle);
        createOrderCell(addressRow, ZERO_COLUMN_INDEX, "Адрес: Гомель, ул. Ильча 2", addressStyle);

        createOrderCell(fieldsRow, ORDER_ID_COLUMN_INDEX, "O/N", tableStyle);
        createOrderCell(fieldsRow, SUPPLIER_COLUMN_INDEX, "Supplier", tableStyle);
        createOrderCell(fieldsRow, ACCOUNT_COLUMN_INDEX, "Account", tableStyle);
        createOrderCell(fieldsRow, STORE_COLUMN_INDEX, "Store", tableStyle);
        createOrderCell(fieldsRow, ORDER_TYPE_COLUMN_INDEX, "Order type", tableStyle);
        createOrderCell(fieldsRow, DATE_COLUMN_INDEX, "Date", tableStyle);
        createOrderCell(fieldsRow, AMOUNT_COLUMN_INDEX, "Amount", tableStyle);
        createOrderCell(fieldsRow, PRODUCTS_COLUMN_INDEX, "Products", tableStyle);
    }

    private CellStyle stylizeWorkbook(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private CellStyle stylizeTextInfo(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private CellStyle stylizeLabel(Workbook workbook, byte startRow, byte endRow, boolean isLabel) {
        CellStyle style = workbook.createCellStyle();

        if (isLabel) {
            style.setAlignment(HorizontalAlignment.CENTER);
        }

        style.setVerticalAlignment(VerticalAlignment.CENTER);

        sheet.addMergedRegion(new CellRangeAddress(
                startRow, // начальная строка
                endRow, // конечная строка
                0, // начальная колонка
                7  // конечная колонка
        ));

        return style;
    }

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

            CellStyle multiLineCellStyle = stylizeWorkbook(cell.getSheet().getWorkbook());

            multiLineCellStyle.setWrapText(true);
            cell.setCellStyle(multiLineCellStyle);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
    }

    private void writeExcelOrdersByDateRange(XSSFWorkbook workbook, LocalDate startDate, LocalDate endDate) {
        List<OrderResponse> orderResponses = orderQueryService.findOrdersByDateRange(startDate, endDate);

        writeOrderExcel(workbook, orderResponses);
    }

    private void writeExcelOrdersBySupplier(XSSFWorkbook workbook, LocalDate startDate, LocalDate endDate, Long supplierId) {
        List<OrderResponse> orderResponses = orderQueryService.findOrderByTypeAndSupplier(supplierId, startDate, endDate);

        writeOrderExcel(workbook, orderResponses);
    }

    private void writeOrderExcel(XSSFWorkbook workbook, List<OrderResponse> orderResponses) {
        int rowCount = 6;

        CellStyle style = stylizeWorkbook(workbook);
        XSSFFont font = workbook.createFont();

        font.setFontHeight(11);
        style.setFont(font);

        sheet.getPrintSetup().setLandscape(true);

        for (OrderResponse orderResponse : orderResponses) {
            Row row = sheet.createRow(rowCount++);

            createOrderCell(row, ORDER_ID_COLUMN_INDEX, orderResponse.getOrderId(), style);
            createOrderCell(row, SUPPLIER_COLUMN_INDEX, orderResponse.getSupplierTitle(), style);
            createOrderCell(row, ACCOUNT_COLUMN_INDEX, orderResponse.getUsername(), style);
            createOrderCell(row, STORE_COLUMN_INDEX, orderResponse.getStoreName(), style);
            createOrderCell(row, ORDER_TYPE_COLUMN_INDEX, orderResponse.getOrderType(), style);
            createOrderCell(row, DATE_COLUMN_INDEX, orderResponse.getOrderDate(), style);
            createOrderCell(row, AMOUNT_COLUMN_INDEX, orderResponse.getAmount(), style);
            createOrderCell(row, PRODUCTS_COLUMN_INDEX, orderResponse.getOrderCompositionResponses(), style);

            sheet.setColumnWidth(PRODUCTS_COLUMN_INDEX, PRODUCTS_COLUMN_WIDTH);
        }

        Row authorRow = sheet.createRow(rowCount + 2);
        CellStyle authorStyle = stylizeLabel(workbook, (byte) (rowCount + 2), (byte) (rowCount + 2), false);

        XSSFFont textFont = workbook.createFont();

        textFont.setFontHeight(12);

        authorStyle.setFont(textFont);

        createOrderCell(authorRow, 0, "Составил___________________", authorStyle);
    }

    @Override
    public void generateExcelByDateRange(HttpServletResponse response, LocalDate startDate, LocalDate endDate) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        writeOrderHeader(workbook, startDate, endDate);
        writeExcelOrdersByDateRange(workbook, startDate, endDate);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=example.xlsx");

        ServletOutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }

    @Override
    public void generateExcelBySupplierId(HttpServletResponse response, LocalDate startDate, LocalDate endDate, Long supplierId) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        writeOrderHeader(workbook, startDate, endDate);
        writeExcelOrdersBySupplier(workbook, startDate, endDate, supplierId);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=example.xlsx");

        ServletOutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}