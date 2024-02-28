package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.OrderResponse;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderExcelServiceImpl implements OrderExcelService {

    private final OrderQueryService orderQueryService;
    private final OrderCRUDService orderCRUDService;
    private final StylizeExcelService stylizeExcelService;
    private final ExcelFontService excelFontService;
    private final OrderExcelInfoCreationService orderExcelInfoCreationService;
    private final CellCreationService cellCreationService;
    private final FooterCreationService footerCreationService;
    private final HeaderCreationService headerCreationService;

    private XSSFSheet sheet;

    private static final Integer[] COLUMN_INDEXES = {0, 1, 2, 3, 4, 5, 6, 7};

    private static final int PRODUCT_COLUMN_INDEX = 7;
    private static final int PRODUCTS_COLUMN_WIDTH = 10000;

    private void createHeaderTitles(XSSFWorkbook workbook, String[] headerTitles) {
        headerCreationService.createExcelHeaderInfo(workbook, headerTitles, sheet);
    }

    private void createTableHeaderRow(CellStyle tableHeaderStyle) {
        String[] tableTitles = {"O/N", "Поставщик", "Аккаунт", "Склад", "Тип заказа", "Дата", "Сумма", "Продукты"};
        headerCreationService.createTableHeaderRow(tableHeaderStyle, 5, COLUMN_INDEXES, tableTitles, sheet);
    }

    private void writeOrderHeader(XSSFWorkbook workbook, LocalDate startDate, LocalDate endDate) {
        checkWorkbookWithSameName(workbook);

        String[] headerTitles = {"Отчёт о движении продуктов", "Период: " + startDate + " - " + endDate, "Складской учёт", "Адрес: Гомель, ул. Ильича 2"};

        sheet = workbook.createSheet("Order");

        CellStyle tableHeaderStyle = stylizeExcelService.stylizeWorkbook(workbook, true);

        createHeaderTitles(workbook, headerTitles);

        createTableHeaderRow(tableHeaderStyle);
    }

    private void writeExcelOrdersByDateRange(XSSFWorkbook workbook, LocalDate startDate, LocalDate endDate) {
        List<OrderResponse> orderResponses = orderQueryService.findOrdersByDateRange(startDate, endDate);

        writeOrderDataExcel(workbook, orderResponses);
    }

    private void writeExcelOrdersBySupplier(XSSFWorkbook workbook, LocalDate startDate, LocalDate endDate, Long supplierId) {
        List<OrderResponse> orderResponses = orderQueryService.findOrderByTypeAndSupplier(supplierId, startDate, endDate);

        writeOrderDataExcel(workbook, orderResponses);
    }

    private void writeOrderDataExcel(XSSFWorkbook workbook, List<OrderResponse> orderResponses) {
        int rowCount = 6;

        sheet.getPrintSetup().setLandscape(true);

        CellStyle tableStyle = stylizeExcelService.stylizeWorkbook(workbook, false);

        XSSFFont textFont = excelFontService.createTextFont(workbook);

        tableStyle.setFont(textFont);

        for (OrderResponse orderResponse : orderResponses) {
            Object[] titleRows = {orderResponse.getOrderId(), orderResponse.getSupplierTitle(), orderResponse.getUsername(),
                    orderResponse.getStoreName(), orderResponse.getOrderType(), orderResponse.getOrderDate(),
                    orderResponse.getAmount(), orderResponse.getOrderCompositionResponses()};

            sheet.setColumnWidth(PRODUCT_COLUMN_INDEX, PRODUCTS_COLUMN_WIDTH);

            cellCreationService.createTableDataRow(titleRows, rowCount++, COLUMN_INDEXES, tableStyle, sheet);
        }

        footerCreationService.createAuthorFooter(workbook, rowCount, textFont, sheet);
    }

    private void writeExcel(XSSFWorkbook workbook, Long orderId) {
        checkWorkbookWithSameName(workbook);

        sheet = workbook.createSheet("Order");

        OrderResponse orderResponse = orderCRUDService.fetchOrder(orderId);

        Integer[] rowIndexes = {0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12};
        String[] columnTitles = {"Информация о заказе", "Складской учёт", "Склад: " + orderResponse.getStoreName(),
                "Адрес: Гомель, ул. Ильича, 2", "Номер: " + orderResponse.getOrderId(), "Сотрудник: " + orderResponse.getUsername(),
                "Сумма: " + orderResponse.getAmount(), "Дата: " + orderResponse.getOrderDate(), "Тип: " + orderResponse.getOrderType(),
                "Продукты: " + orderResponse.getOrderCompositionResponses(), "Поставищик: " + orderResponse.getSupplierTitle(), "Адрес: ул. Хрензнаеткакая 10"};

        orderExcelInfoCreationService.createOrderExcelInfo(workbook, rowIndexes, columnTitles, sheet);
    }

    @Override
    public void generateExcelByDateRange(HttpServletResponse response, LocalDate startDate, LocalDate endDate) throws IOException {
        generateExcel(response, startDate, endDate, null, null);
    }

    @Override
    public void generateExcelBySupplierId(HttpServletResponse response, LocalDate startDate, LocalDate endDate, Long supplierId) throws IOException {
        generateExcel(response, startDate, endDate, supplierId, null);
    }

    @Override
    public void generateOrderExcel(HttpServletResponse response, Long orderId) throws IOException {
        generateExcel(response, null, null, null, orderId);
    }

    private void checkWorkbookWithSameName(XSSFWorkbook workbook) {
        int index = workbook.getSheetIndex("Order");

        if (index != -1) {
            workbook.removeSheetAt(index);
        }
    }

    private void generateExcel(HttpServletResponse response, LocalDate startDate, LocalDate endDate, Long supplierId, Long orderId) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        if (supplierId != null) {
            writeOrderHeader(workbook, startDate, endDate);
            writeExcelOrdersBySupplier(workbook, startDate, endDate, supplierId);
        } else if (orderId != null) {
            writeExcel(workbook, orderId);
        } else {
            writeOrderHeader(workbook, startDate, endDate);
            writeExcelOrdersByDateRange(workbook, startDate, endDate);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=example.xlsx");

        ServletOutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}