package com.sm.cn.service;

import com.sm.cn.common.http.PageVo;
import com.sm.cn.entity.Employee;

import java.util.List;

public interface EmployeeService {

    PageVo queryAllEmployee();

    void addEmployee(Employee employee);

    void deleteEmployeeBId(long id);

    Employee getEmployeeByEmail(String email);

    void updateEmployee(Employee employee);

    Employee getEmployeeById(long id);


    List<Employee> getAllEmployee();
}
