package dev.lebenkov.warehouse.api.service.excel;

import dev.lebenkov.warehouse.api.service.OrderCompositionQueryService;
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
    private final CellCreationService cellCreationService;
    private final DateRangeCreationService dateRangeCreationService;
    private final FooterCreationService footerCreationService;
    private final WorkbookValidation workbookValidation;

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

        footerCreationService.createAuthorFooter(workbook, rowCount, textFont, sheet);
    }

    private List<OrderComposition> getOrderCompositions(LocalDate startDate, LocalDate endDate) {
        return orderCompositionQueryService.findOrderCompositionAndDateBetween(startDate, endDate);
    }

    private void createDeliveryNoteSheet(XSSFWorkbook workbook, LocalDate startDate, LocalDate endDate) {
        workbookValidation.checkWorkbookWithSameName(workbook, "Delivery Note");

        sheet = workbook.createSheet("Delivery Note");

        CellStyle dateStyle = stylizeExcelService.stylizeLabel(workbook, (byte) 2, (byte) 2, (byte) 0, (byte) 4, false, sheet);
        CellStyle titleStyle = stylizeExcelService.stylizeLabel(workbook, (byte) 0, (byte) 0, (byte) 0, (byte) 4, true, sheet);

        CellStyle tableHeaderStyle = stylizeExcelService.stylizeWorkbook(workbook, true);

        XSSFFont titleFont = workbook.createFont();

        titleFont.setBold(true);
        titleFont.setFontHeight(13);

        titleStyle.setFont(titleFont);

        dateRangeCreationService.createDateRangeRow(startDate, endDate, dateStyle, sheet, 2);
        createHeaderRow(titleStyle);
        createTableHeaderRow(tableHeaderStyle);
    }

    private void createHeaderRow(CellStyle titleStyle) {
        Row headerRow = sheet.createRow(0);
        cellCreationService.createCell(headerRow, 0, "ТОВАРНАЯ НАКЛАДНАЯ", titleStyle, sheet);
    }

    private void createTableHeaderRow(CellStyle tableHeaderStyle) {
        Row tableHeaderRow = sheet.createRow(3);
        cellCreationService.createCell(tableHeaderRow, 0, "№", tableHeaderStyle, sheet);
        cellCreationService.createCell(tableHeaderRow, 1, "Наименование", tableHeaderStyle, sheet);
        cellCreationService.createCell(tableHeaderRow, 2, "Количество", tableHeaderStyle, sheet);
        cellCreationService.createCell(tableHeaderRow, 3, "Цена", tableHeaderStyle, sheet);
        cellCreationService.createCell(tableHeaderRow, 4, "Сумма", tableHeaderStyle, sheet);
    }

    private void createOrderRow(OrderComposition orderComposition, int rowCount, int rowId, CellStyle tableStyle) {
        Row row = sheet.createRow(rowCount);
        cellCreationService.createCell(row, 0, rowId, tableStyle, sheet);
        cellCreationService.createCell(row, 1, orderComposition.getProduct().getTitle(), tableStyle, sheet);
        cellCreationService.createCell(row, 2, orderComposition.getQuantity(), tableStyle, sheet);
        cellCreationService.createCell(row, 3, orderComposition.getProduct().getCost(), tableStyle, sheet);
        cellCreationService.createCell(row, 4, (orderComposition.getProduct().getCost() * orderComposition.getQuantity()), tableStyle, sheet);
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