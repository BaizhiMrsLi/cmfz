package com.lyj.repository;

import com.lyj.entity.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

//使用自定义接口继承ElasticsearchRepository
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {
}
