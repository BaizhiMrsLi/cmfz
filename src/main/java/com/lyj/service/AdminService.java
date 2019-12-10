package com.lyj.service;

import com.lyj.entity.Admin;

import java.util.List;

public interface AdminService {
    Admin AdminLogin(Admin admin);

    Admin queryPermissions(String username);

    Admin selectOne(Admin admin);

    List<Admin> findAll(Integer start, Integer size);

    Integer findAllCount();
}
