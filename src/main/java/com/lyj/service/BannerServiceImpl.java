package com.lyj.service;

import com.lyj.annotation.LogAnnotation;
import com.lyj.annotation.RedisAddAnnotation;
import com.lyj.annotation.RedisDelAnnotation;
import com.lyj.dao.BannerDao;
import com.lyj.entity.Banner;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BannerServiceImpl implements BannerService {
    @Autowired
    private BannerDao bannerDao;

    @Override
    @RedisAddAnnotation("查询所有")
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Banner> findAll(Integer start, Integer size) {
        return bannerDao.selectByRowBounds(new Banner(),new RowBounds(start,size));
    }

    @Override
    @RedisAddAnnotation("查询所有条数")
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer findAllCount() {
        return bannerDao.selectCount(new Banner());
    }

    @Override
    @RedisAddAnnotation("查询一个")
    @Transactional(propagation = Propagation.SUPPORTS)
    public Banner selectById(Banner banner) {
        return bannerDao.selectByPrimaryKey(banner);
    }

    @Override
    @RedisDelAnnotation("添加")
    @LogAnnotation(value = "添加轮播图")
    public void save(Banner banner) {
        bannerDao.insert(banner);
    }

    @Override
    @RedisDelAnnotation("删除")
    @LogAnnotation(value = "删除轮播图")
    public void delete(Banner banner) {
        bannerDao.delete(banner);
    }

    @Override
    @RedisDelAnnotation("修改")
    @LogAnnotation(value = "修改轮播图基本信息")
    public void update(Banner banner) {
        bannerDao.updateByPrimaryKeySelective(banner);
    }

    @Override
    @RedisDelAnnotation("批量删除")
    @LogAnnotation(value = "批量删除轮播图")
    public void deleteAll(List<String> ids) {
        bannerDao.deleteByIdList(ids);
    }
}
