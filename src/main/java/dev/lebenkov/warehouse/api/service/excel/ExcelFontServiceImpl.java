package dev.lebenkov.warehouse.api.service.excel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelFontServiceImpl implements ExcelFontService {

    @Override
    public XSSFFont createTextFont(XSSFWorkbook workbook) {
        XSSFFont textFont = workbook.createFont();
        textFont.setFontHeight(12);
        return textFont;
    }
}
