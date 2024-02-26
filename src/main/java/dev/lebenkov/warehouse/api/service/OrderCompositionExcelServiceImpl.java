package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.model.OrderComposition;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCompositionExcelServiceImpl implements OrderCompositionExcelService {

    private XSSFSheet sheet;

    private final OrderCompositionQueryService orderCompositionQueryService;
    private final StylizeExcelService stylizeExcelService;
    private final ExcelCellService excelCellService;

    private void writeDeliveryNoteExcel(XSSFWorkbook workbook, LocalDate startDate, LocalDate endDate) {
        List<OrderComposition> orderCompositions = getOrderCompositions(startDate, endDate);

        createDeliveryNoteSheet(workbook, startDate, endDate);

        int rowCount = 4;
        int rowId = 1;

        CellStyle tableStyle = stylizeExcelService.stylizeWorkbook(workbook, false);
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

        CellStyle dateStyle = stylizeExcelService.stylizeLabel(workbook, (byte) 2, (byte) 2, (byte) 0, (byte) 4, false, sheet);
        CellStyle titleStyle = stylizeExcelService.stylizeLabel(workbook, (byte) 0, (byte) 0, (byte) 0, (byte) 4, true, sheet);

        CellStyle tableHeaderStyle = stylizeExcelService.stylizeWorkbook(workbook, true);

        XSSFFont titleFont = workbook.createFont();

        titleFont.setBold(true);
        titleFont.setFontHeight(13);

        titleStyle.setFont(titleFont);

        excelCellService.createDateRangeRow(startDate, endDate, dateStyle, sheet, 2);
        createHeaderRow(titleStyle);
        createTableHeaderRow(tableHeaderStyle);
    }

    private void createHeaderRow(CellStyle titleStyle) {
        Row headerRow = sheet.createRow(0);
        excelCellService.createOrderCell(headerRow, 0, "ТОВАРНАЯ НАКЛАДНАЯ", titleStyle, sheet);
    }

    private void createTableHeaderRow(CellStyle tableHeaderStyle) {
        Row tableHeaderRow = sheet.createRow(3);
        excelCellService.createOrderCell(tableHeaderRow, 0, "№", tableHeaderStyle, sheet);
        excelCellService.createOrderCell(tableHeaderRow, 1, "Наименование", tableHeaderStyle, sheet);
        excelCellService.createOrderCell(tableHeaderRow, 2, "Количество", tableHeaderStyle, sheet);
        excelCellService.createOrderCell(tableHeaderRow, 3, "Цена", tableHeaderStyle, sheet);
        excelCellService.createOrderCell(tableHeaderRow, 4, "Сумма", tableHeaderStyle, sheet);
    }

    private void createOrderRow(OrderComposition orderComposition, int rowCount, int rowId, CellStyle tableStyle) {
        Row row = sheet.createRow(rowCount);
        excelCellService.createOrderCell(row, 0, rowId, tableStyle, sheet);
        excelCellService.createOrderCell(row, 1, orderComposition.getProduct().getTitle(), tableStyle, sheet);
        excelCellService.createOrderCell(row, 2, orderComposition.getQuantity(), tableStyle, sheet);
        excelCellService.createOrderCell(row, 3, orderComposition.getProduct().getCost(), tableStyle, sheet);
        excelCellService.createOrderCell(row, 4, (orderComposition.getProduct().getCost() * orderComposition.getQuantity()), tableStyle, sheet);
    }

    private void createAuthorFooter(XSSFWorkbook workbook, int rowCount, XSSFFont textFont) {
        CellStyle authorStyle = stylizeExcelService.stylizeLabel(workbook, (byte) rowCount, (byte) rowCount, (byte) 0, (byte) 4, false, sheet);
        authorStyle.setFont(textFont);

        Row footerRow = sheet.createRow(rowCount + 1);
        excelCellService.createOrderCell(footerRow, 0, "Составил" + "_".repeat(20), authorStyle, sheet);
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