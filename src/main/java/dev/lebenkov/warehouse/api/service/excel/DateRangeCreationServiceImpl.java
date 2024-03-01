package dev.lebenkov.warehouse.api.service.excel;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DateRangeCreationServiceImpl implements DateRangeCreationService {

    CellCreationService cellCreationService;

    @Override
    public void createDateRangeRow(LocalDate startDate, LocalDate endDate, CellStyle dateStyle, XSSFSheet sheet, Integer rowIndex) {
        Row dateRow = sheet.createRow(rowIndex);
        cellCreationService.createCell(dateRow, 0, "Период: " + startDate + " - " + endDate, dateStyle, sheet);
    }
}
