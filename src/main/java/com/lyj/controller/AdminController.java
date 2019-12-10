package com.lyj.controller;

import com.lyj.entity.Admin;
import com.lyj.service.AdminService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @RequestMapping("login")
    public String adminLogin(HttpServletRequest request,String captcha, Admin admin){
        HttpSession session = request.getSession();
        String securityCode = (String) session.getAttribute("securityCode");
        if(!captcha.equals(securityCode)){
            return "验证码错误!";
        }

//        1.封装令牌
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(admin.getUsername(), admin.getPassword());
//        2.通过Util类获取subject主体
        Subject subject = SecurityUtils.getSubject();
//        3.登录
        try {
            subject.login(usernamePasswordToken);
            return "";
        } catch (IncorrectCredentialsException i) {
            i.printStackTrace();
            return "密码错误！";
        } catch (AuthenticationException u) {
            u.printStackTrace();
            return "账户不存在！";
        }

//        Admin a = new Admin();
//        a.setUsername(admin.getUsername());
//        Admin login = adminService.AdminLogin(a);
//        if(login==null){
//            return "该用户不存在!";
//        }else if (login.getPassword().equals(admin.getPassword())){
//            session.setAttribute("admin",login);
//            return "";
//        }else {
//            return "密码错误!";
//        }
    }

    @RequestMapping("safeOut")
    public void safeOut() {
        //获取主体信息
        Subject subject = SecurityUtils.getSubject();
        //执行登出方法
        subject.logout();
    }
}
