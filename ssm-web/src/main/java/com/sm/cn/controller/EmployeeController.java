package com.sm.cn.controller;

import com.github.pagehelper.PageHelper;
import com.sm.cn.common.http.AxiosResult;
import com.sm.cn.common.http.PageVo;
import com.sm.cn.entity.Employee;
import com.sm.cn.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("employee")
    public AxiosResult employee(@RequestParam(defaultValue = "1") int currentPage,
                                @RequestParam(defaultValue = "5") int pageSize){
        PageHelper.startPage(currentPage,pageSize);
        return AxiosResult.success(employeeService.queryAllEmployee());
    }

    @PostMapping("employee")
    public AxiosResult employee1(@RequestBody Employee employee){
        employeeService.addEmployee(employee);
        return AxiosResult.success();
    }

    @DeleteMapping("employee/{id}")
    public AxiosResult employee2(@PathVariable long id){
        employeeService.deleteEmployeeBId(id);
        return AxiosResult.success();
    }

    @GetMapping("employee/{id}")
    public AxiosResult getEmployeeById(@PathVariable long id){
        Employee employee = employeeService.getEmployeeById(id);
        return AxiosResult.success(employee);
    }

    @PutMapping("employee")
    public AxiosResult updateEmployee(@RequestBody Employee employee){
        employeeService.updateEmployee(employee);
        return AxiosResult.success();
    }
}
