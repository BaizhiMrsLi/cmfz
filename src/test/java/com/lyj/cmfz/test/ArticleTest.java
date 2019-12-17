package com.lyj.cmfz.test;

import com.lyj.entity.Article;
import com.lyj.repository.ArticleRepository;
import com.lyj.service.ArticleService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ArticleTest extends Basetest {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void test01(){
        Article article = new Article();
        article.setGuru_id("0");
        List<Article> articles = articleService.selectByChange(article);
        articles.forEach(article1 -> System.out.println(article1));
    }

    @Test
    public void test02() {
        List<Article> articles = articleService.queryAll();
        for (Article article : articles) {
            articleRepository.save(article);
        }
    }

    @Test
    public void test03() {
//        Article article = new Article();
//        article.setId("dc8039c2-15c6-11ea-ba00-005056c00001");
//        Article a = articleService.selectById(article);
        articleRepository.deleteById("d9e754f6-13eb-11ea-ba00-005056c00001");
    }
}
