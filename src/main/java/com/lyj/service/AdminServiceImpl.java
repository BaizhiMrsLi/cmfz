package com.lyj.service;

import com.lyj.dao.AdminDao;
import com.lyj.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminDao adminDao;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Admin AdminLogin(Admin admin) {
        return adminDao.selectOne(admin);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Admin queryPermissions(String username) {
        return adminDao.queryPermissions(username);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Admin selectOne(Admin admin) {
        return adminDao.selectOne(admin);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Admin> findAll(Integer start, Integer size) {
        return adminDao.findAll(start, size);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer findAllCount() {
        return adminDao.findAllCount();
    }
}
