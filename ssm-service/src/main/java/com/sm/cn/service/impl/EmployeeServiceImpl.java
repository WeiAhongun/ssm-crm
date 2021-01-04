package com.sm.cn.service.impl;

import com.github.pagehelper.PageInfo;
import com.sm.cn.common.http.PageVo;
import com.sm.cn.entity.Employee;
import com.sm.cn.entity.EmployeeExample;
import com.sm.cn.mapper.EmployeeMapper;
import com.sm.cn.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Override
    public PageVo queryAllEmployee() {
        List<Employee> employees = employeeMapper.selectByExample(null);
        PageInfo pageInfo = new PageInfo(employees);
        return new PageVo(pageInfo.getTotal(),employees);
    }

    @Override
    public void addEmployee(Employee employee) {
        employeeMapper.insert(employee);
    }

    @Override
    public void deleteEmployeeBId(long id) {
        employeeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        EmployeeExample employeeExample = new EmployeeExample();
        employeeExample.createCriteria().andEmployeeEmailEqualTo(email);
        List<Employee> employees = employeeMapper.selectByExample(employeeExample);
        if(employees.size()>0){
            return employees.get(0);
        }
        return null;
    }

    @Override
    public void updateEmployee(Employee employee) {
        employeeMapper.updateByPrimaryKeySelective(employee);
    }

    @Override
    public Employee getEmployeeById(long id) {
        EmployeeExample employeeExample = new EmployeeExample();
        employeeExample.createCriteria().andEmployeeIdEqualTo(id);
        List<Employee> employees = employeeMapper.selectByExample(employeeExample);
        if(employees.size()>0)
        {
            return employees.get(0);
        }
        return null;
    }

    @Override
    public List<Employee> getAllEmployee() {
        return employeeMapper.selectByExample(null);
    }
}
