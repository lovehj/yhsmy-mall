package com.yhsmy.utils.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/**
 * @auth 李正义
 * @date 2020/1/27 14:16
 **/
public class ExcelUtils {

    private ExcelUtils () {
    }

    /**
     * excel导出
     *
     * @param headers  excel数据列，每列的标题
     * @param dataList excel数据列，每列的数据
     * @param title    标题
     * @param xlsCtype 0=xls(excel-2003) >0=xlsx(excel-2007+)
     * @return excel workbook， 有可能为null
     */
    public static Workbook exprotExcel (String[] headers, List<List<Object>> dataList, String title, int xlsCtype) {
        if (headers == null || headers.length <= 0 || dataList.isEmpty () || dataList.size () <= 0) {
            return null;
        }

        Workbook wb = new HSSFWorkbook ();
        if (xlsCtype != 0) { // excel 2003
            wb = new XSSFWorkbook (); // excel 2007
        }

        Sheet sheet = wb.createSheet ();
        sheet.setDefaultRowHeight ((short) 500);

        // 设置第一行表头
        int size = dataList.size (), dataSize = dataList.get (0).size ();
        sheet.addMergedRegion (new CellRangeAddress (0, 0, 0, dataSize - 1));
        Row row = sheet.createRow (0);
        Cell cell = row.createCell (0);
        setCellStyle (wb, cell, HSSFColor.HSSFColorPredefined.DARK_TEAL.getIndex ());
        row.setHeight ((short) 500);
        cell.setCellValue (title);

        // 设置第二行表头数据
        row = sheet.createRow (1);
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell (i);
            cell.setCellValue (headers[i]);
            setCellStyle (wb, cell, (short) -1);
            sheet.setColumnWidth (i, 4800);
        }

        // 设置数据
        for (int i = 0; i < size; i++) {
            List<Object> datas = dataList.get (i);
            row = sheet.createRow (2 + i);
            int dsize = datas.size ();
            for (int j = 0; j < dsize; j++) {
                row.createCell (j).setCellValue (String.valueOf (datas.get (j)));
            }
        }
        return wb;
    }

    /**
     * excel导出
     *
     * @param headers  excel数据列，每列的标题
     * @param dataList excel数据列，每列的数据
     * @param title    标题
     * @return excel workbook， 有可能为null
     */
    public static Workbook exprotExcel (String[] headers, List<List<Object>> dataList, String title) {
        return ExcelUtils.exprotExcel (headers, dataList, title, 0);
    }

    private static void setCellStyle (Workbook wb, Cell cell, short colorIdx) {
        CellStyle cs = wb.createCellStyle ();
        cs.setAlignment (HorizontalAlignment.CENTER);
        if (colorIdx > -1) {
//            cs.setFillBackgroundColor (colorIdx);
//            cs.setFillPattern (FillPatternType.SOLID_FOREGROUND);
        }
        Font font = wb.createFont ();
        font.setFontName ("宋体");
        cs.setFont (font);
        cell.setCellStyle (cs);
    }



}
