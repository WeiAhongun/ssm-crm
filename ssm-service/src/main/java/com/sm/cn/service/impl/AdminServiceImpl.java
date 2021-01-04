package com.sm.cn.service.impl;

import com.sm.cn.entity.Admin;
import com.sm.cn.mapper.AdminMapper;
import com.sm.cn.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Override
    public List<Admin> queryAllAdmin() {

        return adminMapper.selectByExample(null);

    }
}
