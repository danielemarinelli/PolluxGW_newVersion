package core;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class excelUserData {

    public static List<Map<String, String>> getDataFromExcelFile() throws IOException {
        List<Map<String,String>> testDataAllRows=null;
        Map<String,String> testData=null;
        FileInputStream fileInputStream=new FileInputStream("C:\\TEST\\dataExcel\\Data_File.xlsx");
        Workbook workbook=new XSSFWorkbook(fileInputStream);
        Sheet sheet=workbook.getSheetAt(0);
        int lastRowNumber=sheet.getLastRowNum();
        int lastColNumber=sheet.getRow(0).getLastCellNum();
        List list=new ArrayList();
        for(int i=0; i<lastColNumber;i++){
            Row row=sheet.getRow(0);
            Cell cell=row.getCell(i);
            String rowHeader = cell.getStringCellValue().trim();
            list.add(rowHeader);
        }
        testDataAllRows=new ArrayList<Map<String,String>>();
        for (int j=1;j<=lastRowNumber;j++){
            Row row=sheet.getRow(j);
            testData=new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
            for(int k=0;k<lastColNumber;k++){
                Cell cell=row.getCell(k);
                String colValue = getCellDataAsString((XSSFCell) cell.getRow().getCell(k));
                testData.put((String) list.get(k),colValue);
            }
            testDataAllRows.add(testData);
        }
        return testDataAllRows;  //TESTDATA FOR ALL ROWS
    }

    public static List<Map<String, String>> getInfoFromEmailSheet() throws IOException {
        List<Map<String,String>> emailInfo=null;
        Map<String,String> testData=null;
        FileInputStream fileInputStream=new FileInputStream("C:\\TEST\\dataExcel\\Email_Info_Sheet.xlsx");
        Workbook workbook=new XSSFWorkbook(fileInputStream);
        Sheet sheet=workbook.getSheetAt(0);
        int lastRowNumber=sheet.getLastRowNum();
        int lastColNumber=sheet.getRow(0).getLastCellNum();
        List list=new ArrayList();
        for(int i=0; i<lastColNumber;i++){
            Row row=sheet.getRow(0);
            Cell cell=row.getCell(i);
            String rowHeader = cell.getStringCellValue().trim();
            list.add(rowHeader);
        }
        emailInfo=new ArrayList<Map<String,String>>();
        for (int j=1;j<=lastRowNumber;j++){
            Row row=sheet.getRow(j);
            testData=new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
            for(int k=0;k<lastColNumber;k++){
                Cell cell=row.getCell(k);
                String colValue = getCellDataAsString((XSSFCell) cell.getRow().getCell(k));
                if(colValue.contains(".0")){
                    colValue = colValue.replace(".0","");
                }
                testData.put((String) list.get(k),colValue);
            }
            emailInfo.add(testData);
        }
        return emailInfo;
    }

    public static List<Map<String, String>> getFoldersNamesFromExcelSheet() throws IOException {
        List<Map<String,String>> FoldersInfo=null;
        Map<String,String> testData=null;
        FileInputStream fileInputStream=new FileInputStream("C:\\TEST\\dataExcel\\Files_Folders.xlsx");
        Workbook workbook=new XSSFWorkbook(fileInputStream);
        Sheet sheet=workbook.getSheetAt(0);
        int lastRowNumber=sheet.getLastRowNum();
        int lastColNumber=sheet.getRow(0).getLastCellNum();
        List list=new ArrayList();
        for(int i=0; i<lastColNumber;i++){
            Row row=sheet.getRow(0);
            Cell cell=row.getCell(i);
            String rowHeader = cell.getStringCellValue().trim();
            list.add(rowHeader);
        }
        FoldersInfo=new ArrayList<Map<String,String>>();
        for (int j=1;j<=lastRowNumber;j++){
            Row row=sheet.getRow(j);
            testData=new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
            for(int k=0;k<lastColNumber;k++){
                Cell cell=row.getCell(k);
                String colValue = getCellDataAsString((XSSFCell) cell.getRow().getCell(k));
                testData.put((String) list.get(k),colValue);
            }
            FoldersInfo.add(testData);
        }
        return FoldersInfo;  //TESTDATA FOR ALL ROWS
    }

    public static List<Map<String, String>> getPolluxGWDataFromFile() throws IOException {
        List<Map<String,String>> FoldersInfo=null;
        Map<String,String> testData=null;
        FileInputStream fileInputStream=new FileInputStream("C:\\TEST\\dataExcel\\PolluxGW_File.xlsx");
        Workbook workbook=new XSSFWorkbook(fileInputStream);
        Sheet sheet=workbook.getSheetAt(0);
        int lastRowNumber=sheet.getLastRowNum();
        int lastColNumber=sheet.getRow(0).getLastCellNum();
        List list=new ArrayList();
        for(int i=0; i<lastColNumber;i++){
            Row row=sheet.getRow(0);
            Cell cell=row.getCell(i);
            String rowHeader = cell.getStringCellValue().trim();
            list.add(rowHeader);
        }
        FoldersInfo=new ArrayList<Map<String,String>>();
        for (int j=1;j<=lastRowNumber;j++){
            Row row=sheet.getRow(j);
            testData=new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
            for(int k=0;k<lastColNumber;k++){
                Cell cell=row.getCell(k);
                String colValue = getCellDataAsString((XSSFCell) cell.getRow().getCell(k));
                testData.put((String) list.get(k),colValue);
            }
            FoldersInfo.add(testData);
        }
        return FoldersInfo;
    }

    public static List<Map<String, String>> getAppFoldersFromFile() throws IOException {
        List<Map<String,String>> FoldersInfo=null;
        Map<String,String> testData=null;
        FileInputStream fileInputStream=new FileInputStream("C:\\TEST\\dataExcel\\AppsInAdminMode.xlsx");
        Workbook workbook=new XSSFWorkbook(fileInputStream);
        Sheet sheet=workbook.getSheetAt(0);
        int lastRowNumber=sheet.getLastRowNum();
        int lastColNumber=sheet.getRow(0).getLastCellNum();
        List list=new ArrayList();
        for(int i=0; i<lastColNumber;i++){
            Row row=sheet.getRow(0);
            Cell cell=row.getCell(i);
            String rowHeader = cell.getStringCellValue().trim();
            list.add(rowHeader);
        }
        FoldersInfo=new ArrayList<Map<String,String>>();
        for (int j=1;j<=lastRowNumber;j++){
            Row row=sheet.getRow(j);
            testData=new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
            for(int k=0;k<lastColNumber;k++){
                Cell cell=row.getCell(k);
                String colValue = getCellDataAsString((XSSFCell) cell.getRow().getCell(k));
                testData.put((String) list.get(k),colValue);
            }
            FoldersInfo.add(testData);
        }
        return FoldersInfo;
    }



    private static String getCellDataAsString(XSSFCell cell) {
        String value = "";
        if (cell != null) {
            CellType cellType = cell.getCellType();
            switch (cellType) {
                case BLANK:
                    value = "";
                    break;
                case BOOLEAN:
                    value = (cell.getBooleanCellValue()) ? "true" : "false";
                    break;
                case ERROR:
                    value = cell.getErrorCellString();
                    break;
                case FORMULA:
                    value = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    value = Double.toString(cell.getNumericCellValue());
                    break;
                case STRING:
                default:
                    value = cell.getStringCellValue();
            }
        }
        return value.trim();
    }


}
