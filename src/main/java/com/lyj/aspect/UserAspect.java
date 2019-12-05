package com.lyj.aspect;

import com.google.gson.Gson;
import com.lyj.entity.MapVO;
import com.lyj.service.UserService;
import io.goeasy.GoEasy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Configuration
public class UserAspect {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;

    @Around("@annotation(com.lyj.annotation.UserAnnotation)")
    public Object addLog(ProceedingJoinPoint proceedingJoinPoint) throws IOException {
        try {
            Object proceed = proceedingJoinPoint.proceed();

            List<MapVO> mapVOS = userService.AddressAndCount();
            Map<String, List<Integer>> map = new HashMap<>();
            Integer integer = userService.rangeByTime("1", 1);
            Integer integer1 = userService.rangeByTime("1", 7);
            Integer integer2 = userService.rangeByTime("1", 30);
            Integer integer3 = userService.rangeByTime("1", 365);
            map.put("man", Arrays.asList(integer, integer1, integer2, integer3));
            Integer integer4 = userService.rangeByTime("2", 1);
            Integer integer5 = userService.rangeByTime("2", 7);
            Integer integer6 = userService.rangeByTime("2", 30);
            Integer integer7 = userService.rangeByTime("2", 365);
            map.put("woman", Arrays.asList(integer4, integer5, integer6, integer7));

            GoEasy userRegist = new GoEasy("http://rest-hangzhou.goeasy.io","BC-908c13861eee49f0bc33598507687bde");
            userRegist.publish("userRegist", new Gson().toJson(map));

            GoEasy userMap = new GoEasy("http://rest-hangzhou.goeasy.io","BC-4cb2a7179c8e4b83a8200c6cc6094371");
            userMap.publish("userMap", new Gson().toJson(mapVOS));

            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

}
