package com.lyj.config;

import com.lyj.realm.MyRealm;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

// 1. 声明一个配置类 @Configuration 类
@Configuration
public class ShiroFilter {
    // 2. 声明一个@Bean 对象交由Spring工厂管理 需要的Bean对象为过滤器
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        // 可以 通过ShiroFilterFactoryBean 配置整个shiro 过滤器
        // 3. 创建一个shiroFilterFactoryBean对象
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //配置过滤器链 注意 1.使用LinkedHashMap 2.要求将anon过滤器声明写在前面
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        // 注意 : 如出现静态资源拦截问题 会network 显示 302 错误
        linkedHashMap.put("/boot/**", "anon");
        linkedHashMap.put("/echarts/**", "anon");
        linkedHashMap.put("/img/**", "anon");
        linkedHashMap.put("/jqgrid/**", "anon");
        linkedHashMap.put("/kindeditor/**", "anon");
        linkedHashMap.put("/upload/**", "anon");
        // 将登陆方法 放行
        linkedHashMap.put("/admin/login", "anon");
        //将生成验证码也放行
        linkedHashMap.put("/captcha/captcha", "anon");
        //默认拦截所有，都需要认证，由于是链式，所以最后执行
        linkedHashMap.put("/**", "authc");
        // 6. 将过滤器链交由shiroFilterFactoryBean管理
        shiroFilterFactoryBean.setFilterChainDefinitionMap(linkedHashMap);
        // 7. 设置登录Url
        shiroFilterFactoryBean.setLoginUrl("/back/login.jsp");
        // 8. 将DefaultWebSecurityManager对象交由shiroFilterFactoryBean管理
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        return shiroFilterFactoryBean;
    }

    // 创建SecurityManager对象 交给spring工厂管理
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm());
        CacheManager cacheManager = new EhCacheManager();
        securityManager.setCacheManager(cacheManager);
        return securityManager;
    }

    // 创建myRealm对象 交给spring工厂管理
    @Bean
    public MyRealm myRealm() {
        MyRealm myRealm = new MyRealm();
        return myRealm;
    }
}
