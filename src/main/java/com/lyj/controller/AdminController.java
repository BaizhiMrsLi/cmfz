package com.lyj.controller;

import com.lyj.entity.Admin;
import com.lyj.service.AdminService;
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
        Admin a = new Admin();
        a.setUsername(admin.getUsername());
        Admin login = adminService.AdminLogin(a);
        if(login==null){
            return "该用户不存在!";
        }else if (login.getPassword().equals(admin.getPassword())){
            session.setAttribute("admin",login);
            return "";
        }else {
            return "密码错误!";
        }
    }

    @RequestMapping("safeOut")
    public void safeOut(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("admin");
    }
}
