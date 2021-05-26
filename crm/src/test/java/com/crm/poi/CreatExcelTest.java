package com.crm.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * 罗健
 * 2021/5/6
 */
public class CreatExcelTest {
    public static void main(String[] args) throws Exception {
        //工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        //工作表
        HSSFSheet sheet = wb.createSheet("学生列表");
        //行,从0开始
        HSSFRow row = sheet.createRow(0);
        //单元格
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("学号");

        cell = row.createCell(1);
        cell.setCellValue("姓名");

        cell = row.createCell(2);
        cell.setCellValue("年龄");

        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        //从第二行开始赋值
        for (int i = 1; i < 10; i++) {
            row = sheet.createRow(i);

            cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue(i);

            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue("Tom" + i);

            cell = row.createCell(2);
            cell.setCellStyle(style);
            cell.setCellValue(20 + i);
        }

        //输出对象
        OutputStream os = new FileOutputStream("d:/student.xls");
        wb.write(os);
        os.close();
        wb.close();
        System.out.println("end........");
    }
}
