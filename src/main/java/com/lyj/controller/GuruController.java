package com.lyj.controller;

import com.lyj.entity.Guru;
import com.lyj.service.GuruService;
import com.lyj.util.PhotoUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("guru")
public class GuruController {
    @Autowired
    private GuruService guruService;

    @RequestMapping("showGuruList")
    public List<Guru> showGuruList(){
        List<Guru> gurus = guruService.findAllGuru();
        return gurus;
    }

    @RequestMapping("findAll")
    public Map<String, Object> findAll(String searchField, String searchString, String searchOper, Integer page, Integer rows, Boolean _search) {
        //不做搜索处理
        Map<String, Object> map = new HashMap<>();

        List<Guru> gurus = new ArrayList<>();
        Integer total = null;
        Integer records = null;

        if (_search) {
            System.out.println("暂时不用");
        } else {
            gurus = guruService.findAll((page - 1) * rows, rows);
            records = guruService.findAllCount();
        }

        if (records % rows == 0) {
            total = records / rows;
        } else {
            total = records / rows + 1;
        }

        map.put("rows", gurus);
        map.put("page", page);
        map.put("total", total);
        map.put("records", records);
        return map;
    }

    @RequestMapping("opt")
    public Map<String, String> edit(Guru guru, String oper, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        if ("add".equals(oper)) {
            try {
                guru.setId(null);
                guruService.save(guru);
                map.put("status", "addOk");
                map.put("data", guru.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ("del".equals(oper)) {
            String[] split = guru.getId().split(",");
            List<String> list = Arrays.asList(split);
            try {
                //批量删除
                guruService.deleteAll(list);
                map.put("status", "delOk");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ("edit".equals(oper)) {
            try {
                guruService.update(guru);
                map.put("data", guru.getId());
                map.put("status", "updateOk");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @RequestMapping("motifyUpload")
    public void motifyUpload(MultipartFile aa, String id, HttpServletRequest request, HttpSession session) throws IOException, IOException {
        System.out.println("id :" + id);
        System.out.println("cover:" + aa.getOriginalFilename());
        Guru guru = new Guru();
        guru.setId(id);
        guru = guruService.selectById(guru);
        if(aa.getSize()!=0){
            String dir = "/upload/guruCover/";
            String httpUrl = PhotoUpload.getHttpUrl(aa, request, session, dir);
            guru.setCover(httpUrl);
        }
        guruService.update(guru);
    }

    @RequestMapping("upload")
    public void upload(MultipartFile aa, String id, HttpServletRequest request,HttpSession session) throws IOException {
        System.out.println("id :" + id);
        System.out.println("cover:" + aa.getOriginalFilename());
        Guru guru = new Guru();
        guru.setId(id);
        guru = guruService.selectById(guru);
        if(aa.getSize()!=0){
            String dir = "/upload/guruCover/";
            String httpUrl = PhotoUpload.getHttpUrl(aa, request, session, dir);
            guru.setCover(httpUrl);
        }
        guruService.update(guru);
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @RequestMapping("queryAll")
    public Map queryAll(String uid){
        Map map = new HashMap();
        List<Guru> gurus = guruService.findAllGuru();
        SetOperations<String, String> set = stringRedisTemplate.opsForSet();
        Set<String> members = set.members("focus" + uid);
        List<Guru> FocusGurus = new ArrayList<>();
        for (String member : members) {
            Guru guru = new Guru();
            guru.setId(member);
            FocusGurus.add(guruService.selectById(guru));
        }

        map.put("status","200");
        map.put("gurus",gurus);
        map.put("FocusGurus",FocusGurus);
        
        return map;
    }
}
