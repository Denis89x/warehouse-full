package dev.lebenkov.warehouse.api.service;

import dev.lebenkov.warehouse.storage.dto.ProductTypeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CellCreationServiceImpl implements CellCreationService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final StylizeExcelService stylizeExcelService;

    @Override
    public void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style, XSSFSheet sheet) {
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
    public void createTableDataRow(Object[] titleRows, Integer rowCount, Integer[] columnIndexes, CellStyle tableStyle, XSSFSheet sheet) {
        Row row = sheet.createRow(rowCount);
        for (int i = 0; i < columnIndexes.length; i++) {
            createCell(row, columnIndexes[i], titleRows[i], tableStyle, sheet);
        }
    }
}
