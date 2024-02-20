package dev.lebenkov.warehouse.api.service;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public interface StylizeExcelService {
    CellStyle stylizeWorkbook(Workbook workbook);
}
