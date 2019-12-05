package com.lyj.task;

import com.alibaba.excel.EasyExcel;
import com.lyj.entity.User;
import com.lyj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Async
public class SpringTask {
    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 0 12 ? * SUN")
    public void task(){
        String fileName = "C:\\Users\\Administrator\\Desktop\\Demo\\"+new Date().getTime()+"_用户信息.xlsx";
        List<User> users = userService.queryAll();
        EasyExcel.write(fileName,User.class).sheet("学生信息").doWrite(users);
        System.out.println("come in ");
    }
}
