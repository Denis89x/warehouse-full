package dev.lebenkov.warehouse.api.service.excel;

import dev.lebenkov.warehouse.api.service.ProductQueryService;
import dev.lebenkov.warehouse.storage.dto.ProductResponse;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xddf.usermodel.chart.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductChartExcelImpl implements ProductChartExcel {

    private final WorkbookValidation workbookValidation;
    private final ProductQueryService productQueryService;
    private final StylizeExcelService stylizeExcelService;
    private final DateRangeCreationService dateRangeCreationService;
    private final HeaderCreationService headerCreationService;

    @Override
    public void generateProductChartExcel(HttpServletResponse response, LocalDate startDate, LocalDate endDate) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        writeProductChartExcel(workbook, startDate, endDate);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=example.xlsx");

        ServletOutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }

    private void writeProductChartExcel(XSSFWorkbook workbook, LocalDate startDate, LocalDate endDate) {
        List<ProductResponse> productResponses = findProductResponsesByDateRange(startDate, endDate);

        createExcelHeader(workbook, startDate, endDate);

        Map<String, Integer> productNameAndPresenceMap = productResponses.stream()
                .collect(Collectors.toMap(
                        ProductResponse::getTitle,
                        ProductResponse::getPresence,
                        (existingValue, newValue) -> existingValue));

        // Создание графика
        XSSFSheet sheet = workbook.getSheet("Product Chart"); // Получаем лист по имени
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 4, 8, 20);
        XSSFChart chart = drawing.createChart(anchor);

        generateChartData(chart, productNameAndPresenceMap);
    }

    private void generateChartData(XSSFChart chart, Map<String, Integer> productNameAndPresenceMap) {
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);

        XDDFDataSource<String> categories = XDDFDataSourcesFactory.fromArray(productNameAndPresenceMap.keySet().toArray(new String[0]));
        XDDFNumericalDataSource<Integer> values = XDDFDataSourcesFactory.fromArray(productNameAndPresenceMap.values().toArray(new Integer[0]));
        XDDFLineChartData chartData = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
        chartData.addSeries(categories, values);
        chart.plot(chartData);
    }

    private List<ProductResponse> findProductResponsesByDateRange(LocalDate startDate, LocalDate endDate) {
        return productQueryService.findProductsByDateRange(startDate, endDate);
    }

    private void createExcelHeader(XSSFWorkbook workbook, LocalDate startDate, LocalDate endDate) {
        workbookValidation.checkWorkbookWithSameName(workbook, "Product Chart");

        XSSFSheet sheet = workbook.createSheet("Product Chart");

        CellStyle dateStyle = stylizeExcelService.stylizeDateRow(workbook, (byte) 2, (byte) 2, (byte) 0, (byte) 6, sheet);
        CellStyle titleStyle = stylizeExcelService.stylizeTitleRow(workbook, (byte) 0, (byte) 0, (byte) 0, (byte) 6, sheet);

        dateRangeCreationService.createDateRangeRow(startDate, endDate, dateStyle, sheet, 2);
        headerCreationService.createHeaderRow(titleStyle, 0, sheet, "Продукты");
    }
}