package dev.lebenkov.warehouse.api.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StylizeExcelServiceImpl implements StylizeExcelService {

    @Override
    public CellStyle stylizeWorkbook(XSSFWorkbook workbook, boolean isHeader) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        if (isHeader) {
            XSSFFont tableHeaderFont = workbook.createFont();

            tableHeaderFont.setBold(true);
            tableHeaderFont.setFontHeight(13);

            style.setFont(tableHeaderFont);
        }

        return style;
    }

    @Override
    public CellStyle stylizeLabel(Workbook workbook, byte startRow, byte endRow, byte startColumn, byte endColumn, boolean isLabel, XSSFSheet sheet) {
        CellStyle style = workbook.createCellStyle();

        if (isLabel) {
            style.setAlignment(HorizontalAlignment.CENTER);
        }

        style.setVerticalAlignment(VerticalAlignment.CENTER);

        sheet.addMergedRegion(new CellRangeAddress(
                startRow, // начальная строка
                endRow, // конечная строка
                startColumn, // начальная колонка
                endColumn  // конечная колонка
        ));

        return style;
    }

    @Override
    public CellStyle stylizeDateRow(Workbook workbook, byte startRow, byte endRow, byte startColumn, byte endColumn, XSSFSheet sheet) {
        return stylizeLabel(workbook, startRow, endRow, startColumn, endColumn, false, sheet);
    }

    @Override
    public CellStyle stylizeTitleRow(XSSFWorkbook workbook, byte startRow, byte endRow, byte startColumn, byte endColumn, XSSFSheet sheet) {
        CellStyle cellStyle = stylizeLabel(workbook, startRow, endRow, startColumn, endColumn, true, sheet);
        XSSFFont titleFont = workbook.createFont();

        titleFont.setBold(true);
        titleFont.setFontHeight(13);

        cellStyle.setFont(titleFont);

        return cellStyle;
    }

    @Override
    public CellStyle stylizeTextInfo(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }
}
