package com.lyj.task;

import com.alibaba.excel.EasyExcel;
import com.lyj.entity.User;
import com.lyj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ControllerTask {
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    private UserService userService;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        return new ThreadPoolTaskScheduler();
    }

    public void run(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String fileName = "C:\\Users\\Administrator\\Desktop\\Demo\\"+new Date().getTime()+"_用户信息.xlsx";
                List<User> users = userService.queryAll();
                EasyExcel.write(fileName,User.class).sheet("学生信息").doWrite(users);
                System.out.println("come in ");
            }
        };
        threadPoolTaskScheduler.schedule(runnable,new CronTrigger("0/30 * * * * *"));
    }
    public void shutdown(){
        threadPoolTaskScheduler.shutdown();
    }
}
