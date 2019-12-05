package com.lyj.cmfz.test;

import com.lyj.entity.User;
import com.lyj.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class testUser extends Basetest {

    @Autowired
    private UserService userService;

    @Test
    public void userlogin(){
        User user = new User();
        user.setTel("18236562773");
        user.setPassword("123456");
        String message = "";
        try {
            userService.userLogin(user);
            User u = new User();
            u.setTel(user.getTel());
            user = userService.selectByTel(u);
            System.out.println(user);
            if("2".equals(user.getStatus())){
                message = "该用户已被冻结";
                System.out.println(message);
            }else {
                message = "success";
                System.out.println(message);
            }
        } catch (Exception e) {
            message = e.getMessage();
            System.out.println(message);
        }
    }

    @Test
    public void userRegist(){
        User user = new User();
        user.setId(null);
        user.setTel("18236562773");
        user.setPassword("123456");
        User u = new User();
        u.setTel(user.getTel());
        System.out.println(u);
        User user1 = userService.selectByTel(u);
        if(user1 == null){
            userService.save(user);
            System.out.println(user);
        }else {
            System.out.println("已经有该用户");
        }
    }

    @Test
    public void testSelectTel(){
        User user = new User();
        user.setTel("11111111111");
        User user1 = userService.selectByTel(user);
        if(user1==null){
            System.out.println("暂无该用户");
        }else {
            System.out.println("该用户是："+user1);
        }
    }
}
