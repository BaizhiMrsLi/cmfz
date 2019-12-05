package com.lyj.controller;

import com.lyj.entity.Article;
import com.lyj.entity.Guru;
import com.lyj.service.ArticleService;
import com.lyj.service.GuruService;
import com.lyj.util.PhotoUpload;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @RequestMapping("findAll")
    public Map<String, Object> findAll(String searchField, String searchString, String searchOper, Integer page, Integer rows, Boolean _search) {
        //不做搜索处理
        Map<String, Object> map = new HashMap<>();

        List<Article> articles = new ArrayList<>();
        Integer total = null;
        Integer records = null;

        if (_search) {
//            banners = poemService.findAlls(searchField, searchString, searchOper,(page-1)*rows,rows);
//            records = poemService.findAllsTotalCounts(searchField, searchString, searchOper);
        } else {
            articles = articleService.findAll((page - 1) * rows, rows);
            records = articleService.findAllCount();
        }

        if (records % rows == 0) {
            total = records / rows;
        } else {
            total = records / rows + 1;
        }

        map.put("rows", articles);
        map.put("page", page);
        map.put("total", total);
        map.put("records", records);
        return map;
    }

    //富文本框的图片上传
    @RequestMapping("uploadImg")
    public Map uploadImg(MultipartFile imgFile, HttpSession session, HttpServletRequest request){
        HashMap hashMap = new HashMap();
        String dir = "/upload/articleImg/";
        try {
            String httpUrl = PhotoUpload.getHttpUrl(imgFile, request, session, dir);
            hashMap.put("error",0);
            hashMap.put("url",httpUrl);
        } catch (Exception e) {
            hashMap.put("error",1);
            hashMap.put("message","上传错误");
            e.printStackTrace();
        }
        return hashMap;
    }

    //图片空间的回显
    @RequestMapping("showAllImgs")
    public Map showAllImgs(HttpSession session,HttpServletRequest request){
        // 1. 获取文件夹绝对路径
        String realPath = session.getServletContext().getRealPath("/upload/articleImg/");
        // 2. 准备返回的Json数据
        HashMap hashMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        // 3. 获取目标文件夹
        File file = new File(realPath);
        File[] files = file.listFiles();
        // 4. 遍历文件夹中的文件
        for (File file1 : files) {
            // 5. 文件属性封装
            HashMap fileMap = new HashMap();
            fileMap.put("is_dir",false);
            fileMap.put("has_file",false);
            fileMap.put("filesize",file1.length());
            fileMap.put("is_photo",true);
            // 获取文件后缀 | 文件类型
            String extension = FilenameUtils.getExtension(file1.getName());
            fileMap.put("filetype",extension);
            fileMap.put("filename",file1.getName());
            // 获取文件上传时间 1. 截取时间戳 2. 创建格式转化对象 3. 格式类型转换
            String s = file1.getName().split("_")[0];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String format = simpleDateFormat.format(new Date(Long.valueOf(s)));
            fileMap.put("datetime",format);
            arrayList.add(fileMap);
        }
        hashMap.put("file_list",arrayList);
        hashMap.put("total_count",arrayList.size());
        // 返回路径为 项目名 + 文件夹路径
        hashMap.put("current_url",request.getContextPath()+"/upload/articleImg/");
        return hashMap;
    }

    @Autowired
    private GuruService guruService;
    @RequestMapping("updateArticle")
    public void insertArticle(MultipartFile aa, Article article,HttpServletRequest request,HttpSession session){
        if(aa.getSize()!=0){
            String dir = "/upload/articleCover/";
            String httpUrl = PhotoUpload.getHttpUrl(aa, request, session, dir);
            article.setCover(httpUrl);
        }
        if(!"0".equals(article.getGuru_id())){
            Guru guru = new Guru();
            guru.setId(article.getGuru_id());
            guru = guruService.selectById(guru);
            article.setAuthor(guru.getNickname());
        }else {
            article.setAuthor("通用文章");
        }
        articleService.update(article);
    }

    @RequestMapping("addArticle")
    public void addArticle(MultipartFile aa, Article article,HttpServletRequest request,HttpSession session){
        System.out.println(article);
        article.setId(null);
        if(aa.getSize()!=0){
            String dir = "/upload/articleCover/";
            String httpUrl = PhotoUpload.getHttpUrl(aa, request, session, dir);
            article.setCover(httpUrl);
        }
        if(!"0".equals(article.getGuru_id())){
            Guru guru = new Guru();
            guru.setId(article.getGuru_id());
            guru = guruService.selectById(guru);
            article.setAuthor(guru.getNickname());
        }else {
            article.setAuthor("通用文章");
        }
        Date d = new Date();
        article.setPublish_date(d);
        article.setCreate_date(d);

        System.out.println(article);

        articleService.save(article);
    }

    //不用再判断是修改添加还是删除，在这里是只有删除
    @RequestMapping("opt")
    public void edit(Article article, String oper,HttpServletRequest request) {
        String[] split = article.getId().split(",");
        List<String> list = Arrays.asList(split);
        //批量删除
        articleService.deleteAll(list);
    }


    @RequestMapping("selArticleById")
    public Map selArticleById(String uid,Article article){
        Map map = new HashMap();
        article = articleService.selectById(article);
        map.put("status","200");
        map.put("article",article);
        return map;
    }
}
