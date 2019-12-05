package com.lyj.cmfz.test;

import com.alibaba.excel.EasyExcel;
import com.lyj.cmfz.bean.DemoData;
import com.lyj.entity.User;
import com.lyj.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyEasyExcleTest extends Basetest {

    private String fileName = "C:\\Users\\Administrator\\Desktop\\Demo\\"+new Date().getTime()+"_测试.xlsx";

    @Test
    public void test01(){
        System.out.println("测试继承");
        List<DemoData> demoDataList = new ArrayList<>();
        for (int i=0;i<5;i++){
            DemoData demoData = new DemoData("数据"+i,new Date(),11.0,"忽略"+i);
            demoDataList.add(demoData);
        }
        EasyExcel.write(fileName, DemoData.class).sheet("模板").doWrite(demoDataList);
    }

    @Autowired
    private UserService userService;

    @Test
    public void test03(){
        System.out.println("测试继承");
        List<User> users = userService.queryAll();
        users.forEach(user -> System.out.println(user));
        EasyExcel.write(fileName, User.class).sheet("模板").doWrite(users);
    }

}
