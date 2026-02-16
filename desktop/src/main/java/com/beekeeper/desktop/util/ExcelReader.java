package com.beekeeper.desktop.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;

/**
 * Utility na prečítanie Excel súboru.
 * Pomocný nástroj na analýzu záložky "Rátanie dátumov".
 */
public class ExcelReader {
    public static void main(String[] args) throws Exception {
        String filename = "Úľový denník 2025.xlsx";
        if (args.length > 0) {
            filename = args[0];
        }

        FileInputStream fis = new FileInputStream(filename);
        Workbook workbook = new XSSFWorkbook(fis);

        System.out.println("=== Dostupné záložky ===");
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            System.out.println("  " + i + ": " + workbook.getSheetName(i));
        }
        System.out.println();

        // Find "Rátanie dátumov" sheet
        Sheet sheet = null;
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            String name = workbook.getSheetName(i);
            if (name.toLowerCase().contains("rát") || name.toLowerCase().contains("rat") ||
                name.toLowerCase().contains("dátum") || name.toLowerCase().contains("datum")) {
                sheet = workbook.getSheetAt(i);
                System.out.println("=== Záložka: " + name + " ===\n");
                break;
            }
        }

        if (sheet == null) {
            System.out.println("Záložka 'Rátanie dátumov' nebola nájdená!");
            workbook.close();
            fis.close();
            return;
        }

        // Print first 80 rows to see structure
        System.out.println("Obsah (prvých 80 riadkov):\n");
        for (int rowNum = 0; rowNum <= Math.min(80, sheet.getLastRowNum()); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) continue;

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("R%-3d: ", rowNum));

            boolean hasData = false;
            for (int colNum = 0; colNum < 20; colNum++) {
                Cell cell = row.getCell(colNum);
                if (cell != null) {
                    String value = getCellValueAsString(cell);
                    if (!value.trim().isEmpty()) {
                        sb.append("[").append((char)('A' + colNum)).append(":").append(value).append("] ");
                        hasData = true;
                    }
                }
            }

            if (hasData) {
                System.out.println(sb.toString());
            }
        }

        workbook.close();
        fis.close();
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double num = cell.getNumericCellValue();
                    // If it's a whole number, don't show decimals
                    if (num == (long) num) {
                        return String.valueOf((long) num);
                    }
                    return String.valueOf(num);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // Try to get cached value
                try {
                    return "=" + cell.getCellFormula();
                } catch (Exception e) {
                    return "[FORMULA]";
                }
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}
