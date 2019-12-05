package com.lyj.service;

import com.lyj.annotation.LogAnnotation;
import com.lyj.dao.ChapterDao;
import com.lyj.entity.Chapter;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChapterServiceImpl implements ChapterService {
    @Autowired
    private ChapterDao chapterDao;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Chapter> findAll(Chapter chapter,Integer start, Integer size) {
        return chapterDao.selectByRowBounds(chapter,new RowBounds(start,size));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer findAllCount(Chapter chapter) {
        return chapterDao.selectCount(chapter);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Chapter selectById(Chapter chapter) {
        return chapterDao.selectByPrimaryKey(chapter);
    }

    @Override
    @LogAnnotation(value = "添加章节")
    public void save(Chapter chapter) {
        chapterDao.insert(chapter);
    }

    @Override
    @LogAnnotation(value = "删除一个章节")
    public void delete(Chapter chapter) {
        chapterDao.delete(chapter);
    }

    @Override
    @LogAnnotation(value = "修改一个章节")
    public void update(Chapter chapter) {
        chapterDao.updateByPrimaryKeySelective(chapter);
    }

    @Override
    @LogAnnotation(value = "批量删除章节")
    public void deleteAll(List<String> ids) {
        chapterDao.deleteByIdList(ids);
    }
}
