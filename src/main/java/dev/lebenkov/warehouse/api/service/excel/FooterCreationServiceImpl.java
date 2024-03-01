package dev.lebenkov.warehouse.api.service.excel;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FooterCreationServiceImpl implements FooterCreationService {

    StylizeExcelService stylizeExcelService;
    CellCreationService cellCreationService;

    @Override
    public void createAuthorFooter(XSSFWorkbook workbook, int rowCount, XSSFFont textFont, XSSFSheet sheet) {
        CellStyle authorStyle = stylizeExcelService.stylizeLabel(workbook, (byte) rowCount, (byte) rowCount, (byte) 0, (byte) 4, false, sheet);
        authorStyle.setFont(textFont);

        Row footerRow = sheet.createRow(rowCount + 1);
        cellCreationService.createCell(footerRow, 0, "Составил" + "_".repeat(20), authorStyle, sheet);
    }
}
