package dev.lebenkov.warehouse.api.service.excel;

import dev.lebenkov.warehouse.api.service.ProductQueryService;
import dev.lebenkov.warehouse.storage.dto.ProductResponse;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
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
public class ProductExcelServiceImpl implements ProductExcelService {

    private XSSFSheet sheet;

    private final StylizeExcelService stylizeExcelService;
    private final ProductQueryService productQueryService;
    private final HeaderCreationService headerCreationService;
    private final FooterCreationService footerCreationService;
    private final CellCreationService cellCreationService;
    private final DateRangeCreationService dateRangeCreationService;
    private final ExcelFontService excelFontService;

    private static final Integer[] COLUMN_INDEXES = {0, 1, 2, 3, 4, 5, 6};

    private void writeProductExcel(XSSFWorkbook workbook, LocalDate startDate, LocalDate endDate) {
        List<ProductResponse> productResponses = findProductResponsesByDateRange(startDate, endDate);

        createProductSheet(workbook, startDate, endDate);

        int rowCount = 4;

        CellStyle tableStyle = stylizeExcelService.stylizeWorkbook(workbook, false);

        XSSFFont textFont = excelFontService.createTextFont(workbook);

        tableStyle.setFont(textFont);

        for (ProductResponse productResponse : productResponses) {
            Object[] titleRows = {productResponse.getProductId(), productResponse.getTitle(), productResponse.getDate(),
                    productResponse.getPresence(), productResponse.getCost(), productResponse.getDescription(), productResponse.getProductTypeResponse()};
            cellCreationService.createTableDataRow(titleRows, rowCount++, COLUMN_INDEXES, tableStyle, sheet);
        }

        footerCreationService.createAuthorFooter(workbook, rowCount, textFont, sheet);
    }

    private void createProductSheet(XSSFWorkbook workbook, LocalDate startDate, LocalDate endDate) {
        int index = workbook.getSheetIndex("Product");
        if (index != -1) {
            workbook.removeSheetAt(index);
        }
        sheet = workbook.createSheet("Product");

        CellStyle dateStyle = stylizeExcelService.stylizeDateRow(workbook, (byte) 2, (byte) 2, (byte) 0, (byte) 6, sheet);
        CellStyle titleStyle = stylizeExcelService.stylizeTitleRow(workbook, (byte) 0, (byte) 0, (byte) 0, (byte) 6, sheet);

        CellStyle tableHeaderStyle = stylizeExcelService.stylizeWorkbook(workbook, true);

        dateRangeCreationService.createDateRangeRow(startDate, endDate, dateStyle, sheet, 2);
        headerCreationService.createHeaderRow(titleStyle, 0, sheet, "Наличие продуктов");
        createTableHeaderRow(tableHeaderStyle);
    }

    private void createTableHeaderRow(CellStyle tableHeaderStyle) {
        String[] titleColumns = {"Номер", "Название", "Дата", "Наличие", "Цена", "Описание", "Тип заказа"};
        headerCreationService.createTableHeaderRow(tableHeaderStyle, 3, COLUMN_INDEXES, titleColumns, sheet);
    }

    private List<ProductResponse> findProductResponsesByDateRange(LocalDate startDate, LocalDate endDate) {
        return productQueryService.findProductsByDateRange(startDate, endDate);
    }

    @Override
    public void generateProductBalanceExcel(HttpServletResponse response, LocalDate startDate, LocalDate endDate) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        writeProductExcel(workbook, startDate, endDate);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=example.xlsx");

        ServletOutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}