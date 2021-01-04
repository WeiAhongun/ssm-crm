package com.sm.cn.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ExcelReference {
    public static void main(String[] args) throws Exception {
        //创建工作簿
        Workbook workbook = new XSSFWorkbook();
        //创建sheet
        Sheet sheet = workbook.createSheet("第一页");
        //通过索引创建行
        Row row = sheet.createRow(3);
        //通过行创建单元格,单元格中写入值
        Cell cell = row.createCell(3);
        cell.setCellValue("今天是2020/12/27,周日");
        /**
         * 设置列宽
         * i:表示给第几列设置
         * i1:表示设置多款
         */
        sheet.setColumnWidth(3,40*256);

        //设置行高
        row.setHeightInPoints(30);

        //设置单元格的样式
        CellStyle cellStyle = workbook.createCellStyle();
        //设置样式水平居中，垂直居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置单元格字体
        Font font = workbook.createFont();
        font.setColor(IndexedColors.ORANGE.getIndex());
        cellStyle.setFont(font);


        cell.setCellStyle(cellStyle);
        //输出流
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\wzj.xlsx");
        //写出工作簿
        workbook.write(fileOutputStream);
        workbook.close();
        fileOutputStream.close();

    }
}
