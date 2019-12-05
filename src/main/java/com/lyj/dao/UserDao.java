package com.lyj.dao;

import com.lyj.entity.MapVO;
import com.lyj.entity.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserDao extends Mapper<User>, DeleteByIdListMapper<User,String> {
    Integer rangeByTime(@Param("sex")String sex,@Param("day") Integer day);
    List<MapVO> AddressAndCount();
}
