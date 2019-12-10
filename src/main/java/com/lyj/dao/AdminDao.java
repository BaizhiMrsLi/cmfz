package com.lyj.dao;

import com.lyj.entity.Admin;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AdminDao extends Mapper<Admin> {
    Admin queryPermissions(String username);

    List<Admin> findAll(@Param("start") Integer start, @Param("size") Integer size);

    Integer findAllCount();
}
