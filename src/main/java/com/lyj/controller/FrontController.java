package com.lyj.controller;

import com.lyj.entity.Album;
import com.lyj.entity.Article;
import com.lyj.entity.Banner;
import com.lyj.service.AlbumService;
import com.lyj.service.ArticleService;
import com.lyj.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("front")
public class FrontController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private BannerService bannerService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private ArticleService articleService;

    @RequestMapping("queryAll")
    public Map queryAll(String uid,String type,String sub_type){
        Map map = new HashMap();
        List<Banner> banners = new ArrayList<>();
        List<Album> albums = new ArrayList<>();
        List<Article> articles = new ArrayList<>();
        SetOperations<String, String> set = stringRedisTemplate.opsForSet();
        if(sub_type==null){
            if("all".equals(type)){
                banners = bannerService.findAll(0, 5);
                albums = albumService.findAll(0, 6);
                articles = articleService.findAll(0, 6);
                map.put("banners",banners);
                map.put("albums",albums);
                map.put("articles",articles);
            }
            if("wen".equals(type)){
                albums = albumService.queryAll();
                map.put("albums",albums);
            }
            if("si".equals(type)){
                articles = articleService.queryAll();
                map.put("articles",articles);
            }
        }else{
            if("ssyj".equals(sub_type)){
                Set<String> members = set.members("focus"+uid);
                System.out.println(members);
                for (String member : members) {
                    Article article = new Article();
                    article.setGuru_id(member);
                    //查出该上师所有文章
                    List<Article> articles1 = articleService.selectByChange(article);
                    //将该上师所有文章放到集合里
                    for (Article article1 : articles1) {
                        articles.add(article1);
                    }
                }
                map.put("articles",articles);
            }
            if ("xmfy".equals(sub_type)){
                Article article = new Article();
                article.setGuru_id("0");
                articles = articleService.selectByChange(article);
                map.put("articles",articles);
            }
        }
        map.put("status","200");
        return map;
    }
}
