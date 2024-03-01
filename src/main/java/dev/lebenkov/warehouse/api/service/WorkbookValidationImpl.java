package dev.lebenkov.warehouse.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkbookValidationImpl implements WorkbookValidation {
    @Override
    public void checkWorkbookWithSameName(XSSFWorkbook workbook, String sheetIndex) {
        int index = workbook.getSheetIndex(sheetIndex);

        if (index != -1) {
            workbook.removeSheetAt(index);
        }
    }
}
