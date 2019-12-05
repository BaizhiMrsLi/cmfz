package com.lyj.service;

import com.lyj.annotation.LogAnnotation;
import com.lyj.dao.AlbumDao;
import com.lyj.entity.Album;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AlbumServiceImpl implements AlbumService {
    @Autowired
    private AlbumDao albumDao;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Album> findAll(Integer start, Integer size) {
        return albumDao.selectByRowBounds(new Album(),new RowBounds(start,size));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer findAllCount() {
        return albumDao.selectCount(new Album());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Album> queryAll() {
        return albumDao.selectAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Album selectById(Album album) {
        return albumDao.selectByPrimaryKey(album);
    }

    @Override
    @LogAnnotation(value = "添加专辑")
    public void save(Album album) {
        albumDao.insert(album);
    }

    @Override
    @LogAnnotation(value = "删除一条专辑")
    public void delete(Album album) {
        albumDao.delete(album);
    }

    @Override
    @LogAnnotation(value = "修改一个专辑")
    public void update(Album album) {
        albumDao.updateByPrimaryKeySelective(album);
    }

    @Override
    @LogAnnotation(value = "批量删除专辑")
    public void deleteAll(List<String> ids) {
        albumDao.deleteByIdList(ids);
    }
}
