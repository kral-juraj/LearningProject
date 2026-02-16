import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;

public class ReadExcel {
    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("Úľový denník 2025.xlsx");
        Workbook workbook = new XSSFWorkbook(fis);

        System.out.println("Dostupné záložky:");
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            System.out.println("  " + i + ": " + workbook.getSheetName(i));
        }

        // Find "Rátanie dátumov" sheet
        Sheet sheet = null;
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            String name = workbook.getSheetName(i);
            if (name.toLowerCase().contains("rátanie") || name.toLowerCase().contains("ratanie") ||
                name.toLowerCase().contains("datum")) {
                sheet = workbook.getSheetAt(i);
                System.out.println("\n=== Záložka: " + name + " ===\n");
                break;
            }
        }

        if (sheet != null) {
            // Print first 60 rows to see structure
            for (int rowNum = 0; rowNum <= Math.min(60, sheet.getLastRowNum()); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) continue;

                StringBuilder sb = new StringBuilder();
                sb.append("R").append(rowNum).append(": ");

                for (int colNum = 0; colNum < 15; colNum++) {
                    Cell cell = row.getCell(colNum);
                    if (cell != null) {
                        String value = "";
                        switch (cell.getCellType()) {
                            case STRING:
                                value = cell.getStringCellValue();
                                break;
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    value = cell.getDateCellValue().toString();
                                } else {
                                    value = String.valueOf(cell.getNumericCellValue());
                                }
                                break;
                            case BOOLEAN:
                                value = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case FORMULA:
                                value = "=" + cell.getCellFormula();
                                break;
                        }
                        if (!value.trim().isEmpty()) {
                            sb.append("[").append((char)('A' + colNum)).append(":").append(value).append("] ");
                        }
                    }
                }

                String line = sb.toString().trim();
                if (line.length() > 3) {
                    System.out.println(line);
                }
            }
        }

        workbook.close();
        fis.close();
    }
}
