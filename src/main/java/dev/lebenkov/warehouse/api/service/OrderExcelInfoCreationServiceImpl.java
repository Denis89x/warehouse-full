package dev.lebenkov.warehouse.api.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderExcelInfoCreationServiceImpl implements OrderExcelInfoCreationService {

    StylizeExcelService stylizeExcelService;
    CellCreationService cellCreationService;

    @Override
    public void createOrderExcelInfo(XSSFWorkbook workbook, Integer[] rowIndexes, String[] columnTitles, XSSFSheet sheet) {
        CellStyle style = stylizeExcelService.stylizeLabel(workbook, (byte) 0, (byte) 0, (byte) 0, (byte) 7, false, sheet);

        for (int i = 0; i < rowIndexes.length; i++) {
            Row row = sheet.createRow(rowIndexes[i]);

            cellCreationService.createCell(row, 0, columnTitles[i], style, sheet);
        }
    }
}
