package com.lyj.aspect;

import com.lyj.annotation.LogAnnotation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
@Configuration
public class LogAspect {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Around("@annotation(com.lyj.annotation.LogAnnotation)")
    public Object addLog(ProceedingJoinPoint proceedingJoinPoint) throws IOException {

        String realPath = request.getServletContext().getRealPath("/upload/log");
        File f = new File(realPath);
        if(!f.exists()){
            f.mkdirs();
        }
        //向文件中输入一段中文
        Writer w = new FileWriter(realPath+"/log.txt",true);
        BufferedWriter bw = new BufferedWriter( w );

        //获取当前的管理员
        Subject subject = SecurityUtils.getSubject();
        Object admin = subject.getPrincipal();
        if (admin != null) {
            //当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf.format(date);
            //获取方法名
            String name = proceedingJoinPoint.getSignature().getName();
            //获取方法上注解的信息
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            LogAnnotation annotation = signature.getMethod().getAnnotation(LogAnnotation.class);
            String value = annotation.value();
            String status = "";

            //创建redis，使用map结构
            HashOperations<String, Object, Object> maps = stringRedisTemplate.opsForHash();
            try {
                Object proceed = proceedingJoinPoint.proceed();
                status = "success";
                String sb = "管理员:" + admin.toString() + "，在" + format + "时间操作了" + name + "方法，做了" + value + "，此操作的状态:" + status;
                maps.put("持明法洲日志信息", admin.toString() + format, sb);
                bw.write(sb);//\n表示newLine \r 表示return \r\n   \r  \n
                bw.newLine();//换行
                bw.close();
                w.close();
                return proceed;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                status = "error";
                String sb = "管理员:" + admin.toString() + "，在" + format + "时间操作了" + name + "方法，做了" + value + "，此操作的状态:" + status;
                maps.put("持明法洲日志信息", admin.toString() + format, sb);
                bw.write(sb);//\n表示newLine \r 表示return \r\n   \r  \n
                bw.newLine();//换行
                bw.close();
                w.close();
                return null;
            }
        }
        return null;
    }

}
