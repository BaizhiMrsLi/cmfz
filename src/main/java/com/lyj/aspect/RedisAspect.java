package com.lyj.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;

@Aspect
@Configuration
public class RedisAspect {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Around("@annotation(com.lyj.annotation.RedisAddAnnotation)")
    public Object addRedis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //通过反射获得类名
        String clazzName = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        //通过反射获得方法名
        String name = proceedingJoinPoint.getSignature().getName();
        //通过反射获得方法参数
        Object[] args = proceedingJoinPoint.getArgs();
        //参数拼接中间加','
        String arg = Arrays.toString(args);
        //redis中的key，方法名+参数
        String key = name + "," + arg;
        //设置序列化类型
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        stringRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        //获取redis缓存
        HashOperations<String, Object, Object> hash = stringRedisTemplate.opsForHash();
        Object o = hash.get(clazzName, key);
        if (o != null) {
            System.out.println("从redis中获取的数据");
            return o;
        }
        //从数据库中获取数据放到redis
        Object proceed = proceedingJoinPoint.proceed();
        hash.put(clazzName, key, proceed);
        return proceed;
    }

    @Around("@annotation(com.lyj.annotation.RedisDelAnnotation)")
    public Object delRedis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object proceed = proceedingJoinPoint.proceed();
        System.out.println("come in delRedis");
        String clazzName = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        stringRedisTemplate.delete(clazzName);
        return proceed;
    }
}
