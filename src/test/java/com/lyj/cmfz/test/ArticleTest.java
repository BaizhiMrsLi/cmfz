package com.lyj.cmfz.test;

import com.lyj.entity.Article;
import com.lyj.service.ArticleService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ArticleTest extends Basetest {
    @Autowired
    private ArticleService articleService;

    @Test
    public void test01(){
        Article article = new Article();
        article.setGuru_id("0");
        List<Article> articles = articleService.selectByChange(article);
        articles.forEach(article1 -> System.out.println(article1));
    }
}
