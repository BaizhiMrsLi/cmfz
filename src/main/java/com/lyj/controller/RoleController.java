package com.lyj.controller;

import com.lyj.entity.Role;
import com.lyj.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequestMapping("role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequestMapping("findAll")
    public void findAll(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        List<Role> roles = roleService.findAll();

        PrintWriter writer = response.getWriter();

        StringBuilder str = new StringBuilder();
        str.append("<select>");
        for (Role role : roles) {
            str.append("<option value='" + role.getId() + "'>" + role.getRole_name() + "</option>");
        }
        str.append("</select>");
        writer.print(str);
        writer.close();
    }
}
