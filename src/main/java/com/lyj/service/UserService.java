package com.lyj.service;

import com.lyj.entity.MapVO;
import com.lyj.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {
    List<User> findAll(Integer start, Integer size);

    Integer findAllCount();

    User selectById(User user);

    User selectByTel(User user);

    void userLogin(User user);

    void save(User user);

    void delete(User user);

    void update(User user);

    void deleteAll(List<String> ids);

    List<User> queryAll();

    Integer rangeByTime(@Param("sex")String sex, @Param("day") Integer day);

    List<MapVO> AddressAndCount();
}
