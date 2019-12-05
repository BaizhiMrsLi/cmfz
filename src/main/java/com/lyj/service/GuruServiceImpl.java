package com.lyj.service;

import com.lyj.annotation.LogAnnotation;
import com.lyj.dao.GuruDao;
import com.lyj.entity.Guru;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GuruServiceImpl implements GuruService {
    @Autowired
    private GuruDao guruDao;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Guru> findAll(Integer start, Integer size) {
        return guruDao.selectByRowBounds(new Guru(),new RowBounds(start,size));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer findAllCount() {
        return guruDao.selectCount(new Guru());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Guru selectById(Guru guru) {
        return guruDao.selectByPrimaryKey(guru);
    }

    @Override
    @LogAnnotation(value = "添加上师")
    public void save(Guru guru) {
        guruDao.insert(guru);
    }

    @Override
    @LogAnnotation(value = "删除上师")
    public void delete(Guru guru) {
        guruDao.delete(guru);
    }

    @Override
    @LogAnnotation(value = "修改上师")
    public void update(Guru guru) {
        guruDao.updateByPrimaryKeySelective(guru);
    }

    @Override
    @LogAnnotation(value = "批量删除上师")
    public void deleteAll(List<String> ids) {
        guruDao.deleteByIdList(ids);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Guru> findAllGuru() {
        return guruDao.selectAll();
    }
}
