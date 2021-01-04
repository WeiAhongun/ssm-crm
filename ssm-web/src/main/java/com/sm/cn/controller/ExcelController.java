package com.sm.cn.controller;

import com.sm.cn.common.http.AxiosResult;
import com.sm.cn.entity.Employee;
import com.sm.cn.service.EmployeeService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("excel")
public class ExcelController {

    @Autowired
    private EmployeeService employeeService;
    private DecimalFormat df = new DecimalFormat("#");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @GetMapping("export")
    public ResponseEntity<byte []> export() throws IOException {
        List<Employee> allEmployee = employeeService.getAllEmployee();
        //创建工作簿
        Workbook workbook = new SXSSFWorkbook();
        Sheet employeeList = workbook.createSheet("员工列表");
        //创建行若干
        for (int i = 0; i < allEmployee.size(); i++) {
            Row row = employeeList.createRow(i);
            //创建单元格
            row.createCell(0).setCellValue(allEmployee.get(i).getEmployeeId());
            row.createCell(1).setCellValue(allEmployee.get(i).getEmployeeName());
            row.createCell(2).setCellValue(allEmployee.get(i).getEmployeeAddress());
            row.createCell(3).setCellValue(allEmployee.get(i).getEmployeeEmail());
            row.createCell(4).setCellValue(allEmployee.get(i).getEmployeePhone());
            row.createCell(5).setCellValue(String.valueOf(allEmployee.get(i).getEmployeeSalary()));
            row.createCell(4).setCellValue(allEmployee.get(i).getEmployeeDept());
        }

        ByteArrayOutputStream memory = new ByteArrayOutputStream();
        //工作簿写入内存中
        workbook.write(memory);
        workbook.close();
        //把内存中的流转成字节数组
        byte[] bytes = memory.toByteArray();

        //让浏览器解析为 员工列表.xlsx
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDispositionFormData("attachment", URLEncoder.encode("员工列表.xlsx","utf-8"));
        ResponseEntity responseEntity = new ResponseEntity(bytes,httpHeaders, HttpStatus.OK);

        memory.close();
        return responseEntity;
    }


    @PostMapping("import")
    public AxiosResult excelImport(HttpServletRequest request) throws IOException, ServletException, ParseException {
        Part excel = request.getPart("excel");
        XSSFWorkbook workbook = new XSSFWorkbook(excel.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        //获得所有行的索引
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            //获得第i行
            Row row = sheet.getRow(i);
            //获得一行有多少列，不是索引
            short lastCellNum = row.getLastCellNum();
            Object[] objects = new Object[lastCellNum];
            for (int j = 0; j < lastCellNum; j++) {
                Cell cell = row.getCell(j);
                if(cell!=null){
                    Object o = getCellObject(cell);
                    objects[j] = o;
                }
            }

            Employee employee = new Employee();
            //employee.setEmployeeId(df.parse(objects[0].toString()).longValue());
            employee.setEmployeeName(objects[1].toString());
            employee.setEmployeeAddress(objects[2].toString());
            employee.setEmployeeEmail(objects[3].toString());
            employee.setEmployeeDept(objects[4].toString());
            employee.setEmployeeSalary(new BigDecimal(objects[5].toString()));
            employee.setEmployeeTime(new Date(objects[6].toString()));
            employeeService.addEmployee(employee);
        }
        return AxiosResult.success();
    }

    public Object getCellObject(Cell cell){
        CellType cellTypeEnum = cell.getCellTypeEnum();
        Object obj = null;
        //判断单元格的枚举
        switch (cellTypeEnum){
            //如果单元格是字符串，执行下面
            case STRING:
                obj=cell.getStringCellValue();
                break;
                //如果单元格为空，执行下面
            case BLANK:
                obj="";
                break;
                //如果单元格是数字或者日期，执行下面
            case NUMERIC:
                if(DateUtil.isCellDateFormatted(cell)){
                    obj=cell.getDateCellValue();
                }else {
                    obj=cell.getNumericCellValue();
                }
                break;
                //如果单元格是公式，执行下面
            case FORMULA:
                obj=cell.getCellFormula();
                break;
        }
        return obj;
    }























}
