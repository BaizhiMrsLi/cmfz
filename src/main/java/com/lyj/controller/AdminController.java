package com.lyj.controller;

import com.lyj.entity.Admin;
import com.lyj.entity.AdminRole;
import com.lyj.service.AdminService;
import com.lyj.service.Admin_RoleService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private Admin_RoleService admin_roleService;

    @RequestMapping("findAll")
    public Map<String, Object> findAll(String searchField, String searchString, String searchOper, Integer page, Integer rows, Boolean _search) {
        //不做搜索处理
        Map<String, Object> map = new HashMap<>();

        List<Admin> admins = new ArrayList<>();
        Integer total = null;
        Integer records = null;

        if (_search) {
            System.out.println("暂时为用上");
        } else {
            admins = adminService.findAll((page - 1) * rows, rows);
            records = adminService.findAllCount();
        }

        if (records % rows == 0) {
            total = records / rows;
        } else {
            total = records / rows + 1;
        }

        map.put("rows", admins);
        map.put("page", page);
        map.put("total", total);
        map.put("records", records);
        return map;
    }

    @RequestMapping("opt")
    public void edit(Admin admin, String[] roleIds, String oper) {
        if ("edit".equals(oper)) {
            try {
                AdminRole a = new AdminRole();
                a.setA_id(admin.getId());
                //先删除关于该管理员的所有角色信息，随后在添加
                admin_roleService.delete(a);
                for (int i = 0; i < roleIds.length; i++) {
                    AdminRole admin_role = new AdminRole();
                    admin_role.setId(null);
                    admin_role.setA_id(admin.getId());
                    admin_role.setRole_id(roleIds[i]);
                    admin_roleService.save(admin_role);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
