package com.lyj.cmfz.test;

import com.lyj.dao.UserDao;
import com.lyj.entity.User;
import com.lyj.util.DateRandomTest;
import com.lyj.util.MD5Utils;
import com.lyj.util.Number10;
import com.lyj.util.Number6;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;
import java.util.UUID;

public class InsertTest extends Basetest {

    @Autowired
    private UserDao userDao;

    @Test
    public void insertProviences() {
        String[] zhanshi = {"1", "2"};
        String[] name = {"小张", "小红", "小李", "小明"};
        String[] sex = {"1", "2"};
        String[] location = {"北京", "天津", "上海", " 重庆", " 河北", "河南", "云南", " 辽宁", "湖南", "安徽", "山东", "新疆",
                "江苏", "浙江", "江西", "湖北", "广西", "甘肃", "山西", "陕西", "吉林", "福建", "贵州", "广东", "青海", "西藏",
                "四川", "宁夏", "海南", "台湾", "香港", "澳门", "内蒙古", "黑龙江"};
        for (int i = 0; i < 20; i++) {
            User uu = new User();
            uu.setId(UUID.randomUUID().toString());
            uu.setTel(1 + Number10.getNum());
            uu.setPassword(Number6.getNum());
            uu.setSalt(MD5Utils.getSalt());
            uu.setName(name[new Random().nextInt(name.length)] + Number6.getNum());
            uu.setNickname(null);
            uu.setSex(sex[new Random().nextInt(sex.length)]);
            uu.setSignature(null);
            uu.setCover(null);
            uu.setAddress(location[new Random().nextInt(location.length)]);
            uu.setRegistration_date(DateRandomTest.date());
            uu.setLast_login(null);
            uu.setStatus(zhanshi[new Random().nextInt(zhanshi.length)]);
            userDao.insert(uu);
        }
//        注册当天用户
//        for (int i = 0; i < 50; i++) {
//            User uu = new User();
//            uu.setId(UUID.randomUUID().toString());
//            uu.setTel(1 + Number10.getNum());
//            uu.setPassword(Number6.getNum());
//            uu.setSalt(MD5Utils.getSalt());
//            uu.setName(name[new Random().nextInt(name.length)] + Number6.getNum());
//            uu.setNickname(null);
//            uu.setSex(sex[new Random().nextInt(sex.length)]);
//            uu.setSignature(null);
//            uu.setCover(null);
//            uu.setAddress(location[new Random().nextInt(location.length)]);
//            uu.setRegistration_date(new Date());
//            uu.setLast_login(null);
//            uu.setStatus(zhanshi[new Random().nextInt(zhanshi.length)]);
//            userDao.insert(uu);
//        }

    }
}
