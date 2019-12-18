package com.lyj.controller;

import com.alibaba.fastjson.JSONArray;
import com.lyj.entity.MapVO;
import com.lyj.entity.User;
import com.lyj.service.MailServiceImpl;
import com.lyj.service.UserService;
import com.lyj.util.MD5Utils;
import com.lyj.util.Number6;
import com.lyj.util.SendMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    JmsTemplate jmsTemplate;

    @RequestMapping("findAll")
    public Map<String, Object> findAll(String searchField, String searchString, String searchOper, Integer page, Integer rows, Boolean _search) {
        //不做搜索处理
        Map<String, Object> map = new HashMap<>();

        List<User> users = new ArrayList<>();
        Integer total = null;
        Integer records = null;

        if (_search) {
            System.out.println("暂时为用上");
        } else {
            users = userService.findAll((page - 1) * rows, rows);
            records = userService.findAllCount();
        }

        if (records % rows == 0) {
            total = records / rows;
        } else {
            total = records / rows + 1;
        }

        map.put("rows", users);
        map.put("page", page);
        map.put("total", total);
        map.put("records", records);
        return map;
    }

    @RequestMapping("updateUserStatus")
    public void updateUserStatus(User user) {
        user = userService.selectById(user);
        if ("1".equals(user.getStatus())) {
            user.setStatus("2");
        } else {
            user.setStatus("1");
        }
        userService.update(user);
    }

    @RequestMapping("regist")
    public Map<String, List<Integer>> regist() {
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
        return map;
    }

    @RequestMapping("userMap")
    public List<MapVO> userMap() {
        List<MapVO> mapVOS = userService.AddressAndCount();
        return mapVOS;
    }


    @RequestMapping("sendTelCode")
    public Map<String,String> sendTelCode(User user){
        String message = "";
        String status = "";
        Map<String,String> map = new HashMap<>();
        if(user.getTel()!= null){
            String code = Number6.getNum();
            message = SendMessage.sendMessage(user.getTel(), code);
            if ("0".equals(message)){
                //将验证码存到redis
                ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
                stringStringValueOperations.set("telCode",code,1, TimeUnit.MINUTES);
                message = "success";
                status = "200";
            }else {
                message = "error";
                status = "-200";
            }
        }
        map.put(status,message);
        return map;
    }

    @Autowired
    private MailServiceImpl mailService;

    @RequestMapping("registUser")
    public Map<String,Object> registUser(String code,User user){
        Map<String,Object> map = new HashMap<>();
        //在发送验证码之前先判断该电话是否被注册
        User user1 = userService.selectByTel(user);
        if(user1!=null){
            map.put("status","-200");
            map.put("message","该手机号已被注册!");
        }else {
            ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
            String codes = stringStringValueOperations.get("telCode");
            if(codes!=null){
                if(codes.equals(code)){
                    //发送验证码成功，可以将手机号入库
                    userService.save(user);
                    map.put("status","200");
                    map.put("message","success");
                    //手机号入库之后，发送消息.
                    ActiveMQQueue springbootQueue = new ActiveMQQueue("cmfzUserRegist");
                    Map m = new HashMap();

                    m.put("tel", user.getTel());

                    m.put("toEmail", "875440649@qq.com");
                    m.put("title", "【持明法洲】");
                    m.put("content", "恭喜您注册成功！");
                    String s = JSONArray.toJSON(m).toString();
                    jmsTemplate.convertAndSend(springbootQueue, s);
                }else {
                    map.put("status","-200");
                    map.put("message","验证码错误!");
                }
            }else {
                map.put("status","-200");
                map.put("message","验证码超时!");
            }
        }
        return map;
    }

    @JmsListener(destination = "cmfzUserRegist")
    public void Consumers(TextMessage msg) {
        try {
            System.out.println("come in ");
            System.out.println("json:" + msg.getText());
            Map map = JSONArray.parseObject(msg.getText());
            //发送成功消息到手机
            String message = SendMessage.sendSuccessMessage(map.get("tel").toString(), map.get("tel").toString());
            System.out.println("状态:" + message);
            //发送成功消息邮箱
            mailService.sendHtmlMail(map.get("toEmail").toString(), map.get("title").toString(), map.get("content").toString());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("updUser")
    public Map updUser(User user){
        Map map = new HashMap();
        User u = new User();
        u.setTel(user.getTel());
        //先用手机号查该手机号是否存在
        User user1 = userService.selectByTel(u);
        //手机号存在则进行修改
        if(user1 == null){
            map.put("status","-200");
            map.put("message","用户不存在，无法修改!");
        }else {
            try {
                user.setId(user1.getId());
                user.setSalt(MD5Utils.getSalt());
                user.setPassword(MD5Utils.getPassword(user.getPassword()+user.getSalt()));
                //在修改之前，先把密码加盐
                userService.update(user);
                //把用户数据带回去
                user = userService.selectById(user);
                map.put("status","200");
                map.put("message",user);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("status","-200");
                map.put("message","修改失败!");
            }
        }
        return map;
    }

    @RequestMapping("userLogin")
    public Map<String,Object> userlogin(User user){
        Map<String,Object> map = new HashMap<>();
        String message = "";
        try {
            userService.userLogin(user);
            User u = new User();
            u.setTel(user.getTel());
            user = userService.selectByTel(u);
            if("2".equals(user.getStatus())){
                message = "该用户已被冻结";
            }else {
                message = "success";
                //修改最后登录
                user.setLast_login(new Date());
                userService.update(user);
            }
        } catch (Exception e) {
            message = e.getMessage();
        }
        if("success".equals(message)){
            map.put("status","200");
            map.put("message",user);
        }else {
            map.put("status","-200");
            map.put("message",message);
        }
        return map;
    }


    @RequestMapping("focusGuru")
    public Map focusGuru(String uid,String id){
        Map map = new HashMap();
        SetOperations<String, String> set = stringRedisTemplate.opsForSet();
        //关注上师
        set.add("focus"+uid,id);
        set.add("fans"+id,uid);

        Set<String> members = set.members("focus" + uid);

        map.put("status","200");
        map.put("gurus",members);
        return map;
    }

    @RequestMapping("nofocusGuru")
    public Map nofocusGuru(String uid,String id){
        Map map = new HashMap();
        SetOperations<String, String> set = stringRedisTemplate.opsForSet();
        //取消关注上师
        set.remove("focus"+uid,id);
        set.remove("fans"+id,uid);

        Set<String> members = set.members("focus" + uid);

        map.put("status","200");
        map.put("gurus",members);
        return map;
    }

    @RequestMapping("friends")
    public Map friends(String uid) {
        Map map = new HashMap();
        SetOperations<String, String> set = stringRedisTemplate.opsForSet();
        //从redis中随机取两位上师id
        Set<String> strings = set.distinctRandomMembers("focus" + uid, 2);
        List<String> list = new ArrayList<>();
        for (String string : strings) {
            list.add("fans" + string);
        }
        System.out.println("获取到的上师id集合：" + list);
        Set<String> intersect = set.intersect(list);
        System.out.println("获取到的用户交集：" + intersect);
        intersect.remove(uid);
        System.out.println("获取到的用户交集(去除本用户id)：" + intersect);
        List<User> users = new ArrayList<>();
        for (String s : intersect) {
            User user = new User();
            user.setId(s);
            User u = userService.selectById(user);
            users.add(u);
        }
        map.put("status", "200");
        map.put("users", users);
        return map;
    }

}
