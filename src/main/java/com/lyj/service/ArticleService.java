package com.lyj.service;

import com.lyj.entity.Article;

import java.util.List;

public interface ArticleService {
    List<Article> findAll(Integer start, Integer size);

    Integer findAllCount();

    List<Article> queryAll();

    Article selectById(Article article);

    List<Article> selectByChange(Article article);

    void save(Article article);

    void delete(Article article);

    void update(Article article);

    void deleteAll(List<String> ids);
}
