package com.lyj.service;

import com.lyj.entity.Admin;

public interface AdminService {
    Admin AdminLogin(Admin admin);

    Admin queryPermissions(String username);

    Admin selectOne(Admin admin);
}
