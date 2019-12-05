package com.lyj.service;

import com.lyj.annotation.UserAnnotation;
import com.lyj.dao.UserDao;
import com.lyj.entity.MapVO;
import com.lyj.entity.User;
import com.lyj.util.MD5Utils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> findAll(Integer start, Integer size) {
        return userDao.selectByRowBounds(new User(),new RowBounds(start,size));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer findAllCount() {
        return userDao.selectCount(new User());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User selectById(User user) {
        return userDao.selectByPrimaryKey(user);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User selectByTel(User user) {
        return userDao.selectOne(user);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void userLogin(User user) {
        User u = new User();
        u.setTel(user.getTel());
        User login = userDao.selectOne(u);
        if(login == null)throw new RuntimeException("该用户不存在");
        //通过工具类将登录用户输入的密码与根据邮箱查到的盐拼接之后进行md5加密，再放到密码中,进行下一步的密码验证
        user.setPassword(MD5Utils.getPassword(user.getPassword()+login.getSalt()));
        if(!(login.getPassword().equals(user.getPassword())))throw new RuntimeException("该用户密码不正确");
    }

    @Override
    @UserAnnotation("")
    public void save(User user) {
        user.setRegistration_date(new Date());
        user.setStatus("1");
        userDao.insert(user);
    }

    @Override
    @UserAnnotation("")
    public void delete(User user) {
        userDao.delete(user);
    }

    @Override
    @UserAnnotation("")
    public void update(User user) {
        userDao.updateByPrimaryKeySelective(user);
    }

    @Override
    @UserAnnotation("")
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteAll(List<String> ids) {
        userDao.deleteByIdList(ids);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> queryAll() {
        return userDao.selectAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer rangeByTime(String sex, Integer day) {
        return userDao.rangeByTime(sex,day);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<MapVO> AddressAndCount() {
        return userDao.AddressAndCount();
    }
}
