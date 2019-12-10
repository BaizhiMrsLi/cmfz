package com.lyj.service;

import com.lyj.dao.Admin_RoleDao;
import com.lyj.entity.AdminRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class Admin_RoleServiceImpl implements Admin_RoleService {
    @Autowired
    private Admin_RoleDao admin_roleDao;

    @Override
    public void save(AdminRole admin_role) {
        admin_roleDao.insert(admin_role);
    }

    @Override
    public void delete(AdminRole admin_role) {
        admin_roleDao.delete(admin_role);
    }
}
