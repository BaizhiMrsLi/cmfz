package com.lyj.service;

import com.lyj.dao.RoleDao;
import com.lyj.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Role> findAll() {
        return roleDao.selectAll();
    }
}
