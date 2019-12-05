package com.lyj.service;

import com.lyj.annotation.LogAnnotation;
import com.lyj.dao.ArticleDao;
import com.lyj.entity.Article;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleDao articleDao;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Article> findAll(Integer start, Integer size) {
        return articleDao.selectByRowBounds(new Article(),new RowBounds(start,size));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer findAllCount() {
        return articleDao.selectCount(new Article());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Article> queryAll() {
        return articleDao.selectAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Article selectById(Article article) {
        return articleDao.selectByPrimaryKey(article);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Article> selectByChange(Article article) {
        return articleDao.select(article);
    }

    @Override
    @LogAnnotation(value = "添加文章")
    public void save(Article article) {
        articleDao.insert(article);
    }

    @Override
    @LogAnnotation(value = "删除文章")
    public void delete(Article article) {
        articleDao.delete(article);
    }

    @Override
    @LogAnnotation(value = "修改文章")
    public void update(Article article) {
        articleDao.updateByPrimaryKeySelective(article);
    }

    @Override
    @LogAnnotation(value = "批量删除文章")
    public void deleteAll(List<String> ids) {
        articleDao.deleteByIdList(ids);
    }
}
