package com.lyj.realm;

import com.lyj.entity.Admin;
import com.lyj.entity.Resource;
import com.lyj.entity.Role;
import com.lyj.service.AdminService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MyRealm extends AuthorizingRealm {
    @Autowired
    private AdminService adminService;

    //    授权方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取主身份信息,相当于该用户成功登录的账号
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        // 通过主身份信息查询数据库,获取该用户拥有的所有信息
        Admin admin = adminService.queryPermissions(primaryPrincipal);

        ArrayList roles = new ArrayList();
        ArrayList resources = new ArrayList();
//        获取对象所拥有的角色时，进行循环赋值
        List<Role> role = admin.getRoles();
        for (Role role1 : role) {
            roles.add(role1.getRole_name());
            for (Resource resource : role1.getResources()) {
                resources.add(resource.getResource_name());
            }
        }
        //SimpleAuthorizationInfo继承了AuthorizationInfo,并且能够添加角色以及对资源的权限
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(roles);
        authorizationInfo.addStringPermissions(resources);
        return authorizationInfo;
    }

    //    认证方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String principal = (String) authenticationToken.getPrincipal();
        //先创建一个对象，set进去一个username，去数据库查找
        Admin admin = new Admin();
        admin.setUsername(principal);
        //去数据库查找
        Admin admin1 = adminService.selectOne(admin);
        //根据该方法需要返回的对象，进行返回创建,用SimpleAuthenticationInfo进行封装
        AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(admin1.getUsername(), admin1.getPassword(), this.getName());
        return authenticationInfo;
    }
}
